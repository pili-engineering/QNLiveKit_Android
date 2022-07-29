package com.qlive.coreimpl

import com.qlive.core.QClientLifeCycleListener
import com.qlive.core.*
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.coreimpl.datesource.RoomDataSource
import com.qlive.coreimpl.http.NetBzException
import com.qlive.coreimpl.model.HearBeatResp
import com.qlive.coreimpl.util.backGround
import com.qlive.liblog.QLiveLogUtil

open class RoomScheduler : QClientLifeCycleListener {

    protected var user: QLiveUser? = null
    protected var roomInfo: QLiveRoomInfo? = null
    var client: QLiveClient? = null
    private var roomStatus = 0
    private var anchorStatus = 1
    var roomStatusChange: (status: QLiveStatus) -> Unit = {}
    private val roomDataSource = RoomDataSource()

    fun setAnchorStatus(newStatus: Int) {
        anchorStatus = newStatus
        roomStatusChange.invoke(anchorStatus.anchorStatusToLiveStatus())
    }

    private val mHeartBeatJob = Scheduler(8000) {
        if (roomInfo == null) {
            return@Scheduler
        }
        backGround {
            doWork {
                var hearRet: HearBeatResp? = null
                try {
                    hearRet = roomDataSource.heartbeat(roomInfo?.liveID ?: "")
                } catch (e: NetBzException) {
                    if (e.code == 500) {
                        QLiveLogUtil.d("res.liveStatus 心跳超时 ")
                        //心跳超时从新进入房间
                        if (client!!.clientType == QClientType.PUSHER) {
                            // roomDataSource.pubRoom(roomInfo!!.liveID)
//                            roomStatus = QLiveStatus.ANCHOR_OFFLINE
//                            roomStatusChange.invoke(roomStatus.roomStatusToLiveStatus())
                        } else {
                            roomDataSource.joinRoom(roomInfo!!.liveID)
                        }
                    }
                }
                val room = roomDataSource.refreshRoomInfo(roomInfo?.liveID ?: "")
                if (anchorStatus != room.anchorStatus) {
                    anchorStatus = room.anchorStatus
                    roomStatusChange.invoke(anchorStatus.anchorStatusToLiveStatus())
                }
                hearRet ?: HearBeatResp().apply {
                    liveId = roomInfo?.liveID
                    liveStatus = 3
                }
                if (hearRet!!.liveStatus != roomStatus) {
                    roomStatus = hearRet.liveStatus
                    roomStatusChange.invoke(roomStatus.roomStatusToLiveStatus())
                }
                QLiveLogUtil.d("res.liveStatus ${hearRet.liveStatus}   room.anchorStatus ${room.anchorStatus} ")
            }
            catchError {
            }
        }
    }

    override fun onEntering(liveId: String, user: QLiveUser) {
        this.user = user
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        this.roomInfo = roomInfo
        roomStatus = roomInfo.liveStatus
        anchorStatus = roomInfo.anchorStatus
        mHeartBeatJob.start()
    }

    open override fun onLeft() {
        user = null
        mHeartBeatJob.cancel()
    }

    open override fun onDestroyed() {
        mHeartBeatJob.cancel()
    }
}