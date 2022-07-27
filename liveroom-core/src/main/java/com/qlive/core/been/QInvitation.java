package com.qlive.core.been;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 邀请信息
 */
public class QInvitation implements Serializable {

    /**
     * 发起方
     */
    public QLiveUser initiator;
    /**
     * 接收方
     */
    public QLiveUser receiver;
    /**
     * 发起方所在房间ID
     */
    @JSONField(name = "initiatorRoomId")
    public String initiatorRoomID;
    /**
     * 接收方所在房间ID
     */
    @JSONField(name = "receiverRoomId")
    public String receiverRoomID;
    /**
     * 扩展字段
     */
    public HashMap<String, String> extension;
    /**
     * 邀请ID
     */
    @JSONField(serialize = false)
    public int invitationID;

}