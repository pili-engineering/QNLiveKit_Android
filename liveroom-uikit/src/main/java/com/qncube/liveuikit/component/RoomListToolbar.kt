package com.qncube.liveuikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.qncube.liveuikit.R
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.QComponent
import kotlinx.android.synthetic.main.kit_room_list_toobar.view.*

class RoomListToolbar : FrameLayout, QComponent {
    override var kitContext: KitContext? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.kit_room_list_toobar, this, true)
        tvBack.setOnClickListener {
            kitContext?.currentActivity?.finish()
        }
    }

}