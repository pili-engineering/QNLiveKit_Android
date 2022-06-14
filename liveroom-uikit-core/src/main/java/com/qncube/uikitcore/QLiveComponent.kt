package com.qncube.uikitcore

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qncube.liveroomcore.QNLiveClient
import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.liveroomcore.mode.QNLiveUser
import com.qncube.liveroomcore.QClientLifeCycleListener

interface QLiveComponent : QComponent,
    QClientLifeCycleListener {

    var client: QNLiveClient?
    var roomInfo: QLiveRoomInfo?
    var user: QNLiveUser?

    open fun attachLiveClient( client: QNLiveClient) {
        this.client = client

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        this.roomInfo = roomInfo
    }

    override fun onEntering(roomId: String, user: QNLiveUser) {
        this.user = user
    }

    override fun onLeft() {

    }

    override fun onDestroyed() {
        client =null
    }

}