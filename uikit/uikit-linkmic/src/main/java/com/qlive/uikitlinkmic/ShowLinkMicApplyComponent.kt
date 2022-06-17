package com.qlive.uikitlinkmic

import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qlive.core.been.QInvitation
import com.qlive.linkmicservice.QLinkMicService
import com.qlive.coreimpl.asToast
import com.qlive.core.QInvitationHandlerListener
import com.qlive.core.QLiveCallBack
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikitcore.QLiveComponent
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.dialog.CommonTipDialog
import com.qlive.uikitcore.dialog.FinalDialogFragment

/**
 * 展示连麦邀请弹窗
 */
class ShowLinkMicApplyComponent : QLiveComponent {

    var client: QLiveClient? = null
    var roomInfo: QLiveRoomInfo? = null
    var user: QLiveUser? = null
    var kitContext: QLiveUIKitContext ? = null

    private val mInvitationListener = object : QInvitationHandlerListener {
        override fun onReceivedApply(qInvitation: QInvitation) {
            CommonTipDialog.TipBuild()
                .setTittle("连麦申请")
                .setContent(" ${qInvitation.initiator.nick} 申请连麦是否同意，是否接受")
                .setNegativeText("拒绝")
                .setPositiveText("接受")
                .setListener(object : FinalDialogFragment.BaseDialogListener() {
                    override fun onDialogPositiveClick(dialog: DialogFragment, any: Any) {
                        super.onDialogPositiveClick(dialog, any)
                        client!!.getService(QLinkMicService::class.java)
                            .invitationHandler.accept(qInvitation.invitationID, null,
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
                        client!!.getService(QLinkMicService::class.java)
                            .invitationHandler.reject(qInvitation.invitationID, null,
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

        override fun onApplyCanceled(qInvitation: QInvitation) {}
        override fun onApplyTimeOut(qInvitation: QInvitation) {}
        override fun onAccept(qInvitation: QInvitation) {}
        override fun onReject(qInvitation: QInvitation) {}
    }

    override fun attachLiveClient(client: QLiveClient) {
        this.client = client
        client.getService(QLinkMicService::class.java)?.invitationHandler?.addInvitationHandlerListener(
            mInvitationListener
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