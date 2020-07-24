package com.junjie.lock.lock_demo.lock;


import java.util.Random;


public class ShowRunnable implements Runnable {
    private final ISeckillService iSeckillService;
    private Random random = new Random();

    public ShowRunnable(ISeckillService iSeckillService) {
        this.iSeckillService = iSeckillService;
    }

    @Override
    public void run() {
        int i = random.nextInt(100);
        iSeckillService.showRemaining("goodId" + i);
    }
}
