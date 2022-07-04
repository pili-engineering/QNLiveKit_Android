package com.qlive.core.been;



import com.qlive.core.been.QLiveUser;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 弹幕实体
 */
public class QDanmaku implements Serializable {
    public static String action_danmu = "living_danmu";
    public QLiveUser sendUser;
    /**
     * 消息内容
     */
    public String content;
    public String senderRoomID;
    /**
     * 扩展字段
     */
    public HashMap<String, String> extension;
}


