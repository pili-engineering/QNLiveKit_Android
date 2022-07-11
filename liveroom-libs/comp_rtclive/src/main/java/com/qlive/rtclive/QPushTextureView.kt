package com.qlive.rtclive

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.qiniu.droid.rtc.QNRenderView
import com.qiniu.droid.rtc.QNTextureView

open class QPushTextureView : FrameLayout, QPushRenderView {
    private var renderView: QNTextureView? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        renderView = QNTextureView(context)
        addView(
            renderView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
    }

    override fun getView(): View {
        return this
    }

    override fun getQNRender(): QNRenderView {
        return renderView!!
    }
}
