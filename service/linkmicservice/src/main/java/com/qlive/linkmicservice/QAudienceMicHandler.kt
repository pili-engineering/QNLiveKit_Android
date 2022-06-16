package com.qlive.linkmicservice

import com.qlive.avparam.*
import com.qlive.core.QLiveCallBack


/**
 * 观众连麦器
 */
interface QAudienceMicHandler {
    //观众连麦监听
    public interface QLinkMicListener {
        /**
         * 连麦模式连接状态
         * 连接成功后 连麦器会主动禁用推流器 改用rtc
         * @param state
         */
        fun onConnectionStateChanged(
            state: QRoomConnectionState
        ) {
        }
        /**
         * 本地角色变化
         */
        fun onRoleChange(isLinker: Boolean)
    }

    /**
     *  添加连麦监听
     */
    fun addLinkMicListener(listener: QLinkMicListener)

    /**
     * 移除连麦监听
     */
    fun removeLinkMicListener(listener: QLinkMicListener)

    /**
     * 开始上麦
     *
     * @param cameraParams
     * @param microphoneParams
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
     */
    fun stopLink(callBack: QLiveCallBack<Void>?)

    fun switchCamera(callBack: QLiveCallBack<QCameraFace>?) //切换摄像头
    fun muteCamera(muted: Boolean, callBack: QLiveCallBack<Boolean>?) //禁/不禁用本地视频流
    fun muteMicrophone(muted: Boolean, callBack: QLiveCallBack<Boolean>?) //禁/不禁用本地麦克风流
    fun setVideoFrameListener(frameListener: QVideoFrameListener?)
    fun setAudioFrameListener(frameListener: QAudioFrameListener?)


}