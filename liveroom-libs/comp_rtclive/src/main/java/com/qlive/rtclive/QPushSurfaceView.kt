package com.qlive.rtclive

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.qiniu.droid.rtc.QNRenderView
import com.qiniu.droid.rtc.QNSurfaceView

class QPushSurfaceView  :FrameLayout , QPushRenderView {

    private var renderView: QNSurfaceView? = null
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        renderView = QNSurfaceView(context)
        addView(
            renderView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun setMirror(var1: Boolean) {
        renderView?.setMirror(var1)
    }

    override fun getView(): View {
        return this
    }

    override fun getQNRender(): QNRenderView {
        return renderView!!
    }
}