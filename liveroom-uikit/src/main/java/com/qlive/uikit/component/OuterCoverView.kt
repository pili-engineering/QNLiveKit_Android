package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import com.qlive.uikitcore.QBaseRoomFrameLayout
/**
 * 覆盖层空UI
 * 在内置UI上层
 */
class OuterCoverView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getLayoutId(): Int {
       return -1
    }

    override fun initView() {

    }
}