package com.qlive.uikitcore

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser

open class BaseQLiveComponent : QLiveComponent {
    var client: QLiveClient? = null
    var roomInfo: QLiveRoomInfo? = null
    var user: QLiveUser? = null
    var kitContext: QLiveUIKitContext? = null

    override fun attachLiveClient(client: QLiveClient) {
        this.client = client
    }

    override fun attachKitContext(context: QLiveUIKitContext) {
        this.kitContext = context
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            kitContext = null
        }
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