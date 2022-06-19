package com.qlive.uikit.component

import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qlive.core.QLiveClient
import com.qlive.core.QLiveStatus
import com.qlive.core.QLiveStatusListener
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikitcore.QLiveComponent
import com.qlive.uikitcore.QLiveUIKitContext

class AnchorOfflineMonitorComponent : QLiveComponent {

    var kitContext: QLiveUIKitContext? = null
    override fun attachKitContext(context: QLiveUIKitContext) {
        kitContext = context
    }

    override fun attachLiveClient(client: QLiveClient) {
        client.setLiveStatusListener(object : QLiveStatusListener {
            override fun onLiveStatusChanged(liveStatus: QLiveStatus) {
                if (liveStatus == QLiveStatus.ANCHOR_OFFLINE) {
                    Toast.makeText(kitContext?.androidContext, "房主离线", Toast.LENGTH_SHORT).show()
                    kitContext?.currentActivity?.finish()
                }
            }
        })
    }

    override fun onEntering(liveId: String, user: QLiveUser) {
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
    }

    override fun onLeft() {
    }

    override fun onDestroyed() {
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    }
}