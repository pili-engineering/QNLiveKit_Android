package com.qncube.liveroomcore

import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.liveroomcore.mode.QNLiveUser

open class BaseService : QNLiveService {

    protected var user: QNLiveUser? = null
    protected var roomInfo: QLiveRoomInfo? = null
    protected var client: QNLiveClient? = null

    /**
     * 进入回调
     *
     * @param user
     */
    override fun onEntering(liveId: String, user: QNLiveUser) {
        this.user = user
    }

    /**
     * 加入回调
     *
     * @param roomInfo
     */
    override fun onJoined(roomInfo: QLiveRoomInfo) {
        this.roomInfo = roomInfo
    }

    /**
     * 离开回调
     */
    open override fun onLeft() {
        user = null
    }

    /**
     * 销毁回调
     */
    open override fun onDestroyed() {

    }

    open override fun attachRoomClient(client: QNLiveClient) {
        this.client = client
    }
}