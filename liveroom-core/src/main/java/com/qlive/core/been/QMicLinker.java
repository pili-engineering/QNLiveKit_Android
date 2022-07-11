package com.qlive.core.been;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 连麦用户
 */
public class QMicLinker implements Serializable {

    /**
     * 麦上用户资料
     */
    public QLiveUser user;
    @JSONField(name = "live_id")
    /**
     * 连麦用户所在房间ID
     */
    public String userRoomID;
    /**
     * 扩展字段
     */
    @JSONField(name = "extends")
    public HashMap<String, String> extension;

    /**
     * 是否开麦克风
     */
    @JSONField(name = "mic")
    public boolean isOpenMicrophone;
    /**
     * 是否开摄像头
     */
    @JSONField(name = "camera")
    public boolean isOpenCamera;
}

