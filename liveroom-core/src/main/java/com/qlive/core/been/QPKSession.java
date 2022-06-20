package com.qlive.core.been;

import com.qlive.core.been.QLiveUser;

import java.io.Serializable;
import java.util.Map;

public class QPKSession implements Serializable {

    //PK场次ID
    public String sessionID;
    //发起方
    public QLiveUser initiator;
    //接受方
    public QLiveUser receiver;
    //发起方所在房间
    public String initiatorRoomID;
    //接受方所在房间
    public String receiverRoomID;
    //扩展字段
    public Map<String, String> extension;
    //pk 状态 0邀请过程  1pk中 2结束 其他自定义状态比如惩罚时间
    public int status;
    //pk开始时间戳
    public long startTimeStamp;

}