package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.qlive.uikit.R
import com.qlive.uikit.hook.KITFunctionInflaterFactory
import com.qlive.uikitcore.QBaseRoomFrameLayout
import com.qlive.uikitcore.ShowDialogAble
import com.qlive.uikitcore.ext.setDoubleCheckClickListener

class ShowBeautyPreview : QBaseRoomFrameLayout {

    private var component: ShowDialogAble? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        component =
            KITFunctionInflaterFactory.findLiveComponentByName("senseBeautyComponent") as ShowDialogAble?
        if (component == null) {
            visibility = View.INVISIBLE
        } else {
            visibility = View.VISIBLE
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_show_beauty_pre_view
    }

    override fun initView() {
        setDoubleCheckClickListener {
            component?.showDialog(1, Unit)
        }
    }
}