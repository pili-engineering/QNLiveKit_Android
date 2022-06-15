package com.qncube.chatservice;


import com.qncube.liveroomcore.QLiveCallBack;
import com.qncube.liveroomcore.QLiveService;

/**
 * 聊天室服务
 */
public interface QChatRoomService extends QLiveService {

    public void addChatServiceListener(QChatRoomServiceListener chatServiceListener);

    public void removeChatServiceListener(QChatRoomServiceListener chatServiceListener);

    /**
     *  发c2c消息
     * @param msg
     * @param memberID
     * @param callBack
     */
    void sendCustomC2CMsg(String msg, String memberID, QLiveCallBack<Void> callBack);

    /**
     * 发群消息
     * @param msg
     * @param callBack
     */
    void sendCustomGroupMsg(String msg, QLiveCallBack<Void> callBack);


    /**
     * 踢人
     * @param msg
     * @param memberID
     * @param callBack
     */
    void kickUser(String msg, String memberID, QLiveCallBack<Void> callBack);

    /**
     * 禁言
     * @param isMute
     * @param msg
     * @param memberID
     * @param duration
     * @param callBack
     */
    void muteUser(boolean isMute , String msg, String memberID, long duration , QLiveCallBack<Void> callBack);

    /**
     * 添加管理员
     * @param memberID
     * @param callBack
     */
    void addAdmin( String memberID, QLiveCallBack<Void> callBack);

    /**
     * 移除管理员
     * @param msg
     * @param memberID
     * @param callBack
     */
    void removeAdmin(String msg, String memberID, QLiveCallBack<Void> callBack);

}
