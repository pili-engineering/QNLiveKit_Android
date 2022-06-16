package com.qncube.liveuikit.hook

import com.qncube.linveroominner.QClientLifeCycleListener
import com.qncube.liveroomcore.QLiveClient
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.LiveKitContext
import com.qncube.uikitcore.QLiveComponent
import kotlin.collections.HashSet

object KITFunctionInflaterFactory :
    QClientLifeCycleListener {

    val functionComponents = HashSet<QLiveComponent>()

    fun attachLiveClient(client: QLiveClient) {
        functionComponents.forEach {
            it.attachLiveClient(client)
        }
    }

    fun attachKitContext(context: LiveKitContext) {
        functionComponents.forEach {
            it.attachKitContext(context)
            context.lifecycleOwner.lifecycle.addObserver(it)
        }
    }

    override fun onEntering(liveId: String, user: QLiveUser) {
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