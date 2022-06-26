package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.qlive.core.QLiveCallBack
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.sdk.QLive
import com.qlive.uikit.R
import com.qlive.uikit.RoomPage
import com.qlive.uikitcore.QUIKitContext
import com.qlive.uikitcore.QComponent
import com.qlive.uikitcore.ext.asToast
import kotlinx.android.synthetic.main.kit_btn_create_room.view.*

class CreateRoomButton : FrameLayout, QComponent {
    override var kitContext: QUIKitContext? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.kit_btn_create_room, this, true)
        tvCreateRoom.setOnClickListener {
            QLive.getLiveUIKit().getPage(RoomPage::class.java).startAnchorRoomWithPreview(context, object : QLiveCallBack<QLiveRoomInfo> {
                override fun onError(code: Int, msg: String?) {
                    msg?.asToast(kitContext?.androidContext)
                }

                override fun onSuccess(data: QLiveRoomInfo?) {}
            })
        }
    }
}