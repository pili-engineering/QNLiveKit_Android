package com.qlive.shoppingservice;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品信息
 */
public class QItem implements Serializable {
    /**
     * 所在房间ID
     */
    @JSONField(name = "live_id")
    public String liveID;
    /**
     * 商品ID
     */
    @JSONField(name = "item_id")
    public String itemID;
    /**
     * 商品号
     */
    public int order;
    /**
     * 标题
     */
    public String title;
    /**
     * 商品标签 多个以,分割
     */
    public String tags;
    /**
     * 缩略图
     */
    public String thumbnail;
    /**
     * 链接
     */
    public String link;
    /**
     * 当前价格
     */
    @JSONField(name = "current_price")
    public String currentPrice;
    /**
     * 原价
     */
    @JSONField(name = "origin_price")
    public String originPrice;
    /**
     * 上架状态
     * 已下架
     * PULLED(0),
     * 已上架售卖
     * ON_SALE(1),
     * 上架不能购买
     * ONLY_DISPLAY(2);
     */
    public int status;
    /**
     * 商品扩展字段
     */
    @JSONField(name = "extends")
    public Map<String, String> extensions;
}
