package com.qlive.kitlivepre

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.qlive.core.*
import com.qlive.core.QPusherClient
import com.qlive.coreimpl.datesource.UserDataSource
import com.qlive.coreimpl.toast
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.uikitcore.QBaseRoomFrameLayout
import com.qlive.core.been.QCreateRoomParam
import kotlinx.android.synthetic.main.kit_live_preview.view.*

/**
 * 开播预览槽位
 */
class LivePreView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getLayoutId(): Int {
        return R.layout.kit_live_preview
    }

    override fun initView() {

        ivClose.setOnClickListener {
            kitContext?.currentActivity?.finish()
        }
        tvStart.setOnClickListener {
            val titleStr = etTitle.text.toString()
            if (titleStr.isEmpty()) {
                context!!.resources.toast(R.string.preview_tip_input_title)
                return@setOnClickListener
            }
            val noticeStr = etNotice.text.toString() ?: ""
            kitContext?.createAndJoinRoomActionCall?.invoke(
                QCreateRoomParam().apply {
                title = titleStr
                notice = noticeStr
                coverURL = UserDataSource.loginUser.avatar ?: ""
            }, object : QLiveCallBack<Void> {
                override fun onError(code: Int, msg: String?) {}
                override fun onSuccess(data: Void?) {}
            })
        }

        llBeauty.setOnClickListener {

        }

        llSwitch.setOnClickListener {
            (client as QPusherClient)
                .switchCamera(null)
        }
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        visibility = View.GONE
    }

}