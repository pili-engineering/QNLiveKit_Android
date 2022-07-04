package com.qlive.uikituser

import android.content.Context
import android.util.AttributeSet
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.uikitcore.QKitTextView
import com.qlive.uikitcore.ext.toHtml


class RoomIdView : QKitTextView {
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

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        text = getShowTextCall.invoke(roomInfo).toHtml()
    }

}

