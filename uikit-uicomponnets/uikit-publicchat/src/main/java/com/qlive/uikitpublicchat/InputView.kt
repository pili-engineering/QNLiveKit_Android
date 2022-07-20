package com.qlive.uikitpublicchat

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import com.qlive.core.QLiveCallBack
import com.qlive.pubchatservice.QPublicChat
import com.qlive.pubchatservice.QPublicChatService
import com.qlive.uikitcore.QKitFrameLayout
import com.qlive.uikitinput.RoomInputDialog

class InputView : QKitFrameLayout {
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
                    client?.getService(QPublicChatService::class.java)
                        ?.sendPublicChat(it, object :
                            QLiveCallBack<QPublicChat> {
                            override fun onError(code: Int, msg: String) {
                                Toast.makeText(context, "发送失败" + msg, Toast.LENGTH_SHORT).show()
                            }

                            override fun onSuccess(data: QPublicChat?) {
                            }
                        })
                }
            }.show(kitContext!!.fragmentManager, "")
        }
    }

    override fun getLayoutId(): Int {
        return -1
    }

    override fun initView() {
    }
}