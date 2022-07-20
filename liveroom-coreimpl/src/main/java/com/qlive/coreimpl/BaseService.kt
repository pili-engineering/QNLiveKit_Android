package com.qlive.coreimpl

import com.qlive.core.QClientLifeCycleListener
import com.qlive.core.QLiveClient
import com.qlive.core.QLiveService
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser

open class BaseService : QLiveService,
    QClientLifeCycleListener {

    protected var user: QLiveUser? = null
    protected var currentRoomInfo: QLiveRoomInfo? = null
    protected var client: QLiveClient? = null

    /**
     * 进入回调
     *
     * @param user
     */
    override fun onEntering(liveId: String, user: QLiveUser) {
        this.user = user
    }

    /**
     * 加入回调
     *
     * @param roomInfo
     */
    override fun onJoined(roomInfo: QLiveRoomInfo) {
        this.currentRoomInfo = roomInfo
    }

    /**
     * 离开回调
     */
    override fun onLeft() {
        user = null
    }

    /**
     * 销毁回调
     */
    override fun onDestroyed() {
    }

    open fun attachRoomClient(client: QLiveClient) {
        this.client = client
    }

    open suspend fun checkLeave() {

    }
}