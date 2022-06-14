package com.qncube.uikitlinkmic

import androidx.fragment.app.DialogFragment
import com.qncube.liveroomcore.been.QInvitation
import com.qncube.linkmicservice.QNLinkMicInvitationHandler
import com.qncube.linkmicservice.QLinkMicService
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.QLiveClient
import com.qncube.liveroomcore.asToast
import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.liveroomcore.mode.QNLiveUser
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.QLiveComponent
import com.qncube.uikitcore.dialog.CommonTipDialog
import com.qncube.uikitcore.dialog.FinalDialogFragment

/**
 * 展示连麦邀请弹窗
 */
class ShowLinkMicApplyComponent : QLiveComponent {

    override var client: QLiveClient? = null
    override var roomInfo: QLiveRoomInfo? = null
    override var user: QNLiveUser? = null
    override var kitContext: KitContext? = null

    private val mInvitationListener = object : QNLinkMicInvitationHandler.InvitationListener {
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
                            .linkMicInvitationHandler.accept(qInvitation.invitationId, null,
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
                            .linkMicInvitationHandler.reject(qInvitation.invitationId, null,
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
                .show(kitContext!!.fm, "")
        }

        override fun onApplyCanceled(qInvitation: QInvitation) {}
        override fun onApplyTimeOut(qInvitation: QInvitation) {}
        override fun onAccept(qInvitation: QInvitation) {}
        override fun onReject(qInvitation: QInvitation) {}
    }

    override fun attachKitContext(context: KitContext) {
        super.attachKitContext(context)
        client?.getService(QLinkMicService::class.java)?.linkMicInvitationHandler?.addInvitationLister(
            mInvitationListener
        )
    }
}