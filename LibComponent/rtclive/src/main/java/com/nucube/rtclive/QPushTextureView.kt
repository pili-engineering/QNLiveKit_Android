package com.nucube.rtclive

import android.content.Context
import android.util.AttributeSet
import com.qiniu.droid.rtc.QNTextureView
import com.qncube.lcommon.QPushRenderView

class QPushTextureView : QNTextureView, QPushRenderView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
}
