package com.qlive.uikituser

import android.content.Context
import android.util.AttributeSet
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.uikitcore.QBaseRoomFrameLayout
import com.qlive.uikitcore.ext.toHtml
import kotlinx.android.synthetic.main.kit_view_room_id.view.*


class RoomIdView : QBaseRoomFrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //文本显示
    var getShowTextCall: ((roomInfo: QLiveRoomInfo) -> String) = { info ->
        "<font color='#ffffff'>${info.liveID}</font>"
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_view_room_id
    }

    override fun initView() {
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        tvRoomId.text = getShowTextCall.invoke(roomInfo).toHtml()
    }

}

