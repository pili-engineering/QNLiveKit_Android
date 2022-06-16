package com.qlive.pkservice

import com.qlive.coreimpl.PKDateSource
import com.qlive.coreimpl.PKInfo
import com.qlive.coreimpl.backGround
import com.qlive.core.*
import com.qlive.core.been.QExtension
import com.qlive.coreimpl.BaseService
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import java.util.*

class AudiencePKSynchro() : BaseService() {

    private val mPKDateSource = PKDateSource()
    private val mUserSource = com.qlive.coreimpl.UserDataSource()
    var mListenersCall: (() -> LinkedList<QPKServiceListener>)? = null
    var mPKSession: QPKSession? = null
        private set

    private val repeatSynchroJob = com.qlive.coreimpl.Scheduler(6000) {
        if (currentRoomInfo == null) {
            return@Scheduler
        }
        backGround {
            doWork {
                if (currentRoomInfo?.pkID?.isEmpty() == false) {
                    //当前房间在PK
                    val info = mPKDateSource.getPkInfo(currentRoomInfo?.liveID ?: "")
                    if (info.status == PKStatus.RelaySessionStatusStopped.intValue && mPKSession != null) {
                        currentRoomInfo?.pkID = ""
                        mListenersCall?.invoke()?.forEach {
                            it.onStop(mPKSession!!, -1, "time out")
                        }
                        mPKSession = null
                    }
                } else {
                    val reFreshRoom = com.qlive.coreimpl.RoomDataSource()
                        .refreshRoomInfo(currentRoomInfo!!.liveID)
                    if (!reFreshRoom.pkID.isEmpty() && mPKSession == null) {

                        val info = mPKDateSource.getPkInfo(reFreshRoom.liveID ?: "")
                        if (info.status == PKStatus.RelaySessionStatusSuccess.intValue) {

                            val recever = mUserSource.searchUserByUserId(info.recvUserId)
                            val inver = mUserSource.searchUserByUserId(info.initUserId)
                            val pk = fromPkInfo(info, inver, recever)
                            mPKSession = pk
                            currentRoomInfo?.pkID = reFreshRoom.pkID
                            mListenersCall?.invoke()?.forEach {
                                it.onStart(mPKSession!!)
                            }
                        }
                    }
                }
            }
            catchError {
            }
        }
    }

    private val mQPKServiceListener = object :
        QPKServiceListener {

        override fun onStart(pkSession: QPKSession) {
            mPKSession = pkSession
            // repeatSynchroJob.start()
        }
        override fun onStop(pkSession: QPKSession, code: Int, msg: String) {
            mPKSession = null
            // repeatSynchroJob.cancel()
            currentRoomInfo?.pkID = ""
        }
        override fun onStartTimeOut(pkSession: QPKSession) {
        }
        override fun onPKExtensionUpdate(pkSession: QPKSession, extension: QExtension) {
        }
    }

    override fun attachRoomClient(client: QLiveClient) {
        super.attachRoomClient(client)
        mListenersCall?.invoke()?.add(mQPKServiceListener)
    }

    /**
     * 进入回
     * @param user
     */
    override fun onEntering(liveId: String, user: QLiveUser) {
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        if (!roomInfo.pkID.isEmpty()) {
            backGround {
                doWork {
                    val info = mPKDateSource.getPkInfo(roomInfo.pkID ?: "")
                    if (info.status == PKStatus.RelaySessionStatusSuccess.intValue) {
                        val recever = mUserSource.searchUserByUserId(info.recvUserId)
                        val inver = mUserSource.searchUserByUserId(info.initUserId)
                        val pk = fromPkInfo(info, inver, recever)
                        mPKSession = pk
                        mListenersCall?.invoke()?.forEach {
                            it.onStart(pk)
                        }
                    }
                }
                catchError {

                }
                onFinally {
                    repeatSynchroJob.start(true)
                }
            }
        } else {
            repeatSynchroJob.start(true)
        }
    }

    override fun onLeft() {
        super.onLeft()
        repeatSynchroJob.cancel()
    }

    override fun onDestroyed() {
        repeatSynchroJob.cancel()
        super.onDestroyed()
    }

    private fun fromPkInfo(info: PKInfo, inver: QLiveUser, recver: QLiveUser): QPKSession {
        return QPKSession().apply {
            //PK场次ID
            sessionID = info.id
            //发起方
            initiator = inver
            //接受方
            receiver = recver
            //发起方所在房间
            initiatorRoomID = info.initRoomId
            //接受方所在房间
            receiverRoomID = info.recvRoomId
            //扩展字段
            extension = info.extensions
            //pk 状态 0邀请过程  1pk中 2结束 其他自定义状态比如惩罚时间
            status = info.status
            //pk开始时间戳
            startTimeStamp = info.createdAt
        }
    }
}