package com.qncube.uikitpk

import androidx.fragment.app.DialogFragment
import com.qbcube.pkservice.QPKService
import com.qncube.linveroominner.asToast
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.been.QInvitation
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.QLiveComponent
import com.qncube.uikitcore.dialog.CommonTipDialog
import com.qncube.uikitcore.dialog.FinalDialogFragment


class ShowPKApplyFunctionComponent : QLiveComponent {

    override var client: QLiveClient? = null
    override var roomInfo: QLiveRoomInfo? = null
    override var user: QLiveUser? = null
    override var kitContext: KitContext? = null

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
                .show(kitContext!!.fm, "")
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
        super.attachLiveClient(client)
        client.getService(QPKService::class.java).invitationHandler
            .addInvitationHandlerListener(
                mPKInvitationListener
            )
    }
}