package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.qlive.core.QLiveCallBack
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.linkmicservice.QLinkMicService
import com.qlive.pubchatservice.QPublicChatService
import com.qlive.uikit.R
import com.qlive.uikitcore.QKitImageView
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.dialog.LoadingDialog
import com.qlive.uikitcore.ext.setDoubleCheckClickListener


//关闭房间菜单
class CloseRoomView : QKitImageView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setDoubleCheckClickListener {
            LoadingDialog.showLoading(kitContext!!.fragmentManager)

            //发离开房间消息
            client?.getService(QPublicChatService::class.java)
                ?.sendByeBye("离开了房间", null)

            val call = object :
                QLiveCallBack<Void> {
                override fun onError(code: Int, msg: String?) {
                    LoadingDialog.cancelLoadingDialog()
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    kitContext?.currentActivity?.finish()
                }

                override fun onSuccess(data: Void?) {
                    LoadingDialog.cancelLoadingDialog()
                    kitContext?.currentActivity?.finish()
                }
            }
            //调用关闭房间方法
            kitContext?.leftRoomActionCall?.invoke(call)
        }
    }

    override fun attachKitContext(context: QLiveUIKitContext) {
        super.attachKitContext(context)
        visibility = View.GONE
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        visibility = VISIBLE
        //发进入房间消息
        client?.getService(QPublicChatService::class.java)
            ?.sendWelCome("进人了房间", null)
    }
}
