
1. 包 `com.junjie.lock.lock_demo.lock` 是`JDK1.8` `StampedLock`的一个实现Demo
   - 模拟了一个类似秒杀的场景，90%读，10%买。
   - test负责定时产生这些请求。
   - Mointer负责监视所有商品是否库存为0，若是结束程序。

2. 包 `com.junjie.lock.lock_demo.semaphore` 是以AOP+Semaphore实现的一个对Controller层的限流
   - RequestLimit注解定义信号量大小，若为负数或者未定义就视为不限流
   - SemaphoreAspect是限流逻辑的核心，主要采用Around的方式。
   
StampedLock的测试可以使用测试类来完成，100个不同品类商品，每个商品100个，模拟1w并发场景；
具体的使用可以查看LockDemoApplicationTests中的测试方法


Semaphore接口限流的方式使用在DemoController中，核心验证逻辑定义在SemaphoreAspect中，
整体使用AOP实现，通过自定义注解RequestLimit实现对指定接口流量控制。
