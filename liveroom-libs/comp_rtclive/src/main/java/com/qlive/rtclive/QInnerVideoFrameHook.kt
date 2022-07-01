package com.qlive.rtclive

import android.content.Context
import com.qiniu.droid.rtc.QNVideoFrameListener

object QInnerVideoFrameHook {
     var mBeautyHooker: BeautyHooker? = null
}

interface BeautyHooker {
    fun init(context: Context)
    fun provideVideoFrameListener(): QNVideoFrameListener
    fun attach()
    fun detach()
}
