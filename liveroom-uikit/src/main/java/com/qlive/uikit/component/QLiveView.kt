package com.qlive.uikit.component

import com.qlive.uikit.hook.KITLiveInflaterFactory
import com.qlive.uikitcore.QLiveEmptyView
import com.qlive.uikitcore.QLiveComponent

//内置UI型号组件容器
open class QLiveView(private val originViewClass: Class<out QLiveComponent>) {

    init {
        KITLiveInflaterFactory.innerComponentClass.put(originViewClass.name,originViewClass)
    }
    private var replaceClass: Class<out QLiveComponent>? = null

    /**
     * 是否可用
     */
    var isEnable: Boolean = true
        set(value) {
            field = value
            checkReplace()
        }

    /**
     * 替换成用户自定义的UI类
     */
   open fun <T : QLiveComponent> replace(replaceClass: Class<T>) {
        this.replaceClass = replaceClass
        checkReplace()
    }

    private fun checkReplace() {
        if (isEnable) {
            if (replaceClass != null) {
                KITLiveInflaterFactory.replaceViews[originViewClass.name] = replaceClass!!
            } else {
                KITLiveInflaterFactory.replaceViews.remove(
                    originViewClass.name
                )
            }
        } else {
            KITLiveInflaterFactory.replaceViews.put(
                originViewClass.name,
                QLiveEmptyView::class.java
            )
        }
    }

}