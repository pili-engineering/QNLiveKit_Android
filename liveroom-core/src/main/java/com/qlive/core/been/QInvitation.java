package com.qlive.core.been;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;

//连麦邀请
public class QInvitation implements Serializable {

    public QLiveUser initiator;
    public QLiveUser receiver;
    public String initiatorRoomID;
    public String receiverRoomID;
    public HashMap<String, String> extension;
    //连麦类型 用户向主播连麦  / 主播跨房连麦
    public int linkType;

    @JSONField(serialize = false)
    public int invitationID;

}