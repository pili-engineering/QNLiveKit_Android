package com.qncube.linkmicservice

import com.qncube.lcommon.QCameraParam
import com.qncube.lcommon.QMicrophoneParam
import com.qiniu.droid.rtc.QNAudioFrameListener
import com.qiniu.droid.rtc.QNConnectionState
import com.qiniu.droid.rtc.QNVideoFrameListener
import com.qncube.liveroomcore.QLiveCallBack


/**
 * 观众连麦器
 */
interface QNAudienceMicLinker {

    //观众连麦监听
    public interface LinkMicListener {

        /**
         * 连麦模式连接状态
         * 连接成功后 连麦器会主动禁用推流器 改用rtc
         * @param state
         */
        fun onConnectionStateChanged(
            state: QNConnectionState
        )

        /**
         * 本地角色变化
         */
        fun lonLocalRoleChange(isLinker: Boolean)
    }

    /**
     *  添加连麦监听
     */
    fun addLinkMicListener(listener: LinkMicListener)

    /**
     * 移除连麦监听
     */
    fun removeLinkMicListener(listener: LinkMicListener)

    /**
     * 开始上麦
     *
     * @param cameraParams
     * @param microphoneParams
     * @param callBack         上麦成功失败回调
     */
    fun startLink(
        extensions: HashMap<String, String>?, cameraParams: QCameraParam?,
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


    fun switchCamera()

    fun muteLocalCamera(muted: Boolean, callBack: QLiveCallBack<Void>?)

    fun muteLocalMicrophone(muted: Boolean, callBack: QLiveCallBack<Void>?)

    fun setVideoFrameListener(frameListener: QNVideoFrameListener)

    fun setAudioFrameListener(frameListener: QNAudioFrameListener)


}