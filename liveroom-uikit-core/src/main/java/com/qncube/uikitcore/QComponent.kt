package com.qncube.uikitcore

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

interface QComponent : LifecycleEventObserver {

    var QUIKitContext: QUIKitContext?

    fun attachKitContext(context: QUIKitContext) {
        this.QUIKitContext = context
        context.lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            QUIKitContext = null
        }
    }

}