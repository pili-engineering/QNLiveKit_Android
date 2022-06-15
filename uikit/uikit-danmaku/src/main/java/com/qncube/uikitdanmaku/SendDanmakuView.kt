package com.qncube.uikitdanmaku

import android.content.Context
import android.util.AttributeSet
import com.qncube.danmakuservice.QDanmaku
import com.qncube.danmakuservice.QDanmakuService
import com.qncube.linveroominner.asToast
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.uikitcore.QBaseRoomFrameLayout

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

    }

    fun send(msg: String) {
        client?.getService(QDanmakuService::class.java)?.sendDanmaku(msg, null,
            object : QLiveCallBack<QDanmaku> {
                override fun onError(code: Int, msg: String?) {
                    msg?.asToast()
                }

                override fun onSuccess(data: QDanmaku?) {
                }
            })
    }
}