package com.qlive.core.been;

import com.alibaba.fastjson.annotation.JSONField;
import com.qlive.core.QLiveStatus;

import java.io.Serializable;
import java.util.Map;

/**
 * 房间信息
 */
public class QLiveRoomInfo implements Serializable {

    /**
     * 房间ID
     */
    @JSONField(name = "live_id")
    public String liveID;

    /**
     * 房间标题
     */
    @JSONField(name = "title")
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
    public Map<String, String> extension;

    /**
     * 主播信息
     */
    @JSONField(name = "anchor_info")
    public QLiveUser anchor;

    @JSONField(name = "room_token")
    public String roomToken;

    /**
     * 当前房间的pk会话信息
     */
    @JSONField(name = "pk_id")
    public String pkID;

    /**
     * 在线人数
     */
    @JSONField(name = "online_count")
    public long onlineCount;

    /**
     * 开始时间
     */
    @JSONField(name = "start_time")
    public long startTime;

    /**
     * 结束时间
     */
    @JSONField(name = "end_time")
    public long endTime;

    /**
     * 聊天室ID
     */
    @JSONField(name = "chat_id")
    public String chatID;

    /**
     * 推流地址
     */
    @JSONField(name = "push_url")
    public String pushURL;

    /**
     * 拉流地址
     */
    @JSONField(name = "hls_url")
    public String hlsURL;
    /**
     * 拉流地址
     */
    @JSONField(name = "rtmp_url")
    public String rtmpURL;
    /**
     * 拉流地址
     */
    @JSONField(name = "flv_url")
    public String flvURL;
    /**
     * pv
     */
    @JSONField(name = "pv")
    public Double pv;
    /**
     * uv
     */
    @JSONField(name = "uv")
    public Double uv;
    /**
     * 总人数
     */
    @JSONField(name = "total_count")
    public int totalCount;

    /**
     * 连麦者数量
     */
    @JSONField(name = "total_mics")
    public int totalMics;

    /**
     * 直播间状态
     */
    @JSONField(name = "live_status")
    public int liveStatus;

    /**
     * 主播在线状态
     */
    @JSONField(name = "AnchorStatus")
    public int anchorStatus;

}
