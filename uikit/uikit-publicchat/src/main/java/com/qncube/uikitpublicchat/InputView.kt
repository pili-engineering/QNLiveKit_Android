package com.qncube.uikitpublicchat

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.publicchatservice.PubChatModel
import com.qncube.publicchatservice.QPublicChatService
import com.qncube.uikitcore.QBaseRoomFrameLayout
import com.qucube.uikitinput.RoomInputDialog
import kotlinx.android.synthetic.main.kit_input.view.*

class InputView : QBaseRoomFrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getLayoutId(): Int {
        return R.layout.kit_input
    }

    override fun initView() {
        flInput.setOnClickListener {
            RoomInputDialog().apply {
                sendPubCall = {
                    client?.getService(QPublicChatService::class.java)
                        ?.sendPublicChat(it, object :
                            QLiveCallBack<PubChatModel> {
                            override fun onError(code: Int, msg: String) {
                                Toast.makeText(context, "发送失败" + msg, Toast.LENGTH_SHORT).show()
                            }

                            override fun onSuccess(data: PubChatModel?) {
                            }
                        })
                }
            }.show(kitContext!!.fm, "")
        }
    }
}