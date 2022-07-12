package com.qlive.pkservice;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Map;

public class PKInfo implements Serializable {

    @JSONField(name = "init_room_id")
    public String initRoomId;
    @JSONField(name = "stop_at")
    public String stopAt;
    @JSONField(name = "start_at")
    public String startAt;
    @JSONField(name = "created_at")
    public long createdAt;
    @JSONField(name = "id")
    public String id;
    @JSONField(name = "init_user_id")
    public String initUserId;
    @JSONField(name = "recv_user_id")
    public String recvUserId;
    @JSONField(name = "extensions")
    public Map<String, String> extensions;
    @JSONField(name = "status")
    public Integer status;
    @JSONField(name = "recv_room_id")
    public String recvRoomId;
   
}
