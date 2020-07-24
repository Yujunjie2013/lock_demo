package com.junjie.lock.lock_demo.service;

import java.util.Map;

public interface IGoodsService {
    /**
     * 根据id虎丘商品数量
     * @param goodId id
     * @return 数量
     */
    Integer getGoodById(String goodId);

    /**
     * 更新商品
     *
     * @param item 商品id
     * @param good 数量
     */
    void updateGoodById(String item, Integer good);

    Map<String,Integer> getALl();
}
