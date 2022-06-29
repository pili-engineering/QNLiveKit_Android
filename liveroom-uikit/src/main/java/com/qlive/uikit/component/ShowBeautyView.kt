package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.qlive.uikit.R
import com.qlive.uikit.hook.KITFunctionInflaterFactory
import com.qlive.uikitcore.QBaseRoomFrameLayout
import com.qlive.uikitcore.ShowDialogAble
import com.qlive.uikitcore.ext.setDoubleCheckClickListener
import kotlinx.android.synthetic.main.kit_bottom_menu_view.view.*

class ShowBeautyView : QBaseRoomFrameLayout {

    private var component: ShowDialogAble? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        component =
            KITFunctionInflaterFactory.findLiveComponentByName("com.qlive.uikitbeauty.QSenseBeautyComponent") as ShowDialogAble?
        if (component == null) {
            visibility = View.INVISIBLE
        } else {
            visibility = View.VISIBLE
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_bottom_menu_view
    }

    override fun initView() {
        ivMenu.setImageResource(R.mipmap.kit_effct_show)
        ivMenu.setPadding(4, 4, 4, 4)
        ivMenu.setDoubleCheckClickListener {
            component?.showDialog(0, Unit)
        }
    }
}