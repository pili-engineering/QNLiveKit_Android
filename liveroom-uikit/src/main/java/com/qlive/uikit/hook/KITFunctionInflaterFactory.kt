package com.qlive.uikit.hook

import com.qlive.core.QClientLifeCycleListener
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.QLiveComponent
import kotlin.collections.HashSet

/**
 * 功能组件装载器
 */
object KITFunctionInflaterFactory :
    QClientLifeCycleListener {

    val functionComponents = HashSet<QLiveComponent>()

    fun attachLiveClient(client: QLiveClient) {
        functionComponents.forEach {
            it.attachLiveClient(client)
        }
    }

    fun attachKitContext(context: QLiveUIKitContext) {
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