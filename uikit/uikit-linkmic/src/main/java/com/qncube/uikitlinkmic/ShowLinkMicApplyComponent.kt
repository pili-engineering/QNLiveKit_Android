package com.qncube.uikitlinkmic

import androidx.fragment.app.DialogFragment
import com.qncube.linkmicservice.LinkInvitation
import com.qncube.linkmicservice.QNLinkMicInvitationHandler
import com.qncube.linkmicservice.QNLinkMicService
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.QNLiveClient
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

    override var client: QNLiveClient? = null
    override var roomInfo: QLiveRoomInfo? = null
    override var user: QNLiveUser? = null
    override var kitContext: KitContext? = null

    private val mInvitationListener = object : QNLinkMicInvitationHandler.InvitationListener {
        override fun onReceivedApply(linkInvitation: LinkInvitation) {
            CommonTipDialog.TipBuild()
                .setTittle("连麦申请")
                .setContent(" ${linkInvitation.initiator.nick} 申请连麦是否同意，是否接受")
                .setNegativeText("拒绝")
                .setPositiveText("接受")
                .setListener(object : FinalDialogFragment.BaseDialogListener() {
                    override fun onDialogPositiveClick(dialog: DialogFragment, any: Any) {
                        super.onDialogPositiveClick(dialog, any)
                        client!!.getService(QNLinkMicService::class.java)
                            .linkMicInvitationHandler.accept(linkInvitation.invitationId, null,
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
                        client!!.getService(QNLinkMicService::class.java)
                            .linkMicInvitationHandler.reject(linkInvitation.invitationId, null,
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

        override fun onApplyCanceled(linkInvitation: LinkInvitation) {}
        override fun onApplyTimeOut(linkInvitation: LinkInvitation) {}
        override fun onAccept(linkInvitation: LinkInvitation) {}
        override fun onReject(linkInvitation: LinkInvitation) {}
    }

    override fun attachKitContext(context: KitContext) {
        super.attachKitContext(context)
        client?.getService(QNLinkMicService::class.java)?.linkMicInvitationHandler?.addInvitationLister(
            mInvitationListener
        )
    }
}