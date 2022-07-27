package com.qlive.core.been;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 创建房间参数
 */
public class QCreateRoomParam implements Serializable {

    /**
     * 房间标题
     */
    public String title;
    /**
     * 房间公告
     */
    @JSONField(name = "notice")
    public String notice;
    /**
     * 封面
     */
    @JSONField(name = "cover_url")
    public String coverURL;
    /**
     * 扩展字段
     */
    @JSONField(name = "extension")
    public HashMap<String, String> extension;
}
