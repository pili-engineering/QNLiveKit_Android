package com.qlive.core.been;

import com.alibaba.fastjson.annotation.JSONField;
import com.qlive.core.been.QLiveUser;

import java.io.Serializable;
import java.util.Map;

public class QPKSession implements Serializable {

    //PK场次ID
    @JSONField(name = "relay_id")
    public String sessionID;
    //发起方
    public QLiveUser initiator;
    //接受方
    public QLiveUser receiver;
    //发起方所在房间
    @JSONField(name = "initiatorRoomId")
    public String initiatorRoomID;
    //接受方所在房间
    @JSONField(name = "receiverRoomId")
    public String receiverRoomID;
    //扩展字段
    public Map<String, String> extension;
    //pk 状态 0邀请过程  1pk中 2结束 其他自定义状态比如惩罚时间
    @JSONField(name = "relay_status")
    public int status;
    //pk开始时间戳
    @JSONField(name = "startTimeStamp")
    public long startTimeStamp;

}