package com.qncube.uikitcore

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qncube.liveroomcore.QLiveClient
import com.qncube.liveroomcore.QClientLifeCycleListener
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser

interface QLiveComponent : QComponent,
    QClientLifeCycleListener {

    var client: QLiveClient?
    var roomInfo: QLiveRoomInfo?
    var user: QLiveUser?

    open fun attachLiveClient( client: QLiveClient) {
        this.client = client

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        this.roomInfo = roomInfo
    }

    override fun onEntering(roomId: String, user: QLiveUser) {
        this.user = user
    }

    override fun onLeft() {

    }

    override fun onDestroyed() {
        client =null
    }

}