package com.junjie.lock.lock_demo.lock;

import java.util.concurrent.locks.StampedLock;

/**
 * 秒杀接口
 */
public interface ISeckillService {
    /**
     * 购买 item
     *
     * @param item 购买 item
     */
    void buy(String item);


    /**
     * 当前item余量
     *
     * @return 当前item余量
     */
    Integer showRemaining(String item);

    StampedLock stampedLock();

}
