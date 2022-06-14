package com.qncube.liveuikit.hook

import com.qncube.liveroomcore.QClientLifeCycleListener
import com.qncube.liveroomcore.QNLiveClient
import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.liveroomcore.mode.QNLiveUser
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.QLiveComponent
import kotlin.collections.HashSet

object KITFunctionInflaterFactory : QClientLifeCycleListener {

    val functionComponents = HashSet<QLiveComponent>()

    fun attachLiveClient(client: QNLiveClient) {
        functionComponents.forEach {
            it.attachLiveClient(client)
        }
    }

    fun attachKitContext(context: KitContext) {
        functionComponents.forEach {
            it.attachKitContext(context)
            context.lifecycleOwner.lifecycle.addObserver(it)
        }
    }

    override fun onEntering(liveId: String, user: QNLiveUser) {
        functionComponents.forEach {
            it.onEntering(liveId, user)
        }
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        functionComponents.forEach {
            it.onJoined(roomInfo)
        }
    }

    override fun onLeft() {
        functionComponents.forEach {
            it.onLeft()
        }
    }

    override fun onDestroyed() {
        functionComponents.forEach {
            it.onDestroyed()
        }
    }

}