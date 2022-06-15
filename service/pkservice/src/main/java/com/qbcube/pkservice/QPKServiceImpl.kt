package com.qbcube.pkservice

import android.text.TextUtils
import android.util.Base64
import com.niucube.rtm.*
import com.niucube.rtm.msg.RtmTextMsg
import com.nucube.rtclive.CameraMergeOption
import com.nucube.rtclive.DefaultExtQNClientEventListener
import com.nucube.rtclive.MicrophoneMergeOption
import com.nucube.rtclive.RtcLiveRoom
import com.qiniu.droid.rtc.*
import com.qiniu.jsonutil.JsonUtils
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.QNLiveLogUtil
import com.qncube.liveroomcore.QClientType
import com.qncube.liveroomcore.service.BaseService
import com.qncube.lcommon.RtcException
import com.qncube.linveroominner.*
import com.qncube.liveroomcore.been.QExtension
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser
import kotlinx.coroutines.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class QPKServiceImpl : QPKService, BaseService() {

    companion object {
        val liveroom_pk_start = "liveroom_pk_start"
        val liveroom_pk_stop = "liveroom_pk_stop"
        val PK_STATUS_OK = 1
    }

    private val mPKDateSource = PKDateSource()
    private val pkPKInvitationHandlerImpl = QInvitationHandlerImpl("liveroom_pk_invitation")
    private val QPKServiceListeners = LinkedList<QPKServiceListener>()
    private var mQPKMixStreamAdapter: QPKMixStreamAdapter? = null
    private val mAudiencePKSynchro = AudiencePKSynchro().apply {
        mListenersCall = {
            QPKServiceListeners
        }
    }
    var mPKSession: QPKSession? = null
        private set

    private var mPKSessionTemp: QPKSession? = null
    private var trackUidTemp = ""
    private fun checkReceivePk(pkTemp: QPKSession?, uidTem: String) {
        if (pkTemp != null) {
            mPKSessionTemp = pkTemp
        }
        if (!TextUtils.isEmpty(uidTem)) {
            trackUidTemp = uidTem
        }
        if (mPKSessionTemp != null && !TextUtils.isEmpty(trackUidTemp) &&
            mPKSessionTemp?.initiator?.userId == trackUidTemp
        ) {
            //开始收到pk
            backGround {
                doWork {
                    val pkOutline = mPKDateSource.recevPk(mPKSessionTemp?.sessionID ?: "")

                    val field = client!!.getRtc()
                    val room: RtcLiveRoom = field.get(client) as RtcLiveRoom
                    //转发
                    val sourceInfo = QNMediaRelayInfo(room.roomName, room.roomToken)
                    val configuration = QNMediaRelayConfiguration(sourceInfo)

                    val tokens: Array<String> =
                        pkOutline.relay_token.split(":".toRegex()).toTypedArray()
                    val b64 = String(Base64.decode(tokens[2].toByteArray(), Base64.DEFAULT))
                    val json: JSONObject = JSONObject(b64)
                    val mAppId = json.optString("appId")
                    val peerRoomName = json.optString("roomName")

                    mPKDateSource.ackACKPk(mPKSessionTemp?.sessionID ?: "")
                    val destInfo1 = QNMediaRelayInfo(peerRoomName, pkOutline.relay_token)
                    configuration.addDestRoomInfo(destInfo1)
                    startMediaRelay(peerRoomName, room.mClient, configuration)

                    //pk 接收方收到 邀请方
                    mPKSession = mPKSessionTemp
                    mPKSession?.status = PK_STATUS_OK

                    try {
                        //群信号
                        RtmManager.rtmClient.sendChannelMsg(
                            RtmTextMsg<QPKSession>(
                                liveroom_pk_start,
                                mPKSession
                            ).toJsonString(), currentRoomInfo!!.chatId, false
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    QPKServiceListeners.forEach {
                        it.onStart(mPKSession!!)
                    }
                    //混流
                    resetMixStream(mPKSession?.initiator?.userId ?: "")
                    QNLiveLogUtil.LogE("pk 接收方确认回复pk成功 ")
                }
                catchError {
                    it.message?.asToast()
                    QNLiveLogUtil.LogE("pk 接收方确认回复pk 错误 ${it.getCode()} ${it.message}")
                }
                onFinally {
                    mPKSessionTemp = null
                    trackUidTemp = ""
                }
            }
        }
    }

    private val mC2cListener = object : RtmMsgListener {
        override fun onNewMsg(msg: String, fromID: String, toID: String): Boolean {
            if (msg.optAction() == liveroom_pk_start) {
                QNLiveLogUtil.LogE("pk 接收方收到pk holle ")
                val pk =
                    JsonUtils.parseObject(msg.optData(), QPKSession::class.java) ?: return true
                checkReceivePk(pk, "")
            }

            if (msg.optAction() == liveroom_pk_stop) {
                val pk =
                    JsonUtils.parseObject(msg.optData(), QPKSession::class.java) ?: return true
                if (mPKSession?.sessionID == pk.sessionID) {
                    loopStop()
                }
            }

            return false
        }
    }

    private val groupListener = object : RtmMsgListener {
        override fun onNewMsg(msg: String, fromID: String, toID: String): Boolean {
            when (msg.optAction()) {
                liveroom_pk_start -> {
                    val pk =
                        JsonUtils.parseObject(msg.optData(), QPKSession::class.java) ?: return true
                    QPKServiceListeners.forEach {
                        it.onStart(pk)
                    }
                }
                liveroom_pk_stop -> {
                    val pk =
                        JsonUtils.parseObject(msg.optData(), QPKSession::class.java) ?: return true
                    QPKServiceListeners.forEach {
                        it.onStop(pk, 1, "")
                    }

                }
            }
            return false
        }
    }

    private var timeoutJob: Job? = null
    private fun startTimeOutJob(timeoutTimestamp: Long) {
        timeoutJob = GlobalScope.launch(Dispatchers.Main) {
            try {

                delay(timeoutTimestamp)
                QNLiveLogUtil.LogE("pk 邀请方等待超时 ")
                if (mPKSession == null) {
                    return@launch
                }
                QPKServiceListeners.forEach {
                    it.onStartTimeOut(mPKSession!!)
                }
                try {

                    mPKDateSource.stopPk(mPKSession?.sessionID ?: "")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                mPKSession = null
                stopMediaRelay()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val defaultExtQNClientEventListener = object : DefaultExtQNClientEventListener {
        override fun onUserJoined(p0: String, p1: String?) {
            super.onUserJoined(p0, p1)
            if (p0 == mPKSession?.receiver?.userId) {
                QNLiveLogUtil.LogE("pk 邀请方收到对方流 ")
                // 邀请放 收到 接收放确认了
                backGround {
                    doWork {
                        timeoutJob?.cancel()
                        mPKSession?.status = PK_STATUS_OK

                        try {
                            //群信号
                            RtmManager.rtmClient.sendChannelMsg(
                                RtmTextMsg<QPKSession>(
                                    liveroom_pk_start,
                                    mPKSession
                                ).toJsonString(), currentRoomInfo!!.chatId, false
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                        QPKServiceListeners.forEach {
                            it.onStart(mPKSession!!)
                        }
                        //混流
                        resetMixStream(mPKSession?.receiver?.userId ?: "")

                    }
                    catchError {}
                }
            } else {
                QNLiveLogUtil.LogE("pk 接收方收到对方流 ")
                checkReceivePk(null, p0)
            }
        }

        override fun onUserLeft(p0: String) {
            super.onUserLeft(p0)
            if (mPKSession == null) {
                return
            }
            if (p0 == mPKSession?.receiver?.userId) {
                QNLiveLogUtil.LogE("pk 对方离开房间 ")
                loopStop()
            }

            if (p0 == mPKSession?.initiator?.userId) {
                QNLiveLogUtil.LogE("pk 对方离开房间 ")
                loopStop()
            }
        }
    }

    private fun loopStop() {

        backGround {
            doWork {

                try {
                    mPKDateSource.stopPk(mPKSession?.sessionID ?: "")
                } catch (e: Exception) {
                    e.printStackTrace()
                    QNLiveLogUtil.LogE("pk 对方离开房间 上报结束失败 ${e.message} ")
                }

                stopMediaRelay()
                try {
                    RtmManager.rtmClient.sendChannelMsg(
                        RtmTextMsg<QPKSession>(
                            liveroom_pk_stop,
                            mPKSession
                        ).toJsonString(), currentRoomInfo!!.chatId, false
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    QNLiveLogUtil.LogE("pk 结束信令发送失败 ")
                }
                if (mPKSession == null) {
                    return@doWork
                }
                QPKServiceListeners.forEach {
                    it.onStop(mPKSession!!, 1, "peer stop")
                }
                val peer = if (mPKSession!!.initiator.userId == user?.userId) {
                    mPKSession!!.receiver.userId
                } else {
                    mPKSession!!.initiator.userId
                }
                mPKSession = null

                resetMixStream(peer)
            }
        }
    }

    private fun resetMixStream(peerId: String) {
        if (mQPKMixStreamAdapter == null) {
            return
        }
        val field = client!!.getRtc()
        val mRtcLiveRoom: RtcLiveRoom = field.get(client) as RtcLiveRoom

        if (mPKSession != null) {
            mRtcLiveRoom.mMixStreamManager.startNewMixStreamJob(
                mQPKMixStreamAdapter!!.onPKMixStreamStart(
                    mPKSession!!
                )
            )
            val ops = mQPKMixStreamAdapter?.onPKLinkerJoin(mPKSession!!)
            ops?.forEach {
                mRtcLiveRoom.mMixStreamManager.updateUserAudioMergeOptions(
                    it.uid,
                    it.microphoneMergeOption,
                    false
                )
                mRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
                    it.uid,
                    it.cameraMergeOption,
                    false
                )
            }
            mRtcLiveRoom.mMixStreamManager.commitOpt()
        } else {

            if (mRtcLiveRoom.mMixStreamManager
                    .roomUser == 1
            ) {
                mRtcLiveRoom.mMixStreamManager.startForwardJob()
                return
            } else {
                mRtcLiveRoom.mMixStreamManager.stopMixStreamJob()
                mRtcLiveRoom.mMixStreamManager.startMixStreamJob()
            }

            mRtcLiveRoom.mMixStreamManager.updateUserAudioMergeOptions(
                peerId,
                MicrophoneMergeOption(),
                false
            )
            mRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
                peerId,
                CameraMergeOption(),
                false
            )

            val ops = mQPKMixStreamAdapter?.onPKLinkerLeft()
            ops?.forEach {
                mRtcLiveRoom.mMixStreamManager.updateUserAudioMergeOptions(
                    it.uid,
                    it.microphoneMergeOption,
                    false
                )
                mRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
                    it.uid,
                    it.cameraMergeOption,
                    false
                )
            }
            mRtcLiveRoom.mMixStreamManager.commitOpt()
        }
    }

    override fun attachRoomClient(client: QLiveClient) {
        super.attachRoomClient(client)
        RtmManager.addRtmC2cListener(mC2cListener)
        RtmManager.addRtmChannelListener(groupListener)

        if (client.clientType == QClientType.PUSHER) {
            val field = client!!.getRtc()
            val room: RtcLiveRoom = field.get(client) as RtcLiveRoom
            room.addExtraQNRTCEngineEventListener(defaultExtQNClientEventListener)
            pkPKInvitationHandlerImpl.attach()
        } else {
            mAudiencePKSynchro.attachRoomClient(client)
        }
    }

    override fun onDestroyed() {
        super.onDestroyed()
        RtmManager.removeRtmC2cListener(mC2cListener)
        RtmManager.removeRtmChannelListener(groupListener)

        if (client?.clientType == QClientType.PUSHER) {
            pkPKInvitationHandlerImpl.onDestroyed()
        } else {
            mAudiencePKSynchro.onDestroyed()
        }
    }

    override fun onEntering(liveId: String, user: QLiveUser) {
        super.onEntering(liveId, user)
        if (client?.clientType == QClientType.PUSHER) {
            pkPKInvitationHandlerImpl.onEntering(liveId, user)
        } else {
            mAudiencePKSynchro.onEntering(liveId, user)
        }
    }

    override fun onLeft() {
        super.onLeft()
        if (client?.clientType == QClientType.PUSHER) {
            pkPKInvitationHandlerImpl.onLeft()
        } else {
            mAudiencePKSynchro.onLeft()
        }
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)

        if (client?.clientType == QClientType.PUSHER) {
            pkPKInvitationHandlerImpl.onJoined(roomInfo)
        } else {
            mAudiencePKSynchro.onJoined(roomInfo)
        }
    }

    /**
     * 设置混流适配器
     * @param adapter
     */
    override fun setPKMixStreamAdapter(adapter: QPKMixStreamAdapter?) {
        mQPKMixStreamAdapter = adapter
    }

    override fun addServiceListener(QPKServiceListener: QPKServiceListener) {
        QPKServiceListeners.add(QPKServiceListener)
    }

    override fun removeServiceListener(QPKServiceListener: QPKServiceListener) {
        QPKServiceListeners.remove(QPKServiceListener)
    }

    /**
     * 跟新扩展自定义字段
     *
     * @param extension
     */
    override fun updatePKExtension(extension: QExtension, callBack: QLiveCallBack<Void>?) {
    }

    override fun start(
        timeoutTimestamp: Long,
        receiverRoomID: String,
        receiverUID: String,
        extensions: HashMap<String, String>?,
        callBack: QLiveCallBack<QPKSession>?
    ) {
        if (currentRoomInfo == null) {
            callBack?.onError(0, " roomInfo==null")
            return
        }
        backGround {
            doWork {
                val pkOutline =
                    mPKDateSource.startPk(
                        currentRoomInfo?.liveId ?: "",
                        receiverRoomID,
                        receiverUID
                    )
                val receiver =
                    com.qncube.linveroominner.UserDataSource().searchUserByUserId(receiverUID)

                val pkSession = QPKSession()
                pkSession.extension = extensions
                pkSession.initiator = user
                pkSession.initiatorRoomID = currentRoomInfo?.liveId
                pkSession.receiver = receiver
                pkSession.receiverRoomID = receiverRoomID
                pkSession.sessionID = pkOutline.relay_id
                pkSession.status = pkOutline.relay_status

                mPKSession = pkSession

                //发c2c消息
                RtmManager.rtmClient.sendC2cMsg(
                    RtmTextMsg<QPKSession>(
                        liveroom_pk_start,
                        mPKSession
                    ).toJsonString(), receiver.imUid, false
                )
                val field = client!!.getRtc()
                val room: RtcLiveRoom = field.get(client) as RtcLiveRoom
                //转发
                val sourceInfo = QNMediaRelayInfo(room.roomName, room.roomToken)
                val configuration = QNMediaRelayConfiguration(sourceInfo)


                val tokens: Array<String> =
                    pkOutline.relay_token.split(":".toRegex()).toTypedArray()
                val b64 = String(Base64.decode(tokens[2].toByteArray(), Base64.DEFAULT))
                val json: JSONObject = JSONObject(b64)
                val mAppId = json.optString("appId")
                val peerRoomName = json.optString("roomName")

                val destInfo1 = QNMediaRelayInfo(peerRoomName, pkOutline.relay_token)
                configuration.addDestRoomInfo(destInfo1)

                startMediaRelay(peerRoomName, room.mClient, configuration)
                mPKDateSource.ackACKPk(mPKSession?.sessionID ?: "")
                startTimeOutJob(timeoutTimestamp)
                callBack?.onSuccess(pkSession)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    private suspend fun startMediaRelay(
        peerRoomName: String,
        client: QNRTCClient,
        configuration: QNMediaRelayConfiguration
    ) = suspendCoroutine<Unit> { continuation ->
        client.startMediaRelay(configuration, object : QNMediaRelayResultCallback {
            override fun onResult(p0: MutableMap<String, QNMediaRelayState>) {
                if (p0[peerRoomName] == QNMediaRelayState.SUCCESS) {
                    continuation.resume(Unit)
                } else {
                    continuation.resumeWithException(
                        RtcException(
                            -1,
                            "pk startMediaRelay" + p0[peerRoomName]?.name
                        )
                    )
                }
            }

            override fun onError(p0: Int, p1: String) {
                continuation.resumeWithException(RtcException(p0, p1))
            }
        })
    }

    private suspend fun stopMediaRelay() =
        suspendCoroutine<Unit> { continuation ->
            val field = client!!.getRtc()
            val room: RtcLiveRoom = field.get(client) as RtcLiveRoom
            room.mClient.stopMediaRelay(object : QNMediaRelayResultCallback {
                override fun onResult(p0: MutableMap<String, QNMediaRelayState>) {
                    continuation.resume(Unit)
                }

                override fun onError(p0: Int, p1: String) {
                    continuation.resumeWithException(RtcException(p0, p1))
                }
            }
            )
        }

    override fun stop(callBack: QLiveCallBack<Void>?) {
        if (currentRoomInfo == null || mPKSession?.status != PK_STATUS_OK) {
            callBack?.onError(0, " roomInfo==null")
            return
        }
        backGround {
            doWork {
                val peer = if (mPKSession!!.initiator.userId == user?.userId) {
                    mPKSession!!.receiver
                } else {
                    mPKSession!!.initiator
                }
                mPKDateSource.stopPk(mPKSession?.sessionID ?: "")
                stopMediaRelay()
                try {
                    RtmManager.rtmClient.sendChannelMsg(
                        RtmTextMsg<QPKSession>(
                            liveroom_pk_stop,
                            mPKSession
                        ).toJsonString(), currentRoomInfo!!.chatId, false
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    QNLiveLogUtil.LogE("pk 结束信令发送失败 ")
                }
                QPKServiceListeners.forEach {
                    it.onStop(mPKSession!!, 0, "positive stop")
                }

                mPKSession = null
                callBack?.onSuccess(null)
                resetMixStream(peer.userId)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    /**
     * 设置某人的连麦预览
     *
     * @param uid  麦上用户ID
     * @param view
     */
    override fun setPeerAnchorPreView(uid: String, view: QNRenderView) {
        val field = client!!.getRtc()
        val room: RtcLiveRoom = field.get(client) as RtcLiveRoom
        room.setUserCameraWindowView(uid, view)
    }

    /**
     * 获得pk邀请处理
     * @return
     */
    override fun getInvitationHandler(): QInvitationHandler {
        return pkPKInvitationHandlerImpl
    }

}