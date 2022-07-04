package com.qlive.pubchatservice;

import com.qlive.core.QLiveCallBack;
import com.qlive.core.QLiveService;
import com.qlive.core.been.QPublicChat;

public interface QPublicChatService extends QLiveService {


    /**
     * 发送 聊天室聊天
     *
     * @param msg
     */
    public void sendPublicChat(String msg, QLiveCallBack<QPublicChat> callBack);

    /**
     * 发送 欢迎进入消息
     *
     * @param msg
     */
    public void sendWelCome(String msg, QLiveCallBack<QPublicChat> callBack);

    /**
     * 发送 拜拜
     *
     * @param msg
     */
    public void sendByeBye(String msg, QLiveCallBack<QPublicChat> callBack);

    /**
     * 点赞
     *
     * @param msg
     * @param callBack
     */
    public void sendLike(String msg, QLiveCallBack<QPublicChat> callBack);

    /**
     * 自定义要显示在公屏上的消息
     *
     * @param action
     * @param msg
     * @param callBack
     */
    public void sendCustomPubChat(String action, String msg,  QLiveCallBack<QPublicChat> callBack);


    /**
     * 往本地公屏插入消息 不发送到远端
     */
    public void pubMsgToLocal(QPublicChat chatModel);

    public void addServiceLister(QPublicChatServiceLister lister);

    public void removeServiceLister(QPublicChatServiceLister lister);
}
