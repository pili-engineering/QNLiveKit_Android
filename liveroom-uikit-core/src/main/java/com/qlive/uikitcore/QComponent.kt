package com.qlive.uikitcore

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * 小组件
 */
interface QComponent : LifecycleEventObserver {

    var kitContext: QUIKitContext?

    /**
     * 绑定UI上下文
     */
    fun attachKitContext(context: QUIKitContext) {
        this.kitContext = context
        context.lifecycleOwner.lifecycle.addObserver(this)
    }

    /**
     * activity 生命周期
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            kitContext = null
        }
    }
}