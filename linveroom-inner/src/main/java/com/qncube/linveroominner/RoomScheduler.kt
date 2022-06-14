package com.qncube.linveroominner

import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser

open class RoomScheduler : QClientLifeCycleListener {

    protected var user: QLiveUser? = null
    protected var roomInfo: QLiveRoomInfo? = null
    protected var client: QLiveClient? = null
    private var roomStatus = 0
    private var anchorStatus = 1
    var roomStatusChange: (status: QLiveStatus) -> Unit = {}
    private val roomDataSource = com.qncube.linveroominner.RoomDataSource()
    private val mHeartBeatJob = Scheduler(8000) {
        if (roomInfo == null) {
            return@Scheduler
        }
        backGround {
            doWork {
                val res = roomDataSource.heartbeat(roomInfo?.liveId ?: "")
                val room = roomDataSource.refreshRoomInfo(roomInfo?.liveId ?: "")

                if (res.liveStatus != roomStatus) {
                    roomStatus = res.liveStatus
                    roomStatusChange.invoke(roomStatus.roomStatusToLiveStatus())
                }
                if (anchorStatus != room.anchorStatus) {
                    anchorStatus = room.anchorStatus
                    roomStatusChange.invoke(anchorStatus.anchorStatusToLiveStatus())
                }
                QNLiveLogUtil.LogE("res.liveStatus ${res.liveStatus}   room.anchorStatus ${room.anchorStatus} ")
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