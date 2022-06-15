package com.qncube.publicchatservice;

//消息监听
public  interface QPublicChatServiceLister {

    /**
     * 收到公聊消息
     *
     * @param pubChat
     */
    void onReceivePublicChat(QPublicChat pubChat);
}