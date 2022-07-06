package com.qlive.pushclient

import com.qlive.avparam.*
import com.qlive.core.*
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.rtclive.QPushRenderView

/**
 *推流客户端
 */
interface QPusherClient : QLiveClient {

    /**
     * 获取插件服务实例
     *
     * @param serviceClass 插件的类
     * @param <T>
     * @return 返回 服务插件对象
     */
    override fun <T : QLiveService> getService(serviceClass: Class<T>): T?

    /**
     * 设置直播状态回调
     * @param liveStatusListener
     */
    override fun setLiveStatusListener(liveStatusListener: QLiveStatusListener?)

    /**
     * 当前客户端类型
     *  QClientType.PUSHER 代表推流端
     *  QClientType.PLAYER 代表拉流端
     * @return QClientType
     */
    override fun getClientType(): QClientType

    /**
     *启动视频采集 和预览
     *
     * @param cameraParam 摄像头参数
     * @param renderView 预览窗口
     */
    fun enableCamera(cameraParam: QCameraParam?, renderView: QPushRenderView)

    /**
     * 启动麦克采集
     *
     * @param microphoneParam 麦克风参数
     */
    fun enableMicrophone(microphoneParam: QMicrophoneParam?) //启动麦克参数

    /**
     * 加入房间
     *
     * @param roomID 房间ID
     * @param callBack 回调函数
     */
    fun joinRoom(roomID: String, callBack: QLiveCallBack<QLiveRoomInfo>?) //加入房间

    /**
     * 主播关闭房间
     * @param callBack
     */
    fun closeRoom(callBack: QLiveCallBack<Void>?)

    /**
     * 销毁推流客户端
     * 销毁后不能使用
     */
    override fun destroy() //销毁

    /**
     *主播设置推流链接状态监听
     * @param connectionStatusLister
     */
    fun setConnectionStatusLister(connectionStatusLister: QConnectionStatusLister?) //推流连接状态监听

    /**
     * Switch camera
     * @param callBack
     */
    fun switchCamera(callBack: QLiveCallBack<QCameraFace>?) //切换摄像头

    /**
     * 禁/不禁用本地视频流
     * 禁用后本地能看到预览 观众不能看到主播的画面
     * @param muted 是否禁用
     * @param callBack
     */
    fun muteCamera(muted: Boolean, callBack: QLiveCallBack<Boolean>?) //禁/不禁用本地视频流

    /**
     * 禁用麦克风推流
     * @param muted
     * @param callBack
     */
    fun muteMicrophone(muted: Boolean, callBack: QLiveCallBack<Boolean>?) //禁/不禁用本地麦克风流

    /**
     * 设置视频帧回调
     * @param frameListener 视频帧监听
     */
    fun setVideoFrameListener(frameListener: QVideoFrameListener?) //设置本地摄像头数据监听

    /**
     * 设置本地音频数据监听
     * @param frameListener
     */
    fun setAudioFrameListener(frameListener: QAudioFrameListener?) //设置本地音频数据监听

    /**
     * 暂停
     */
    fun pause()

    /**
     *恢复
     */
    fun resume()

    /**
     * 设置默认免费版美颜参数
     * @param beautySetting 美颜参数
     */
    fun setDefaultBeauty(beautySetting: QBeautySetting) //免费的默认美颜调节
}