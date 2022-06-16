package com.qlive.uikit.component

import com.qlive.uikit.hook.KITInflaterFactory
import com.qlive.uikitcore.QComponent
import com.qlive.uikitcore.QEmptyView

//内置UI型号组件
public open class QView(val originViewClass: Class<out QComponent>) {

    init {
        KITInflaterFactory.innerComponentClass.put(originViewClass.name,originViewClass)
    }
    var replaceClass: Class<out QComponent>? = null
    /**
     * 是否可用
     */
    var isEnable: Boolean = true
        set(value) {
            field = value
            checkReplace()
        }

    fun <T : QComponent> replace(replaceClass: Class<T>) {
        this.replaceClass = replaceClass
        checkReplace()
    }

    private fun checkReplace(){
        if (isEnable) {
            if (replaceClass != null) {
                KITInflaterFactory.replaceViews[originViewClass.name] = replaceClass!!
            } else {
                KITInflaterFactory.replaceViews.remove(
                    originViewClass.name
                )
            }
        } else {
            KITInflaterFactory.replaceViews.put(
                originViewClass.name,
                QEmptyView::class.java
            )
        }
    }

}