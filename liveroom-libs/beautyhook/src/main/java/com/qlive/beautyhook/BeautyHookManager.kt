package com.qlive.beautyhook

import androidx.fragment.app.FragmentManager
import com.qlive.rtclive.BeautyHooker
import com.qlive.rtclive.QInnerVideoFrameHook

object BeautyHookManager {
    var isEnable = false

    fun checkHasHooker(): Boolean {
        try {
            val classStr = "com.qlive.beautyhookimpl.BeautyHookerImpl"
            val classImpl = Class.forName(classStr)
            val constructor = classImpl.getConstructor()
            val obj = constructor.newInstance() as BeautyHooker
            QInnerVideoFrameHook.mBeautyHooker = obj
            isEnable = true
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            isEnable = false
        }
        return isEnable
    }

    var showBeautyEffectDialog: ((fragmentM: FragmentManager) -> Unit) = {

    }

    var showBeautyStickDialog: ((fragmentM: FragmentManager) -> Unit) = {

    }

}