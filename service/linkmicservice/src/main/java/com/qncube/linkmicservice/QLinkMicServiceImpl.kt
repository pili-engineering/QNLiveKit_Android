package com.qncube.linkmicservice

import com.niucube.rtm.*
import com.niucube.rtm.msg.RtmTextMsg
import com.qiniu.droid.rtc.QNRenderView
import com.qiniu.jsonutil.JsonUtils
import com.qncube.linveroominner.*
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.been.QExtension
import com.qncube.liveroomcore.service.BaseService
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser
import com.qncube.liveroomcore.been.QNMicLinker

class QLinkMicServiceImpl : QLinkMicService, BaseService() {

    companion object {
        val liveroom_miclinker_join = "liveroom_miclinker_join"
        val liveroom_miclinker_left = "liveroom_miclinker_left"
        val liveroom_miclinker_kick = "liveroom_miclinker_kick"
        val liveroom_miclinker_microphone_mute = "liveroom_miclinker_microphone_mute"
        val liveroom_miclinker_camera_mute = "liveroom_miclinker_camera_mute"
        val liveroom_miclinker_extension_change = "liveroom_miclinker_extension_change"
    }

    private val mLinkDateSource = LinkDateSource()
    private val mMicLinkContext = MicLinkContext()
    private val mAudienceMicLinker: QAudienceMicLinkerImpl =
        QAudienceMicLinkerImpl(mMicLinkContext)
    private val mAnchorHostMicLinker: QAnchorHostMicLinkerImpl =
        QAnchorHostMicLinkerImpl(mMicLinkContext)
    private val mLinkMicInvitationHandler = QLinkMicInvitationHandlerImpl()

    private val mRtmMsgListener = object : RtmMsgListener {
        override fun onNewMsg(msg: String, fromId: String, toId: String): Boolean {
            when (msg.optAction()) {

                liveroom_miclinker_join -> {
                    if (mMicLinkContext.getMicLinker(user?.userId ?: "xasdaasda!#!@#") != null) {
                        return true
                    }
                    val micLinker =
                        JsonUtils.parseObject(msg.optData(), QNMicLinker::class.java) ?: return true
                    if (mMicLinkContext.addLinker(micLinker)) {
                        mMicLinkContext.mMicLinkerListeners.forEach {
                            it.onUserJoinLink(micLinker)
                        }
                    }
                }
                liveroom_miclinker_left -> {
                    if (mMicLinkContext.getMicLinker(user?.userId ?: "xasdaasda!#!@#") != null) {
                        return true
                    }
                    val micLinker =
                        JsonUtils.parseObject(msg.optData(), UidMode::class.java) ?: return true
                    mMicLinkContext.removeLinker(micLinker.uid)?.let { lincker ->
                        mMicLinkContext.mMicLinkerListeners.forEach {
                            it.onUserLeft(lincker)
                        }
                    }

                }
                liveroom_miclinker_kick
                -> {
                    val uidMsg =
                        JsonUtils.parseObject(msg.optData(), UidMsgMode::class.java) ?: return true

                    mMicLinkContext.removeLinker(uidMsg.uid)?.let { lincker ->
                        mMicLinkContext.mMicLinkerListeners.forEach {
                            it.onUserBeKick(lincker, uidMsg.msg)
                        }
                    }
                }
                liveroom_miclinker_microphone_mute
                -> {
                    val muteMode =
                        JsonUtils.parseObject(msg.optData(), MuteMode::class.java) ?: return true
                    mMicLinkContext.getMicLinker(muteMode.uid)?.let { linker ->
                        linker.isOpenMicrophone = !muteMode.mute
                        mMicLinkContext.mMicLinkerListeners.forEach {
                            it.onUserMicrophoneStatusChange(linker)
                        }
                    }
                }
                liveroom_miclinker_camera_mute
                -> {
                    val muteMode =
                        JsonUtils.parseObject(msg.optData(), MuteMode::class.java) ?: return true
                    mMicLinkContext.getMicLinker(muteMode.uid)?.let { linker ->
                        linker.isOpenCamera = !muteMode.mute
                        mMicLinkContext.mMicLinkerListeners.forEach {
                            it.onUserCameraStatusChange(linker)
                        }
                    }
                }
                liveroom_miclinker_extension_change
                -> {
                    val extMode =
                        JsonUtils.parseObject(msg.optData(), UidExtensionMode::class.java)
                            ?: return true
                    mMicLinkContext.getMicLinker(extMode.uid)?.let { linker ->
                        linker.extension.put(extMode.extension.key, extMode.extension.value)
                        mMicLinkContext.mMicLinkerListeners.forEach {
                            it.onUserExtension(linker, extMode.extension)
                        }
                    }
                }
            }
            return false
        }
    }

    /**
     * 获取当前房间所有连麦用户
     * @return
     */
    override fun getAllLinker(): MutableList<QNMicLinker> {
        return mMicLinkContext.allLinker
    }

    /**
     * 设置某人的连麦视频预览
     * 麦上用户调用  上麦后才会使用切换成rtc连麦 下麦后使用拉流预览
     * @param uid
     * @param preview
     */
    override fun setUserPreview(uid: String, preview: QNRenderView) {
        mMicLinkContext.mRtcLiveRoom.setUserCameraWindowView(uid, preview)
    }

    /**
     * 踢人
     *
     * @param uid
     */
    override fun kickOutUser(uid: String, msg: String, callBack: QLiveCallBack<Void>?) {
        val uidMsgMode = UidMsgMode()
        uidMsgMode.msg = msg
        uidMsgMode.uid = uid
        val rtmMsg = RtmTextMsg<UidMsgMode>(
            liveroom_miclinker_kick,
            uidMsgMode
        )
        RtmManager.rtmClient.sendChannelMsg(rtmMsg.toJsonString(), roomInfo?.liveId ?: "", true,
            object : RtmCallBack {
                override fun onSuccess() {
                    callBack?.onSuccess(null)
                }

                override fun onFailure(code: Int, msg: String) {
                    callBack?.onError(code, msg)
                }
            })
    }

    /**
     * 跟新扩展字段
     *
     * @param micLinker
     * @param extension
     */
    override fun updateExtension(
        micLinker: QNMicLinker,
        extension: QExtension,
        callBack: QLiveCallBack<Void>?
    ) {
        backGround {
            doWork {
                mLinkDateSource.updateExt(micLinker, extension)
                val extMode = UidExtensionMode()
                extMode.uid = micLinker.user.userId
                extMode.extension = extension
                val rtmMsg =
                    RtmTextMsg<UidExtensionMode>(
                        liveroom_miclinker_extension_change,
                        extMode
                    )
                RtmManager.rtmClient.sendChannelMsg(
                    rtmMsg.toJsonString(),
                    roomInfo?.liveId ?: "",
                    true
                )
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }

    }

    override fun addMicLinkerListener(listener: QLinkMicService.MicLinkerListener) {
        mMicLinkContext.mMicLinkerListeners.add(listener)
    }

    override fun removeMicLinkerListener(listener: QLinkMicService.MicLinkerListener?) {
        mMicLinkContext.mMicLinkerListeners.remove(listener)
    }

    /**
     * 获得连麦邀请处理
     *
     * @return
     */
    override fun getLinkMicInvitationHandler(): QNLinkMicInvitationHandler {
        return mLinkMicInvitationHandler
    }

    /**
     * 观众向主播连麦
     *
     * @return
     */
    override fun getAudienceMicLinker(): QNAudienceMicLinker {
        return mAudienceMicLinker
    }

    /**
     * 主播处理自己被连麦
     *
     * @return
     */
    override fun getAnchorHostMicLinker(): QNAnchorHostMicLinker {
        return mAnchorHostMicLinker
    }

    override fun attachRoomClient(client: QLiveClient) {
        super.attachRoomClient(client)
        if (client.clientType == QClientType.PUSHER) {
            mAnchorHostMicLinker.attachRoomClient(client)
        } else {
            mAudienceMicLinker.attachRoomClient(client)
        }
        mLinkMicInvitationHandler.attachRoomClient(client)
        RtmManager.addRtmChannelListener(mRtmMsgListener)
    }

    override fun onEntering(roomId: String, user: QLiveUser) {
        super.onEntering(roomId, user)

        if (client!!.clientType == QClientType.PUSHER) {
            mAnchorHostMicLinker.onEntering(roomId, user)
        } else {
            mAudienceMicLinker.onEntering(roomId, user)
        }
        mLinkMicInvitationHandler.onEntering(roomId, user)
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        //添加一个房主麦位
        mMicLinkContext.addLinker(QNMicLinker().apply {
            user = roomInfo.anchor
            userRoomId = roomInfo.liveId
            isOpenMicrophone = true
            isOpenCamera = true
        })

        if (client!!.clientType == QClientType.PUSHER) {
            mAnchorHostMicLinker.onJoined(roomInfo)
        } else {
            mAudienceMicLinker.onJoined(roomInfo)
        }

        mLinkMicInvitationHandler.onJoined(roomInfo)
    }

    override fun onLeft() {
        super.onLeft()

        if (client!!.clientType == QClientType.PUSHER) {
            mAnchorHostMicLinker.onLeft()
        } else {
            mAudienceMicLinker.onLeft()
        }

        mLinkMicInvitationHandler.onLeft()
    }

    override fun onDestroyed() {
        super.onDestroyed()
        if (client!!.clientType == QClientType.PUSHER) {
            mAnchorHostMicLinker.onDestroyed()
        } else {
            mAudienceMicLinker.onDestroyed()
        }

        mLinkMicInvitationHandler.onDestroyed()
        RtmManager.removeRtmChannelListener(mRtmMsgListener)
    }
}