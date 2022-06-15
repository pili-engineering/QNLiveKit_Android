package com.qncube.liveuikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.qncube.linveroominner.asToast
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveuikit.R
import com.qncube.liveuikit.RoomPage
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.QComponent
import kotlinx.android.synthetic.main.kit_btn_create_room.view.*

class CreateRoomButton : FrameLayout, QComponent {
    override var kitContext: KitContext? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.kit_btn_create_room, this, true)
        tvCreateRoom.setOnClickListener {
            RoomPage.createAndJoinRoom(context, object : QLiveCallBack<QLiveRoomInfo> {
                override fun onError(code: Int, msg: String?) {
                    msg?.asToast()
                }

                override fun onSuccess(data: QLiveRoomInfo?) {}
            })
        }
    }
}