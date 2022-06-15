package com.qbcube.pkservice

import com.qncube.linveroominner.PKDateSource
import com.qncube.linveroominner.PKInfo
import com.qncube.linveroominner.backGround
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.been.QExtension
import com.qncube.liveroomcore.service.BaseService
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser
import java.util.*

class AudiencePKSynchro() : BaseService() {

    private val mPKDateSource = PKDateSource()
    private val mUserSource = com.qncube.linveroominner.UserDataSource()
    var mListenersCall: (() -> LinkedList<QPKServiceListener>)? = null
    var mPKSession: QPKSession? = null
        private set

    private val repeatSynchroJob = com.qncube.linveroominner.Scheduler(6000) {
        if (currentRoomInfo == null) {
            return@Scheduler
        }
        backGround {
            doWork {
                if (currentRoomInfo?.pkId?.isEmpty() == false) {
                    //当前房间在PK
                    val info = mPKDateSource.getPkInfo(currentRoomInfo?.liveId ?: "")
                    if (info.status == PKStatus.RelaySessionStatusStopped.intValue && mPKSession != null) {
                        currentRoomInfo?.pkId = ""
                        mListenersCall?.invoke()?.forEach {
                            it.onStop(mPKSession!!, -1, "time out")
                        }
                        mPKSession = null
                    }
                } else {
                    val reFreshRoom = com.qncube.linveroominner.RoomDataSource()
                        .refreshRoomInfo(currentRoomInfo!!.liveId)
                    if (!reFreshRoom.pkId.isEmpty() && mPKSession == null) {

                        val info = mPKDateSource.getPkInfo(reFreshRoom.liveId ?: "")
                        if (info.status == PKStatus.RelaySessionStatusSuccess.intValue) {

                            val recever = mUserSource.searchUserByUserId(info.recvUserId)
                            val inver = mUserSource.searchUserByUserId(info.initUserId)
                            val pk = fromPkInfo(info, inver, recever)
                            mPKSession = pk
                            currentRoomInfo?.pkId = reFreshRoom.pkId
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
            currentRoomInfo?.pkId = ""
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
        if (!roomInfo.pkId.isEmpty()) {
            backGround {
                doWork {
                    val info = mPKDateSource.getPkInfo(roomInfo.pkId ?: "")
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