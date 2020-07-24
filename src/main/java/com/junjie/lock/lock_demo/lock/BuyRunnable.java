package com.junjie.lock.lock_demo.lock;


import java.util.Random;


public class BuyRunnable implements Runnable {
    private ISeckillService iSeckillService;
    private Random random = new Random();

    public BuyRunnable(ISeckillService iSeckillService) {
        this.iSeckillService = iSeckillService;
    }

    @Override
    public void run() {
        int i = random.nextInt(100);
        iSeckillService.buy("goodId" + i);
    }
}
