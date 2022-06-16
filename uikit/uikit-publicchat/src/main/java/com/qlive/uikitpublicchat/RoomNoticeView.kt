package com.qlive.uikitpublicchat

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.uikitcore.QBaseRoomFrameLayout
import com.qlive.uikitcore.ext.toHtml
import kotlinx.android.synthetic.main.kit_notice_view.view.*
import kotlinx.coroutines.*

/**
 * 公告槽位
 */
class RoomNoticeView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    //显示公告
    var noticeHtmlShowAdapter: ((notice: String) -> String) = {
        "  <font color='#3ce1ff'>官方公告</font>" + " <font color='#ffb83c'>" + ":${it}</font>";
    }

    //背景
    var backgroundView: Int = R.drawable.kit_shape_40000000_6

    override fun getLayoutId(): Int {
        return R.layout.kit_notice_view
    }

    override fun initView() {
        tvNotice.setBackgroundResource(backgroundView)
    }

    private var goneJob: Job? = null
    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        goneJob?.cancel()
        tvNotice.text = noticeHtmlShowAdapter.invoke(roomInfo.notice).toHtml()
        if (tvNotice.text.isEmpty()) {
            visibility = View.GONE
            return
        }
        visibility = View.VISIBLE
        goneJob = kitContext?.lifecycleOwner?.lifecycleScope?.launch(Dispatchers.Main) {
            try {
                delay(1000 * 60)
                visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}