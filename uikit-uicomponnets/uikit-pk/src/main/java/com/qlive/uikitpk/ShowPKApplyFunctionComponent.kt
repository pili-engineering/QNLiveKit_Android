package com.qlive.uikitpk

import androidx.fragment.app.DialogFragment
import com.qlive.pkservice.QPKService
import com.qlive.core.*
import com.qlive.core.been.QInvitation
import com.qlive.linkmicservice.QLinkMicService
import com.qlive.uikitcore.BaseQLiveComponent
import com.qlive.uikitcore.dialog.CommonTipDialog
import com.qlive.uikitcore.dialog.FinalDialogFragment
import com.qlive.uikitcore.ext.asToast


class ShowPKApplyFunctionComponent : BaseQLiveComponent() {

    private var isShowTip = false

    private val mPKInvitationListener = object : QInvitationHandlerListener {
        //收到邀请
        override fun onReceivedApply(pkInvitation: QInvitation) {

            if ((client?.getService(QLinkMicService::class.java)?.allLinker?.size ?: 0) > 1) {
                if (!isShowTip) {
                    CommonTipDialog.TipBuild()
                        .setTittle("提示")
                        .setContent("${pkInvitation.initiator.nick} 邀请你PK，连麦中无法PK已为你自动拒绝～")
                        .setPositiveText("我知道了")
                        .isNeedCancelBtn(false)
                        .build()
                        .show(kitContext!!.fragmentManager, "")
                    isShowTip = true
                }

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
                return
            }

            if (client?.getService(QPKService::class.java)?.currentPKingSession() != null) {
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
                return
            }

            CommonTipDialog.TipBuild()
                .setTittle("PK邀请")
                .setContent("${pkInvitation.initiator.nick} 邀请你PK，是否接受")
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
                                        msg?.asToast(kitContext?.androidContext)
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
        super.attachLiveClient(client)
        client.getService(QPKService::class.java).invitationHandler
            .addInvitationHandlerListener(
                mPKInvitationListener
            )
    }

}