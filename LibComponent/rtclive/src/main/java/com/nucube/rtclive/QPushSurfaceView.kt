package com.nucube.rtclive

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.qiniu.droid.rtc.QNSurfaceView
import com.qiniu.droid.rtc.QNTextureView
import com.qncube.lcommon.QPushRenderView

class QPushSurfaceView  : QNSurfaceView, QPushRenderView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun getView(): View {
        return this
    }
}