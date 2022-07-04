package com.qlive.uikitcore

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.qlive.core.QLiveClient
import com.qlive.core.QClientLifeCycleListener
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser

/**
 * 直播间内小组件
 *
 * 父接口：
 * QClientLifeCycleListener -> client 生命周期
 * LifecycleEventObserver ->  房间客户端生命周期
 */
interface QLiveComponent : QClientLifeCycleListener, LifecycleEventObserver {

    fun attachKitContext(context: QLiveUIKitContext)

    /**
     * 绑定房间客户端
     */
    fun attachLiveClient(client: QLiveClient)

}
