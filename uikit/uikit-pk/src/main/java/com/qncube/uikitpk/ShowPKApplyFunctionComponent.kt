package com.qncube.uikitpk

import androidx.fragment.app.DialogFragment
import com.qbcube.pkservice.PKInvitation
import com.qbcube.pkservice.QNPKInvitationHandler
import com.qbcube.pkservice.QNPKService
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.QLiveClient
import com.qncube.liveroomcore.asToast
import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.liveroomcore.mode.QNLiveUser
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.QLiveComponent
import com.qncube.uikitcore.dialog.CommonTipDialog
import com.qncube.uikitcore.dialog.FinalDialogFragment


class ShowPKApplyFunctionComponent : QLiveComponent {

    override var client: QLiveClient? = null
    override var roomInfo: QLiveRoomInfo? = null
    override var user: QNLiveUser? = null
    override var kitContext: KitContext? = null

    private val mPKInvitationListener = object : QNPKInvitationHandler.PKInvitationListener {
        override fun onReceivedApply(pkInvitation: PKInvitation) {
            CommonTipDialog.TipBuild()
                .setTittle("PK邀请")
                .setContent("${pkInvitation.receiver.nick} 邀请你PK，是否接受")
                .setNegativeText("拒绝")
                .setPositiveText("接受")
                .setListener(object : FinalDialogFragment.BaseDialogListener() {
                    override fun onDialogPositiveClick(dialog: DialogFragment, any: Any) {
                        super.onDialogPositiveClick(dialog, any)
                        client!!.getService(QNPKService::class.java)
                            .pkInvitationHandler.accept(pkInvitation.invitationId, null,
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
                        client!!.getService(QNPKService::class.java)
                            .pkInvitationHandler.reject(pkInvitation.invitationId, null,
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

        override fun onApplyCanceled(pkInvitation: PKInvitation) {}

        override fun onApplyTimeOut(pkInvitation: PKInvitation) {

        }

        override fun onAccept(pkInvitation: PKInvitation) {

        }

        override fun onReject(pkInvitation: PKInvitation) {
        }
    }

    override fun attachLiveClient(client: QLiveClient) {
        super.attachLiveClient(client)
        client.getService(QNPKService::class.java).pkInvitationHandler.addPKInvitationListener(
            mPKInvitationListener
        )
    }
}