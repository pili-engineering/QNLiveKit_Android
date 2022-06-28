package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.Toast
import com.qlive.core.QLiveCallBack
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.linkmicservice.QLinkMicService
import com.qlive.pubchatservice.QPublicChatService
import com.qlive.uikit.R
import com.qlive.uikitcore.QBaseRoomFrameLayout
import com.qlive.uikitcore.dialog.LoadingDialog
import com.qlive.uikitcore.ext.setDoubleCheckClickListener
import kotlinx.android.synthetic.main.kit_close_menu_view.view.*


//关闭房间菜单
class CloseRoomView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getLayoutId(): Int {
        return R.layout.kit_close_menu_view
    }

    override fun initView() {
        ivClose.setDoubleCheckClickListener {
            LoadingDialog.showLoading(kitContext!!.fragmentManager)
            //发离开房间消息
            client?.getService(QPublicChatService::class.java)
                ?.sendByeBye("离开了房间", null)

            val call = object :
                QLiveCallBack<Void> {
                override fun onError(code: Int, msg: String?) {
                    LoadingDialog.cancelLoadingDialog()
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(data: Void?) {
                    LoadingDialog.cancelLoadingDialog()
                    client?.destroy()
                    kitContext?.currentActivity?.finish()
                }
            }
            //调用关闭房间方法
            kitContext?.leftRoomActionCall?.invoke(call)
        }
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        //发进入房间消息
        client?.getService(QPublicChatService::class.java)
            ?.sendWelCome("进人了房间", null)
    }
}
