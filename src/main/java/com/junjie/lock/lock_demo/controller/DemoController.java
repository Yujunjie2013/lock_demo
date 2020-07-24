package com.junjie.lock.lock_demo.controller;

import com.junjie.lock.lock_demo.annotation.RequestLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @RequestLimit(limitValue = 5)
    @GetMapping("/limit")
    public String limit() {
        try {
            System.err.println("limit:" + Thread.currentThread().getName());
            //模拟耗时5秒钟
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Demo-limit请求成功";
    }


    @RequestLimit(limitValue = 1, timeOut = 1, timeUnit = TimeUnit.SECONDS)//并发1，获取锁等待1秒
    @GetMapping("/xixi")
    public String xixi() {
        try {
            System.err.println("hello:" + Thread.currentThread().getName());
            //模拟耗时2秒钟
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Demo-hello请求成功";
    }


    @GetMapping("/hello")
    public String hello() {
        try {
            System.err.println("hello:" + Thread.currentThread().getName());
            //模拟耗时2秒钟
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Demo-hello请求成功";
    }
}
