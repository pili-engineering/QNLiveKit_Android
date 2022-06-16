package com.qlive.core.been;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Map;

public class QLiveRoomInfo {

    @JSONField(name = "live_id")
    public String liveID;

    @JSONField(name = "title")
    public String title;

    @JSONField(name = "notice")
    public String notice;

    @JSONField(name = "cover_url")
    public String coverURL;

    @JSONField(name = "extension")
    public Map<String, String> extension;

    @JSONField(name = "anchor_info")
    public QLiveUser anchor;

    @JSONField(name = "room_token")
    public String roomToken;

    @JSONField(name = "pk_id")
    public String pkID;

    @JSONField(name = "online_count")
    public long onlineCount;

    @JSONField(name = "start_time")
    public long startTime;

    @JSONField(name = "end_time")
    public long endTime;

    @JSONField(name = "chat_id")
    public String chatID;

    @JSONField(name = "push_url")
    public String pushURL;

    @JSONField(name = "hls_url")
    public String hlsURL;

    @JSONField(name = "rtmp_url")
    public String rtmpURL;

    @JSONField(name = "flv_url")
    public String flvURL;

    @JSONField(name = "pv")
    public Double pv;

    @JSONField(name = "uv")
    public Double uv;
    @JSONField(name = "total_count")
    public int totalCount;

    @JSONField(name = "total_mics")
    public int totalMics;

    @JSONField(name = "live_status")
    public int liveStatus;

    @JSONField(name = "anchor_status")
    public int anchorStatus;


}
