package com.qlive.uikitdanmaku

import android.content.Context
import android.util.AttributeSet
import com.qlive.core.been.QDanmaku
import com.qlive.danmakuservice.QDanmakuService
import com.qlive.core.QLiveCallBack
import com.qlive.uikitcore.QKitImageView
import com.qlive.uikitcore.ext.asToast
import com.qlive.uikitinput.RoomInputDialog

/**
 * 发弹幕按钮
 */
class SendDanmakuView : QKitImageView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
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