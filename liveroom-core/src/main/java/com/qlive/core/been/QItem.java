package com.qlive.core.been;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品信息
 */
public class QItem {
    @JSONField(name = "live_id")
    public String liveID;
    @JSONField(name = "item_id")
    public String itemID;
    public int order;
    public String title;
    public String tags;
    public String thumbnail;
    public String link;
    @JSONField(name = "current_price")
    public String currentPrice;
    @JSONField(name = "origin_price")
    public String originPrice;
    public int status;
    @JSONField(name = "extends")
    public Map<String, String> extensions;
}
