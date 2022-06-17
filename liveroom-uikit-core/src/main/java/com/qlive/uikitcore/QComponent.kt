package com.qlive.uikitcore

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

interface QComponent : LifecycleEventObserver {

    var kitContext: QUIKitContext?

    fun attachKitContext(context: QUIKitContext) {
        this.kitContext = context
        context.lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            kitContext = null
        }
    }

}