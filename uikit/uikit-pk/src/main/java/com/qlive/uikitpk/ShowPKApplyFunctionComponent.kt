package com.qlive.uikitpk

import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qlive.pkservice.QPKService
import com.qlive.coreimpl.asToast
import com.qlive.core.*
import com.qlive.core.been.QInvitation
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikitcore.QLiveComponent
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.dialog.CommonTipDialog
import com.qlive.uikitcore.dialog.FinalDialogFragment


class ShowPKApplyFunctionComponent : QLiveComponent {

     var client: QLiveClient? = null
     var roomInfo: QLiveRoomInfo? = null
     var user: QLiveUser? = null
     var kitContext: QLiveUIKitContext ? = null

    private val mPKInvitationListener = object : QInvitationHandlerListener {
        override fun onReceivedApply(pkInvitation: QInvitation) {
            CommonTipDialog.TipBuild()
                .setTittle("PK邀请")
                .setContent("${pkInvitation.receiver.nick} 邀请你PK，是否接受")
                .setNegativeText("拒绝")
                .setPositiveText("接受")
                .setListener(object : FinalDialogFragment.BaseDialogListener() {
                    override fun onDialogPositiveClick(dialog: DialogFragment, any: Any) {
                        super.onDialogPositiveClick(dialog, any)
                        client!!.getService(QPKService::class.java)
                            .invitationHandler.accept(pkInvitation.invitationID, null,
                                object :
                                    QLiveCallBack<Void> {
                                    override fun onError(code: Int, msg: String?) {
                                        msg?.asToast()
                                    }

                                    override fun onSuccess(data: Void?) {
                                    }
                                })
                    }

                    override fun onDialogNegativeClick(dialog: DialogFragment, any: Any) {
                        super.onDialogNegativeClick(dialog, any)
                        client!!.getService(QPKService::class.java)
                            .invitationHandler.reject(pkInvitation.invitationID, null,
                                object :
                                    QLiveCallBack<Void> {
                                    override fun onError(code: Int, msg: String?) {
                                        // msg?.asToast()
                                    }

                                    override fun onSuccess(data: Void?) {
                                    }
                                })
                    }
                }
                ).build()
                .show(kitContext!!.fragmentManager, "")
        }

        override fun onApplyCanceled(pkInvitation: QInvitation) {}

        override fun onApplyTimeOut(pkInvitation: QInvitation) {

        }

        override fun onAccept(pkInvitation: QInvitation) {

        }

        override fun onReject(pkInvitation: QInvitation) {
        }
    }

    override fun attachLiveClient(client: QLiveClient) {
        this.client = client
        client.getService(QPKService::class.java).invitationHandler
            .addInvitationHandlerListener(
                mPKInvitationListener
            )
    }

    /**
     * 绑定上下文回调
     */
    override fun attachKitContext(context: QLiveUIKitContext) {
        this.kitContext = context
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            kitContext = null
        }
    }

    /**
     * 房间加入成功回调
     * @param roomInfo 加入哪个房间
     */
    override fun onJoined(roomInfo: QLiveRoomInfo) {
        this.roomInfo = roomInfo
    }

    /**
     * 房间正在进入回调
     */
    override fun onEntering(roomId: String, user: QLiveUser) {
        this.user = user
    }

    /**
     * 当前房间已经离开回调 - 我是观众-离开 我是主播对应关闭房间
     */
    override fun onLeft() {

    }

    /**
     * client销毁回调 == 房间页面将要退出
     */
    override fun onDestroyed() {
        client = null
    }
}