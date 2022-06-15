package com.qncube.liveroomcore.been;

import com.alibaba.fastjson.annotation.JSONField;
import com.qncube.liveroomcore.been.QLiveUser;

import java.util.HashMap;

//连麦邀请
public class QInvitation {

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