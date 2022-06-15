package com.qncube.linkmicservice

import android.util.Log
import com.niucube.rtm.RtmException
import com.niucube.rtm.RtmManager
import com.niucube.rtm.msg.RtmTextMsg
import com.niucube.rtm.sendChannelMsg
import com.nucube.rtclive.*
import com.qiniu.droid.rtc.*
import com.qiniu.jsonutil.JsonUtils
import com.qncube.lcommon.*
import com.qncube.linkmicservice.QLinkMicServiceImpl.Companion.liveroom_miclinker_camera_mute
import com.qncube.linkmicservice.QLinkMicServiceImpl.Companion.liveroom_miclinker_join
import com.qncube.linkmicservice.QLinkMicServiceImpl.Companion.liveroom_miclinker_left
import com.qncube.linkmicservice.QLinkMicServiceImpl.Companion.liveroom_miclinker_microphone_mute
import com.qncube.linveroominner.*
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.service.BaseService
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QMicLinker
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class QAudienceMicHandlerImpl(val context: MicLinkContext) : QAudienceMicHandler, BaseService() {

    private val mLinkDateSource = LinkDateSource()
    private var mPlayer: IPlayer? = null
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

    private val mMicListJob = com.qncube.linveroominner.Scheduler(5000) {
        if (currentRoomInfo == null) {
            return@Scheduler
        }
        if (isLinked()) {
            return@Scheduler
        }
        backGround {
            doWork {
                val list = mLinkDateSource.getMicList(currentRoomInfo?.liveId ?: "")
                if (compare(context.allLinker, list)) {
                    return@doWork
                }
                val toRemve = LinkedList<QMicLinker>()
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
                context.allLinker.removeAll(toRemve)
                list.forEach {
                    context.addLinker(it)
                }
                context.mQLinkMicServiceListeners.forEach {
                    it.onInitLinkers(context.allLinker)
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
        val field = client.getPlayer()
        mPlayer = field.get(client) as IPlayer
        context.mRtcLiveRoom = RtcLiveRoom(AppCache.appContext)
        context.mRtcLiveRoom.addExtraQNRTCEngineEventListener(context.mExtQNClientEventListener)
        context.mRtcLiveRoom.addExtraQNRTCEngineEventListener(mAudienceExtQNClientEventListener)
    }

    override fun onDestroyed() {
        super.onDestroyed()
        context.mRtcLiveRoom.close()
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        mMicListJob.start()
    }

    override fun startLink(
        extensions: HashMap<String, String>?, cameraParams: QCameraParam?,
        microphoneParams: QMicrophoneParam?, callBack: QLiveCallBack<Void>?
    ) {
        mMicListJob.cancel()
        if (currentRoomInfo == null) {
            callBack?.onError(-1, "roomInfo==null")
            mMicListJob.start(true)
            return
        }
        val linker = QMicLinker()
        linker.extension = extensions
        linker.isOpenCamera = cameraParams != null
        linker.isOpenMicrophone = microphoneParams != null
        linker.userRoomID = currentRoomInfo?.liveId ?: ""
        backGround {
            doWork {
                val token = mLinkDateSource.upMic(linker)
                linker.user = user
                RtmManager.rtmClient.sendChannelMsg(
                    RtmTextMsg<QMicLinker>(
                        liveroom_miclinker_join,
                        linker
                    ).toJsonString(),
                    currentRoomInfo!!.chatId, false
                )
                cameraParams?.let {
                    context.mRtcLiveRoom.enableCamera(it)
                }
                microphoneParams?.let {
                    context.mRtcLiveRoom.enableMicrophone(it)
                }
                context.mRtcLiveRoom.joinRtc(token.rtc_token, JsonUtils.toJson(linker))
//                val users = ArrayList<QNMicLinker>()
//                context.mRtcLiveRoom.mClient.remoteUsers.forEach {
//                    if (it.userID != roomInfo?.anchor?.userId) {
//                        val linck = JsonUtils.parseObject(it.userData, QNMicLinker::class.java)
//                    }
//                }
                context.mRtcLiveRoom.publishLocal()
                mQLinkMicListeners.forEach {
                    it.onRoleChange(true)
                }
                context.mExtQNClientEventListener.onUserJoined(
                    user?.userId ?: "",
                    JsonUtils.toJson(linker)
                )
                mPlayer?.pause()
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
                mLinkDateSource.downMic(mMeLinker!!)
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
                            currentRoomInfo!!.chatId, false
                        )
                    } catch (e: RtmException) {
                        e.printStackTrace()
                    }
                }
                context.mRtcLiveRoom.leave()
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
                mPlayer?.resume()
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
            callBack?.onError(-1,"not in seat")
            return
        }
        context.mRtcLiveRoom.switchCamera()
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
                if (context.mRtcLiveRoom.muteLocalCamera(muted)) {
                    mLinkDateSource.switch(
                        mMeLinker!!, false, !muted
                    )
                    RtmManager.rtmClient.sendChannelMsg(
                        RtmTextMsg<MuteMode>(
                            liveroom_miclinker_camera_mute,
                            mode
                        ).toJsonString(),
                        currentRoomInfo!!.chatId, true
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
                if (context.mRtcLiveRoom.muteLocalMicrophone(muted)) {
                    mLinkDateSource.switch(
                        mMeLinker!!, true, !muted
                    )
                    RtmManager.rtmClient.sendChannelMsg(
                        RtmTextMsg<MuteMode>(
                            liveroom_miclinker_microphone_mute,
                            mode
                        ).toJsonString(),
                        currentRoomInfo!!.chatId, true
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
        context.mRtcLiveRoom.setVideoFrameListener(QVideoFrameListenerWrap(frameListener))
    }

    override fun setAudioFrameListener(frameListener: QAudioFrameListener?) {
        context.mRtcLiveRoom.setAudioFrameListener(QAudioFrameListenerWrap(frameListener))
    }

}