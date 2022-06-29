package com.qlive.uikituser

import android.content.Context
import android.util.AttributeSet
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.uikitcore.QKitTextView
import com.qlive.uikitcore.Scheduler
import com.qlive.uikitcore.ext.toHtml
import java.text.DecimalFormat


class RoomTimerView : QKitTextView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    var showTimeCall: ((time: Int) -> String) = {
        "<font color='#ffffff'>${formatTime(it)}</font>"
    }

    private fun formatTime(time: Int): String {
        val decimalFormat = DecimalFormat("00")
        val hh: String = decimalFormat.format(time / 3600)
        val mm: String = decimalFormat.format(time % 3600 / 60)
        val ss: String = decimalFormat.format(time % 60)
        return if (hh == "00") {
            "$mm:$ss"
        } else {
            "$hh:$mm:$ss"
        }
    }

    private var total = 0;
    private val mScheduler = Scheduler(1000) {
        total++
        text = showTimeCall(total).toHtml()
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        mScheduler.start()
    }

    override fun onLeft() {
        super.onLeft()
        mScheduler.cancel()
    }

}
