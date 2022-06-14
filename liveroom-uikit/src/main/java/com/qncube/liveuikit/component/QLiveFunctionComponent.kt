package com.qncube.liveuikit.component

import com.qncube.liveuikit.hook.KITFunctionInflaterFactory
import com.qncube.uikitcore.QLiveComponent

class QLiveFunctionComponent(val originComponent: QLiveComponent) {

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