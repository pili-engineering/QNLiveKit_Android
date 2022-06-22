package com.qlive.rtclive

import android.view.View
import com.qiniu.droid.rtc.QNRenderView

interface QPushRenderView {
    fun setMirror(var1: Boolean)
    fun getView(): View;
    fun getQNRender(): QNRenderView
}