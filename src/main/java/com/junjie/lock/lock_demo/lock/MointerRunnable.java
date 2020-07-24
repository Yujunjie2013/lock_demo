package com.junjie.lock.lock_demo.lock;

import com.junjie.lock.lock_demo.service.IGoodsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * 监控实现类
 */
public class MointerRunnable implements IMointerRunnable {
    private final List<ExecutorService> toBeStoppedExecutors = new ArrayList<>();

    private final List<Thread> toBeStoppedThreads = new ArrayList<>();

    private ISeckillService iSeckillService;
    private IGoodsService iGoodsService;

    public MointerRunnable(ISeckillService iSeckillService, IGoodsService iGoodsService) {
        this.iSeckillService = iSeckillService;
        this.iGoodsService = iGoodsService;
    }

    @Override
    public void fireOnSoldOut() {
        for (Thread thread : toBeStoppedThreads) {
            thread.interrupt();
        }
        for (ExecutorService es : toBeStoppedExecutors) {
            es.shutdown();
        }
    }

    @Override
    public void addToBeStopped(Thread t) {
        toBeStoppedThreads.add(t);
    }

    @Override
    public void addToBeStopped(ExecutorService executor) {
        toBeStoppedExecutors.add(executor);
    }

    @Override
    public void run() {
        while (true) {
            if (Thread.interrupted()) {
                System.out.println("........监控结束了.......");
                break;
            }
            boolean soldout = true;
            long stamp = iSeckillService.stampedLock().readLock();
            try {
                soldout = isSoldout();
            } finally {
                iSeckillService.stampedLock().unlockRead(stamp);
            }

            if (soldout) {
                //如果售罄了，终止所有正在运行的线程
                fireOnSoldOut();
                break;
            }
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是否售罄
     *
     * @return true 售罄
     */
    private boolean isSoldout() {
        boolean soldout = true;
        Map<String, Integer> cache = iGoodsService.getALl();
        for (Map.Entry<String, Integer> entry : cache.entrySet()) {
            Integer v = entry.getValue();
            if (v != null && v > 0) {
                soldout = false;
                break;
            }
        }
        return soldout;
    }
}
