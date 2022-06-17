package com.qlive.uikitdanmaku

import android.content.Context
import android.util.AttributeSet
import com.qlive.danmakuservice.QDanmaku
import com.qlive.danmakuservice.QDanmakuService
import com.qlive.core.QLiveCallBack
import com.qlive.uikitcore.QBaseRoomFrameLayout
import com.qlive.uikitcore.ext.asToast
import com.qlive.uikitinput.RoomInputDialog

class SendDanmakuView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getLayoutId(): Int {
        return R.layout.kit_view_send_danmaku
    }

    override fun initView() {
        setOnClickListener {
            RoomInputDialog().apply {
                sendPubCall = {
                    send(it)
                }
            }.show(kitContext!!.fragmentManager, "")
        }
    }

    private fun send(msg: String) {
        client?.getService(QDanmakuService::class.java)?.sendDanmaku(msg, null,
            object : QLiveCallBack<QDanmaku> {
                override fun onError(code: Int, msg: String?) {
                    msg?.asToast(context)
                }

                override fun onSuccess(data: QDanmaku?) {
                }
            })
    }
}