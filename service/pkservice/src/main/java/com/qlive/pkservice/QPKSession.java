package com.qlive.pkservice;

import com.alibaba.fastjson.annotation.JSONField;
import com.qlive.core.been.QLiveUser;

import java.io.Serializable;
import java.util.Map;

/**
 * pk 会话
 */
public class QPKSession implements Serializable {

    /**
     * PK场次ID
     */
    @JSONField(name = "relay_id")
    public String sessionID;
    /**
     * 发起方
     */
    public QLiveUser initiator;
    /**
     * 接受方
     */
    public QLiveUser receiver;
    /**
     * 发起方所在房间
     */
    @JSONField(name = "initiatorRoomId")
    public String initiatorRoomID;
    /**
     * 接受方所在房间
     */
    @JSONField(name = "receiverRoomId")
    public String receiverRoomID;
    /**
     * 扩展字段
     */
    public Map<String, String> extension;
    /**
     * pk状态
     *     RelaySessionStatusWaitAgree(0),//等待接收方同意
     *     RelaySessionStatusAgreed(1),//接收方已同意
     *     RelaySessionStatusInitSuccess(2),//发起方已经完成跨房，等待对方完成
     *     RelaySessionStatusRecvSuccess(3),//接收方已经完成跨房，等待对方完成
     *     RelaySessionStatusSuccess(4),//两方都完成跨房
     *     RelaySessionStatusRejected(5),//接收方拒绝
     *     RelaySessionStatusStopped(6),//结束
     */
    @JSONField(name = "relay_status")
    public int status;
    /**
     * pk开始时间戳
     */
    @JSONField(name = "startTimeStamp")
    public long startTimeStamp;

}