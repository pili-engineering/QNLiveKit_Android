package com.qlive.pubchatservice;

import com.qlive.core.been.QPublicChat;

//消息监听
public  interface QPublicChatServiceLister {

    /**
     * 收到公聊消息
     *
     * @param pubChat
     */
    void onReceivePublicChat(QPublicChat pubChat);
}