package com.qncube.playerclient;


import com.qncube.liveroomcore.QLiveCallBack;
import com.qncube.liveroomcore.QNLiveClient;
import com.qncube.liveroomcore.QRenderView;
import com.qncube.liveroomcore.mode.QLiveRoomInfo;

import org.jetbrains.annotations.NotNull;

/**
 * 拉流客户端
 */
public interface QNLivePullClient extends QNLiveClient {

    void joinRoom(String roomID, QLiveCallBack<QLiveRoomInfo> callBack);        //加入房间

    void leaveRoom(QLiveCallBack<Void> callBack);                               //关闭房间

    void destroy();                                                              //销毁

    void play(@NotNull QRenderView renderView);                                           //绑定播放器渲染视图

    void setPlayerEventListener(QPlayerEventListener playerEventListener);       //设置拉流端事件回调

}


