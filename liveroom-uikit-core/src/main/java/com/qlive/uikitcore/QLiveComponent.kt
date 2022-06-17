package com.qlive.uikitcore

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.qlive.core.QLiveClient
import com.qlive.core.QClientLifeCycleListener
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser

interface QLiveComponent :
    QClientLifeCycleListener, LifecycleEventObserver {

    fun attachKitContext(context: QLiveUIKitContext)
    fun attachLiveClient(client: QLiveClient)
}