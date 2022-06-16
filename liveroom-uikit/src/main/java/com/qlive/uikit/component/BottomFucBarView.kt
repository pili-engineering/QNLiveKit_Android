package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qlive.coreimpl.asToast
import com.qlive.core.QClientType
import com.qlive.core.QLiveCallBack
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikit.R
import com.qlive.pubchatservice.QPublicChatService
import com.qlive.uikitcore.QBaseRoomFrameLayout
import com.qlive.uikitcore.QBaseRoomLinearLayout
import com.qlive.uikitcore.QLiveComponent
import com.qlive.uikitcore.dialog.LoadingDialog
import com.qlive.uikitdanmaku.SendDanmakuView
import com.qlive.uikitlinkmic.StartLinkView
import com.qlive.uikitinput.RoomInputDialog
import kotlinx.android.synthetic.main.kit_close_menu_view.view.*


class BottomFucBarView : QBaseRoomLinearLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        orientation = HORIZONTAL
    }

    //主播菜单默认 发弹幕 美颜 关房间
    private val mAnchorFuncMenus by lazy {
        ArrayList<QLiveComponent>().apply {
            add(SendDanmakuView(context))
            add(CloseRoomView(context))
        }
    }

    //用户菜单默认 发弹幕 连麦 关房间
    private val mAudienceFuncMenus by lazy {
        ArrayList<QLiveComponent>().apply {
            add(SendDanmakuView(context))
            add(StartLinkView(context))
            add(CloseRoomView(context))
        }
    }

    private var menus = ArrayList<QLiveComponent>()
    override fun initView() {
        menus = if (client!!.clientType == QClientType.PLAYER) {
            mAudienceFuncMenus
        } else {
            mAnchorFuncMenus
        }
        menus.forEach {
            if (it is View) {
                it.attachKitContext(kitContext!!)
                it.attachLiveClient(client!!)
                addView(it)
            }
        }
    }


    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        menus.forEach {
            it.onJoined(roomInfo)
        }
    }

    override fun onDestroyed() {
        super.onDestroyed()
        menus.forEach {
            it.onDestroyed()
        }
    }

    override fun onLeft() {
        super.onLeft()
        menus.forEach {
            it.onLeft()
        }
    }

    override fun onEntering(roomId: String, user: QLiveUser) {
        super.onEntering(roomId, user)
        menus.forEach {
            it.onEntering(roomId, user)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
        menus.forEach {
            it.onStateChanged(source, event)
        }
    }
}

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
        ivClose.setOnClickListener {
            LoadingDialog.showLoading(kitContext!!.fm)
            client?.getService(QPublicChatService::class.java)
                ?.sendByeBye("离开了房间", null)

            val call = object :
                QLiveCallBack<Void> {
                override fun onError(code: Int, msg: String?) {
                    LoadingDialog.cancelLoadingDialog()
                    msg?.asToast()
                }

                override fun onSuccess(data: Void?) {
                    LoadingDialog.cancelLoadingDialog()
                    client?.destroy()
                    kitContext?.currentActivity?.finish()
                }
            }
            kitContext?.leftRoomActionCall?.invoke(call)
        }
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        client?.getService(QPublicChatService::class.java)
            ?.sendWelCome("进人了房间", null)
    }
}


