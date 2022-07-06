package com.qlive.linkmicservice

import com.qlive.avparam.*
import com.qlive.core.QLiveCallBack


/**
 * 观众连麦器
 */
interface QAudienceMicHandler {

    /**
     * 观众连麦处理器监听
     * 观众需要处理的事件
     */
    public interface QLinkMicListener {
        /**
         * 连麦模式连接状态
         * 连接成功后 连麦器会主动禁用推流器 改用rtc
         * @param state 状态
         */
        fun onConnectionStateChanged(
            state: QRoomConnectionState
        ) {
        }
        /**
         * 本地角色变化
         * @param isLinker 当前角色是不是麦上用户 上麦后true 下麦后false
         */
        fun onRoleChange(isLinker: Boolean)
    }

    /**
     *  添加连麦监听
     *  @param listener 监听
     */
    fun addLinkMicListener(listener: QLinkMicListener)

    /**
     * 移除连麦监听
     * @param listener  监听
     */
    fun removeLinkMicListener(listener: QLinkMicListener)

    /**
     * 开始上麦
     * @param extension        麦位扩展字段
     * @param cameraParams     摄像头参数 空代表不开
     * @param microphoneParams 麦克参数  空代表不开
     * @param callBack         上麦成功失败回调
     */
    fun startLink(
        extension: HashMap<String, String>?, cameraParams: QCameraParam?,
        microphoneParams: QMicrophoneParam?, callBack: QLiveCallBack<Void>?
    )

    /**
     * 我是不是麦上用户
     */
    fun isLinked(): Boolean

    /**
     * 结束连麦
     * @param callBack 操作回调
     */
    fun stopLink(callBack: QLiveCallBack<Void>?)

    /**
     * 上麦后可以切换摄像头
     *
     * @param callBack
     */
    fun switchCamera(callBack: QLiveCallBack<QCameraFace>?) //切换摄像头

    /**
     * 上麦后可以禁言本地视频流
     *
     * @param muted
     * @param callBack
     */
    fun muteCamera(muted: Boolean, callBack: QLiveCallBack<Boolean>?) //禁/不禁用本地视频流

    /**
     * 上麦后可以禁用本地音频流
     *
     * @param muted
     * @param callBack
     */
    fun muteMicrophone(muted: Boolean, callBack: QLiveCallBack<Boolean>?) //禁/不禁用本地麦克风流

    /**
     *上麦后可以设置本地视频帧回调
     * @param frameListener
     */
    fun setVideoFrameListener(frameListener: QVideoFrameListener?)

    /**
     * 上麦后可以设置音频帧回调
     *
     * @param frameListener
     */
    fun setAudioFrameListener(frameListener: QAudioFrameListener?)

    /**
     * 上麦后可以设置免费的默认美颜参数
     * @param beautySetting
     */
    fun setDefaultBeauty(beautySetting: QBeautySetting)

}