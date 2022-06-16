package com.qncube.uikitcore

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.qncube.liveroomcore.QLiveClient
import com.qncube.linveroominner.QClientLifeCycleListener
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser

interface QLiveComponent :
    QClientLifeCycleListener , LifecycleEventObserver {

    var client: QLiveClient?
    var roomInfo: QLiveRoomInfo?
    var user: QLiveUser?
    var kitContext: LiveKitContext?
    fun attachKitContext(context: LiveKitContext) {
        this.kitContext = context
        context.lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            kitContext = null
        }
    }

    open fun attachLiveClient(client: QLiveClient) {
        this.client = client

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
        client = null
    }

}