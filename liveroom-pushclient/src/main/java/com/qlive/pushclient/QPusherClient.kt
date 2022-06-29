package com.qlive.pushclient

import com.qlive.avparam.*
import com.qlive.core.QLiveCallBack
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.rtclive.QPushRenderView

//推流客户端
interface QPusherClient : QLiveClient {
    fun joinRoom(roomID: String, callBack: QLiveCallBack<QLiveRoomInfo>?) //加入房间
    fun closeRoom(callBack: QLiveCallBack<Void>?) //关闭房间
    override fun destroy() //销毁
    fun setConnectionStatusLister(connectionStatusLister: QConnectionStatusLister?) //推流连接状态监听
    fun enableCamera(cameraParam: QCameraParam?, renderView: QPushRenderView) //启动视频采集 和预览
    fun enableMicrophone(microphoneParam: QMicrophoneParam?) //启动麦克参数
    fun switchCamera(callBack: QLiveCallBack<QCameraFace>?) //切换摄像头
    fun muteCamera(muted: Boolean, callBack: QLiveCallBack<Boolean>?) //禁/不禁用本地视频流
    fun muteMicrophone(muted: Boolean, callBack: QLiveCallBack<Boolean>?) //禁/不禁用本地麦克风流
    fun setVideoFrameListener(frameListener: QVideoFrameListener?) //设置本地摄像头数据监听
    fun setAudioFrameListener(frameListener: QAudioFrameListener?) //设置本地音频数据监听

    fun pause()
    fun resume()
}