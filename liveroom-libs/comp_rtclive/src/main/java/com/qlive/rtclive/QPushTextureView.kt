package com.qlive.rtclive

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.qiniu.droid.rtc.QNTextureView
import com.qlive.avparam.QPushRenderView

open class QPushTextureView : QNTextureView, QPushRenderView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun getView(): View {
        return this
    }
}