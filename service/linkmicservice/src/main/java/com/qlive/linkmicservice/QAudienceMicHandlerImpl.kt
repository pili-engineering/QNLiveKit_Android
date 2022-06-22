package com.qlive.linkmicservice

import android.util.Log
import com.qlive.avparam.QPlayerProvider
import com.qlive.rtm.RtmException
import com.qlive.rtm.RtmManager
import com.qlive.rtm.msg.RtmTextMsg
import com.qlive.rtm.sendChannelMsg
import com.qlive.rtclive.*
import com.qiniu.droid.rtc.*
import com.qlive.jsonutil.JsonUtils
import com.qlive.avparam.*
import com.qlive.linkmicservice.QLinkMicServiceImpl.Companion.liveroom_miclinker_camera_mute
import com.qlive.linkmicservice.QLinkMicServiceImpl.Companion.liveroom_miclinker_join
import com.qlive.linkmicservice.QLinkMicServiceImpl.Companion.liveroom_miclinker_left
import com.qlive.linkmicservice.QLinkMicServiceImpl.Companion.liveroom_miclinker_microphone_mute
import com.qlive.coreimpl.*
import com.qlive.core.*
import com.qlive.coreimpl.BaseService
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QMicLinker
import com.qlive.coreimpl.datesource.LinkDateSource
import com.qlive.coreimpl.model.MuteMode
import com.qlive.coreimpl.model.UidMode
import com.qlive.coreimpl.util.backGround
import com.qlive.coreimpl.util.getCode
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class QAudienceMicHandlerImpl(val context: MicLinkContext) : QAudienceMicHandler, BaseService() {

    init {
        context.hostLeftCall = {
            if (isLinked()) {
                QLiveLogUtil.LogE("房主下麦")
                stopLink(null)
            }
        }
//        context.downMicCall = {
//            if (isLinked()) {
//                QLiveLogUtil.LogE("pk下麦")
//                stopLink(null)
//            }
//        }
    }

    private val mLinkDateSource = LinkDateSource()
    private var mPlayer: QIPlayer? = null
    private val mQLinkMicListeners = ArrayList<QAudienceMicHandler.QLinkMicListener>()
    private val mMeLinker: QMicLinker?
        get() {
            return context.getMicLinker(user?.userId ?: "hjhb")
        }

    private fun compare(old: List<QMicLinker>, new: List<QMicLinker>): Boolean {
        if (old.size - new.size != 1) {
            return false
        }
        var oldKey = ""
        old.forEach {
            if (it.user.userId != currentRoomInfo?.anchor?.userId) {
                oldKey += it.user.userId
            }
        }
        var newKey = ""
        new.forEach {
            if (it.user.userId != currentRoomInfo?.anchor?.userId) {
                newKey += it.user.userId
            }
        }
        return newKey == oldKey
    }

    //麦位同步
    private val mMicListJob = com.qlive.coreimpl.Scheduler(5000) {
        if (currentRoomInfo == null) {
            return@Scheduler
        }
        if (isLinked()) {
            return@Scheduler
        }
        backGround {
            doWork {
                val list = mLinkDateSource.getMicList(currentRoomInfo?.liveID ?: "")
                if (isLinked()) {
                    return@doWork
                }
                if (compare(context.allLinker, list)) {
                    return@doWork
                }
                val toRemve = LinkedList<QMicLinker>()
                val newAdd = LinkedList<QMicLinker>()

                context.allLinker.forEach { old ->
                    var isContainer = false
                    if (old.user.userId == currentRoomInfo?.anchor?.userId) {
                        isContainer = true
                    } else {
                        list.forEach { new ->
                            if (new.user.userId == old.user.userId) {
                                isContainer = true
                            }
                        }
                    }
                    if (!isContainer) {
                        toRemve.add(old)
                    }
                }
                toRemve.forEach { linck ->
                    context.mQLinkMicServiceListeners.forEach {
                        it.onLinkerLeft(linck)
                    }
                }
                context.allLinker.removeAll(toRemve)

                list.forEach { linck ->
                    if (context.addLinker(linck)) {
                        context.mQLinkMicServiceListeners.forEach {
                            it.onLinkerJoin(linck)
                        }
                    }
                }
            }
        }
    }

    override fun addLinkMicListener(listener: QAudienceMicHandler.QLinkMicListener) {
        mQLinkMicListeners.add(listener)
    }

    override fun removeLinkMicListener(listener: QAudienceMicHandler.QLinkMicListener) {
        mQLinkMicListeners.remove(listener)
    }

    private val mAudienceExtQNClientEventListener = object : DefaultExtQNClientEventListener {
        override fun onConnectionStateChanged(
            p0: QNConnectionState,
            p1: QNConnectionDisconnectedInfo?
        ) {
            mQLinkMicListeners.forEach {
                it.onConnectionStateChanged(p0.toQConnectionState())
            }
            if (p0 == QNConnectionState.DISCONNECTED) {
                if (mMeLinker != null) {
                    stopInner(
                        true, null
                    )
                }
            }
        }
    }

    override fun attachRoomClient(client: QLiveClient) {
        super.attachRoomClient(client)
        mPlayer = playerGetter
        context.mQRtcLiveRoom = QRtcLiveRoom(AppCache.appContext)
        context.mQRtcLiveRoom.addExtraQNRTCEngineEventListener(context.mExtQNClientEventListener)
        context.mQRtcLiveRoom.addExtraQNRTCEngineEventListener(mAudienceExtQNClientEventListener)
    }

    override fun onDestroyed() {
        mQLinkMicListeners.clear()
        super.onDestroyed()
        context.mQRtcLiveRoom.close()
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        mMicListJob.start()
    }

    override fun startLink(
        extension: HashMap<String, String>?, cameraParams: QCameraParam?,
        microphoneParams: QMicrophoneParam?, callBack: QLiveCallBack<Void>?
    ) {
        mMicListJob.cancel()
        if (currentRoomInfo == null) {
            callBack?.onError(-1, "roomInfo==null")
            mMicListJob.start(true)
            return
        }
        val linker = QMicLinker()
        linker.extension = extension
        linker.isOpenCamera = cameraParams != null
        linker.isOpenMicrophone = microphoneParams != null
        linker.userRoomID = currentRoomInfo?.liveID ?: ""
        backGround {
            doWork {
                val token = mLinkDateSource.upMic(linker)
                linker.user = user
                RtmManager.rtmClient.sendChannelMsg(
                    RtmTextMsg<QMicLinker>(
                        liveroom_miclinker_join,
                        linker
                    ).toJsonString(),
                    currentRoomInfo!!.chatID, false
                )
                cameraParams?.let {
                    context.mQRtcLiveRoom.enableCamera(it)
                }
                microphoneParams?.let {
                    context.mQRtcLiveRoom.enableMicrophone(it)
                }
                context.mQRtcLiveRoom.joinRtc(token.rtc_token, JsonUtils.toJson(linker))
//                val users = ArrayList<QNMicLinker>()
//                context.mRtcLiveRoom.mClient.remoteUsers.forEach {
//                    if (it.userID != roomInfo?.anchor?.userId) {
//                        val linck = JsonUtils.parseObject(it.userData, QNMicLinker::class.java)
//                    }
//                }
                context.mQRtcLiveRoom.publishLocal()
                mQLinkMicListeners.forEach {
                    it.onRoleChange(true)
                }
                context.mExtQNClientEventListener.onUserJoined(
                    user?.userId ?: "",
                    JsonUtils.toJson(linker)
                )
                mPlayer?.onLinkStatusChange(true)
                callBack?.onSuccess(null)
            }
            catchError {
                mMicListJob.start(true)
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun isLinked(): Boolean {
        return mMeLinker != null
    }

    override fun stopLink(callBack: QLiveCallBack<Void>?) {
        stopInner(false, callBack)
    }

    fun stopInner(force: Boolean, callBack: QLiveCallBack<Void>?, sendMsg: Boolean = true) {
        if (mMeLinker == null) {
            callBack?.onError(-1, "user is not on mic")
            return
        }
        backGround {
            doWork {
                Log.d("QNAudience", "下麦 ")
                try {
                    mLinkDateSource.downMic(mMeLinker!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val mode = UidMode().apply {
                    uid = user?.userId
                }
                if (sendMsg) {
                    try {
                        RtmManager.rtmClient.sendChannelMsg(
                            RtmTextMsg<UidMode>(
                                liveroom_miclinker_left,
                                mode
                            ).toJsonString(),
                            currentRoomInfo!!.chatID, false
                        )
                    } catch (e: RtmException) {
                        e.printStackTrace()
                    }
                }
                context.mQRtcLiveRoom.leave()
                if (sendMsg) {
                    context.mExtQNClientEventListener.onUserLeft(
                        user?.userId ?: ""
                    )
                }
                Log.d("QNAudience", "下麦 1")
                context.removeLinker(user!!.userId)
                mQLinkMicListeners.forEach {
                    it.onRoleChange(false)
                }
                mPlayer?.onLinkStatusChange(false)
                mMicListJob.start(true)
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun onLeft() {
        super.onLeft()
        mMicListJob.cancel()
        if (mMeLinker != null) {
            stopInner(true, null)
        }
    }

    override fun switchCamera(callBack: QLiveCallBack<QCameraFace>?) {
        if (mMeLinker == null) {
            callBack?.onError(-1, "not in seat")
            return
        }
        context.mQRtcLiveRoom.switchCamera()
    }

    override fun muteCamera(muted: Boolean, callBack: QLiveCallBack<Boolean>?) {

        if (mMeLinker == null) {
            return
        }
        val mode = MuteMode().apply {
            uid = user?.userId
            mute = muted
        }
        backGround {
            doWork {
                if (context.mQRtcLiveRoom.muteLocalCamera(muted)) {
                    mLinkDateSource.switch(
                        mMeLinker!!, false, !muted
                    )
                    RtmManager.rtmClient.sendChannelMsg(
                        RtmTextMsg<MuteMode>(
                            liveroom_miclinker_camera_mute,
                            mode
                        ).toJsonString(),
                        currentRoomInfo!!.chatID, true
                    )
                    mMeLinker?.isOpenCamera = !muted
                    callBack?.onSuccess(true)
                } else {
                    callBack?.onSuccess(false)
                }
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun muteMicrophone(muted: Boolean, callBack: QLiveCallBack<Boolean>?) {

        if (mMeLinker == null) {
            return
        }
        val mode = MuteMode().apply {
            uid = user?.userId
            mute = muted
        }
        backGround {
            doWork {
                if (context.mQRtcLiveRoom.muteLocalMicrophone(muted)) {
                    mLinkDateSource.switch(
                        mMeLinker!!, true, !muted
                    )
                    RtmManager.rtmClient.sendChannelMsg(
                        RtmTextMsg<MuteMode>(
                            liveroom_miclinker_microphone_mute,
                            mode
                        ).toJsonString(),
                        currentRoomInfo!!.chatID, true
                    )
                    mMeLinker?.isOpenMicrophone = !muted
                    callBack?.onSuccess(true)
                } else {
                    callBack?.onSuccess(false)
                }
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun setVideoFrameListener(frameListener: QVideoFrameListener?) {
        context.mQRtcLiveRoom.setVideoFrameListener(QVideoFrameListenerWrap(frameListener))
    }

    override fun setAudioFrameListener(frameListener: QAudioFrameListener?) {
        context.mQRtcLiveRoom.setAudioFrameListener(QAudioFrameListenerWrap(frameListener))
    }

    private val playerGetter by lazy {
        (client as QPlayerProvider).playerGetter.invoke()
    }
}