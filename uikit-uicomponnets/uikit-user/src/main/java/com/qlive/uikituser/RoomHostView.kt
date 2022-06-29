package com.qlive.uikituser

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikitcore.QBaseRoomFrameLayout
import com.qlive.uikitcore.ext.toHtml
import kotlinx.android.synthetic.main.kit_view_room_host_slot.view.*

//房主信息左上角
class RoomHostView : QBaseRoomFrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //房主头像点击事件回调 提供点击事件自定义回调
    var mClickCallBack: (anchor: QLiveUser) -> Unit = {

    }

    /**
     * 标题自定义显示回调 默认房间标题
     */
    var showHostTitleCall: ((room: QLiveRoomInfo) -> String) = {
        "<font color='#ffffff'>" + it.title + "</font>"
    }

    /**
     * 副标题自定义回调 默认房间ID
     */
    var showSubTitleCall: ((room: QLiveRoomInfo) -> String) = {
        "<font color='#ffffff'>" + it.anchor.nick + "</font>"
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_view_room_host_slot;
    }

    override fun initView() {
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        ivHost.setOnClickListener {
            mClickCallBack(roomInfo.anchor)
        }
        tvTitle.text = showHostTitleCall.invoke(roomInfo).toHtml()
        tvSubTitle.text = showSubTitleCall.invoke(roomInfo).toHtml()
        Glide.with(context!!)
            .load(roomInfo.anchor.avatar)
            .into(ivHost)
    }

}

