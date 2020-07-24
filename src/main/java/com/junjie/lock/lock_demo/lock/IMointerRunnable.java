package com.junjie.lock.lock_demo.lock;

import java.util.concurrent.ExecutorService;

/**
 * 秒杀监控接口
 */
public interface IMointerRunnable extends Runnable {
    /**
     * 触发商品被秒杀完事件
     */
    void fireOnSoldOut();

    void addToBeStopped(Thread t);

    void addToBeStopped(ExecutorService executor);
}
