package com.qncube.liveroomcore;


import com.qncube.lcommon.QPlayerEventListener;
import com.qncube.lcommon.QPlayerRenderView;
import com.qncube.lcommon.QRenderView;
import com.qncube.liveroomcore.been.QLiveRoomInfo;

import org.jetbrains.annotations.NotNull;

/**
 * 拉流客户端
 */
public interface QPlayerClient extends QLiveClient {

    void joinRoom(String roomID, QLiveCallBack<QLiveRoomInfo> callBack);        //加入房间

    void leaveRoom(QLiveCallBack<Void> callBack);                               //关闭房间

    void destroy();                                                              //销毁

    void play(@NotNull QPlayerRenderView renderView);                                           //绑定播放器渲染视图

    void setPlayerEventListener(QPlayerEventListener playerEventListener);       //设置拉流端事件回调

}


