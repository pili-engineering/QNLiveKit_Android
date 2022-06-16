package com.qncube.publicchatservice;



import com.qncube.liveroomcore.been.QLiveUser;

import org.jetbrains.annotations.NotNull;

public class QPublicChat {
    public static String action_welcome = "liveroom-welcome";
    public static String action_bye = "liveroom-bye-bye";
    public static String action_like = "liveroom-like";
    public static String action_puchat = "liveroom-pubchat";
    public static String action_pubchat_custom = "liveroom-pubchat-custom";

    public String action;
    public QLiveUser sendUser;
    public String content;
    public String senderRoomId;

}
