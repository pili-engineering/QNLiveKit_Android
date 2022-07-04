package com.qlive.uikit.component

import com.qlive.uikit.hook.KITFunctionInflaterFactory
import com.qlive.uikitcore.QLiveComponent

/**
 * 功能组件容器
 */
class QLiveFunctionComponent(val originComponent: QLiveComponent) {

    init {
        KITFunctionInflaterFactory.functionComponents.add(originComponent)
    }

    private var replaceComponent: (QLiveComponent)? = null

    var isEnable: Boolean = true
        set(value) {
            field = value
            check()
        }

    fun <T : QLiveComponent> replace(replaceComponent: T) {
        this.replaceComponent = replaceComponent
        check()
    }

    private fun check() {
        if (isEnable) {
            if (replaceComponent != null) {
                KITFunctionInflaterFactory.functionComponents.add(replaceComponent!!)
            } else {
                KITFunctionInflaterFactory.functionComponents.add(originComponent)
            }
        } else {
            replaceComponent?.let {
                KITFunctionInflaterFactory.functionComponents.remove(it)
            }
            KITFunctionInflaterFactory.functionComponents.remove(originComponent)
        }
    }
}