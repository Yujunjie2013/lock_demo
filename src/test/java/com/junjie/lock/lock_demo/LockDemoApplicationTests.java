package com.junjie.lock.lock_demo;

import com.junjie.lock.lock_demo.lock.*;
import com.junjie.lock.lock_demo.service.IGoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

@SpringBootTest
class LockDemoApplicationTests {

    @Autowired
    private ISeckillService iSeckillService;
    @Autowired
    private IGoodsService iGoodsService;

    @Test
    void contextLoads() {
        ThreadPoolExecutor buyers = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
        ThreadPoolExecutor showers = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
        Random random = new Random();
        BuyRunnable buyRunnable = new BuyRunnable(iSeckillService);
        ShowRunnable showRunnable = new ShowRunnable(iSeckillService);

        Thread runner = new Thread(() -> {
            while (true) {
                if (Thread.interrupted()) {
                    System.out.println("结束模拟秒杀请求...");
                    break;
                }
                IntStream.range(0, 10000).forEach(i -> {
                    int r = random.nextInt(100);
                    if (r < 90) {//模拟10%写，90%读场景
                        if (!showers.isShutdown())
                            showers.execute(showRunnable);
                    } else {
                        if (!buyers.isShutdown())
                            buyers.execute(buyRunnable);
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        IMointerRunnable mointerRunnable = new MointerRunnable(iSeckillService, iGoodsService);
        mointerRunnable.addToBeStopped(runner);
        mointerRunnable.addToBeStopped(buyers);
        mointerRunnable.addToBeStopped(showers);

        Thread monitor = new Thread(mointerRunnable);
        monitor.setDaemon(true);

        runner.start();
        monitor.start();
        try {
            monitor.join();
            System.out.println("秒杀结束了");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
