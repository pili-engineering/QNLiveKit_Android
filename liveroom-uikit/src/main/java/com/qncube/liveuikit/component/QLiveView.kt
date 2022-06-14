package com.qncube.liveuikit.component

import com.qncube.liveuikit.hook.KITLayoutInflaterFactory
import com.qncube.uikitcore.QComponent
import com.qncube.uikitcore.QEmptyView
import com.qncube.uikitcore.QLiveComponent

//内置UI型号组件
open class QLiveView(private val originViewClass: Class<out QLiveComponent>) {

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
    fun <T : QLiveComponent> replace(replaceClass: Class<T>) {
        this.replaceClass = replaceClass
        checkReplace()
    }

    private fun checkReplace() {
        if (isEnable) {
            if (replaceClass != null) {
                KITLayoutInflaterFactory.replaceViews[originViewClass.name] = replaceClass!!
            } else {
                KITLayoutInflaterFactory.replaceViews.remove(
                    originViewClass.name
                )
            }
        } else {
            KITLayoutInflaterFactory.replaceViews.put(
                originViewClass.name,
                QEmptyView::class.java
            )
        }
    }

}