package com.junjie.lock.lock_demo.service;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@Service
public class GoodsServiceImpl implements IGoodsService {
    /**
     * key商品id,value是商品数量
     */
    private HashMap<String, Integer> hashMap = new HashMap<>();

    @PostConstruct
    public void init() {
        IntStream.range(0, 100)
                .forEach(i -> {
                    //模拟一百件商品，每件商品数量100个
                    hashMap.put("goodId" + i, 100);
                });
    }

    @Override
    public Integer getGoodById(String goodId) {
        //TODO 实际业务中肯定是去数据库或redis中查询数据的，这里为了简单实用集合进行模拟
        return hashMap.get(goodId);
    }

    @Override
    public void updateGoodById(String item, Integer good) {
        hashMap.put(item, good);
    }

    @Override
    public Map<String, Integer> getALl() {
        return hashMap;
    }
}
