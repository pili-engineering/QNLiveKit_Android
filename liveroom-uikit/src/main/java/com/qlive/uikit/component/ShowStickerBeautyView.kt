package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.qlive.uikitcore.BeautyComponent
import com.qlive.uikitcore.QKitImageView
import com.qlive.uikitcore.ext.setDoubleCheckClickListener

class ShowStickerBeautyView : QKitImageView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (BeautyComponent.isEnable) {
            setDoubleCheckClickListener {
                BeautyComponent.showBeautyStickDialog.invoke(kitContext!!.fragmentManager)
            }
            visibility = View.VISIBLE
        } else {
            visibility = View.INVISIBLE
        }
    }
}