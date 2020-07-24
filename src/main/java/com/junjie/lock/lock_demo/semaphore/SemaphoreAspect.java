package com.junjie.lock.lock_demo.semaphore;

import com.junjie.lock.lock_demo.annotation.RequestLimit;
import com.junjie.lock.lock_demo.exception.RequestLimitException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

@Component
@Aspect
public class SemaphoreAspect {

    private ConcurrentHashMap<String, Semaphore> semaphoreMap = new ConcurrentHashMap<>();

//    @Pointcut("execution(public * com.junjie.lock.lock_demo.controller..*(..))")
//    public void limitPoint() {
//    }

    /**
     * 使用注解做切点
     */
    @Pointcut("@annotation(com.junjie.lock.lock_demo.annotation.RequestLimit)")
    public void limitPoint() {
    }

    @Around("limitPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            RequestLimit requestLimit = method.getAnnotation(RequestLimit.class);
            if (requestLimit != null && requestLimit.limitValue() > 0) {
                String name = method.getName();
                int limitValue = requestLimit.limitValue();
                Semaphore semaphore;
                if ((semaphore = semaphoreMap.get(name)) == null) {
                    semaphoreMap.put(name, semaphore = new Semaphore(limitValue));
                }
                boolean acquire = semaphore.tryAcquire(requestLimit.timeOut(), requestLimit.timeUnit());
                if (!acquire) {
                    throw new RequestLimitException("已超过该接口最大并发数");
                }
                try {
                    return pjp.proceed();
                } finally {
                    //用完归还信号量
                    semaphore.release();
                }
            }
        }
        return pjp.proceed();
    }
}
