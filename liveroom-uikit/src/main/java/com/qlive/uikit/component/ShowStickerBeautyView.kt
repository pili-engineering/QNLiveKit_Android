package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.qlive.uikit.R
import com.qlive.uikit.hook.KITFunctionInflaterFactory
import com.qlive.uikitcore.QKitImageView
import com.qlive.uikitcore.ShowDialogAble
import com.qlive.uikitcore.ext.setDoubleCheckClickListener

class ShowStickerBeautyView : QKitImageView {

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
        setDoubleCheckClickListener {
            component?.showDialog(1, Unit)
        }
    }
}