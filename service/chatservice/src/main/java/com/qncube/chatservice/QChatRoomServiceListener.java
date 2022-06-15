package com.qncube.chatservice;


import org.jetbrains.annotations.NotNull;

/**
 * 聊天室服务回调
 */
public interface QChatRoomServiceListener {

    /**
     * 有人加入聊天室
     */
   default void onUserJoin(@NotNull String memberID){};

    /**
     * 有人离开聊天室
     */
    default void onUserLeft(@NotNull String memberID){}

    /**
     * 收到c2c消息
     */
    default  void onReceivedC2CMsg(@NotNull String msg,@NotNull  String fromID,@NotNull  String toID){}

    /**
     * 收到群消息
     */
    default  void onReceivedGroupMsg(@NotNull String msg,@NotNull  String fromID, @NotNull String toID){}

    /**
     * 有人被踢
     */
    default void onUserKicked(@NotNull String memberID){}

    /**
     * 有人被禁言
     */
    default void onUserBeMuted(@NotNull boolean isMute, @NotNull  String memberID,@NotNull  long duration){}

    default void onAdminAdd(@NotNull String memberID){}

    default void onAdminRemoved(@NotNull String memberID,@NotNull  String reason){}
}
