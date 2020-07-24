package com.junjie.lock.lock_demo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 定义切面并发数控制注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {
    /**
     * 流量控制
     *
     * @return 默认10
     */
    int limitValue() default 10;

    /**
     * 默认等待时长
     *
     * @return 1
     */
    int timeOut() default 200;

    /**
     * 等待时长单位，默认毫秒
     *
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

}
