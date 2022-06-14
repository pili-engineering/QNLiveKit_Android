package com.qncube.liveuikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.qncube.liveroomcore.QClientType
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.asToast
import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.liveuikit.R
import com.qncube.publicchatservice.QPublicChatService
import com.qncube.uikitcore.QBaseRoomFrameLayout
import com.qncube.uikitcore.QBaseRoomLinearLayout
import com.qncube.uikitcore.QLiveComponent
import com.qncube.uikitcore.dialog.LoadingDialog
import com.qncube.uikitdanmaku.SendDanmakuView
import com.qncube.uikitlinkmic.StartLinkView
import com.qucube.uikitinput.RoomInputDialog
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
            add(SendDanmakuView(context).apply {
                setOnClickListener {
                    RoomInputDialog().apply {
                        sendPubCall = {
                            send(it)
                        }
                    }.show(kitContext!!.fm, "")
                }
            })
            add(CloseRoomView(context))
        }
    }

    //用户菜单默认 发弹幕 连麦 关房间
    private val mAudienceFuncMenus by lazy {
        ArrayList<QLiveComponent>().apply {
            add(SendDanmakuView(context).apply {
                setOnClickListener {
                    RoomInputDialog().apply {
                        sendPubCall = {
                            send(it)
                        }
                    }.show(kitContext!!.fm, "")
                }
            })
            add(StartLinkView(context))
            add(CloseRoomView(context))
        }
    }

    override fun initView() {
        val menus = if (client!!.clientType == QClientType.PLAYER) {
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

                client?.leaveRoom(object :
                    QLiveCallBack<Void> {
                    override fun onError(code: Int, msg: String?) {
                        LoadingDialog.cancelLoadingDialog()
                        msg?.asToast()
                    }

                    override fun onSuccess(data: Void?) {
                        LoadingDialog.cancelLoadingDialog()
                        client?.closeRoom()
                        kitContext?.currentActivity?.finish()
                    }
                })
            }
        }

        override fun onJoined(roomInfo: QLiveRoomInfo) {
            super.onJoined(roomInfo)
            client?.getService(QPublicChatService::class.java)
                ?.sendWelCome("进人了房间", null)
        }
    }
}



