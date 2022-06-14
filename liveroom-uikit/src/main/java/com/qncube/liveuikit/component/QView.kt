package com.qncube.liveuikit.component

import android.view.View
import com.qncube.liveuikit.hook.KITLayoutInflaterFactory
import com.qncube.uikitcore.QBaseRoomFrameLayout
import com.qncube.uikitcore.QComponent
import com.qncube.uikitcore.QEmptyView

//内置UI型号组件
public open class QView(val originViewClass: Class<out QComponent>) {

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