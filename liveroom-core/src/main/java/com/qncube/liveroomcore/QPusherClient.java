package com.qncube.liveroomcore;

import com.qncube.lcommon.QAudioFrameListener;
import com.qncube.lcommon.QCameraFace;
import com.qncube.lcommon.QCameraParam;
import com.qncube.lcommon.QConnectionStatusLister;
import com.qncube.lcommon.QMicrophoneParam;
import com.qncube.lcommon.QPushRenderView;
import com.qncube.lcommon.QVideoFrameListener;
import com.qncube.liveroomcore.been.QLiveRoomInfo;

import org.jetbrains.annotations.NotNull;

//推流客户端
public interface QPusherClient extends QLiveClient {

    void joinRoom(String roomID, QLiveCallBack<QLiveRoomInfo> callBack);        //加入房间

    void closeRoom(QLiveCallBack<Void> callBack);                               //关闭房间

    void destroy();                                                              //销毁

    void setConnectionStatusLister(QConnectionStatusLister connectionStatusLister);    //推流连接状态监听

    void enableCamera(QCameraParam cameraParam, @NotNull QPushRenderView renderView);  //启动视频采集 和预览

    void enableMicrophone(QMicrophoneParam microphoneParam);                       //启动麦克参数

    void switchCamera(QLiveCallBack<QCameraFace> callBack);                        //切换摄像头

    void muteCamera(boolean muted, QLiveCallBack<Boolean> callBack);                //禁/不禁用本地视频流

    void muteMicrophone(boolean muted, QLiveCallBack<Boolean> callBack);            //禁/不禁用本地麦克风流

    void setVideoFrameListener(QVideoFrameListener frameListener);                 //设置本地摄像头数据监听

    void setAudioFrameListener(QAudioFrameListener frameListener);                 //设置本地音频数据监听

    QPushRenderView getPushRenderView(); //获得已经绑定的RenderView
}