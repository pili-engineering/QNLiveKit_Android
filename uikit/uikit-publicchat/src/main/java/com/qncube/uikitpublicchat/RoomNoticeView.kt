package com.qncube.uikitpublicchat

import android.content.Context
import android.util.AttributeSet
import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.uikitcore.QBaseRoomFrameLayout
import com.qncube.uikitcore.ext.toHtml
import kotlinx.android.synthetic.main.kit_notice_view.view.*

/**
 * 公告槽位
 */
class RoomNoticeView : QBaseRoomFrameLayout {
    companion object{
        //添加到公聊头部
        var isAddToPubChatListHeader = true
    }
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

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        tvNotice.text = noticeHtmlShowAdapter.invoke(roomInfo.notice).toHtml()
    }
}