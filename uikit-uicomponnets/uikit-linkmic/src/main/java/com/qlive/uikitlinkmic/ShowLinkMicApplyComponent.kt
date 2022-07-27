package com.qlive.uikitlinkmic

import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.qlive.core.been.QInvitation
import com.qlive.linkmicservice.QLinkMicService
import com.qlive.core.QInvitationHandlerListener
import com.qlive.core.QLiveCallBack
import com.qlive.core.QLiveClient
import com.qlive.uikitcore.BaseQLiveComponent
import com.qlive.uikitcore.dialog.CommonTipDialog
import com.qlive.uikitcore.dialog.FinalDialogFragment

/**
 * 主播监听连麦申请
 * 展示连麦邀请弹窗
 */
class ShowLinkMicApplyComponent : BaseQLiveComponent() {

    private val mApplyingDialogs = HashMap<Int, DialogFragment>()

    private val mInvitationListener = object : QInvitationHandlerListener {
        override fun onReceivedApply(qInvitation: QInvitation) {
            if (user?.userId != roomInfo?.anchor?.userId) {
                return
            }
            CommonTipDialog.TipBuild()
                .setTittle("连麦申请")
                .setContent(" ${qInvitation.initiator.nick} 申请连麦，是否接受").setNegativeText("拒绝")
                .setPositiveText("接受")
                .setListener(object : FinalDialogFragment.BaseDialogListener() {
                    override fun onDialogPositiveClick(dialog: DialogFragment, any: Any) {
                        super.onDialogPositiveClick(dialog, any)
                        client!!.getService(QLinkMicService::class.java)
                            .invitationHandler.accept(qInvitation.invitationID, null,
                                object :
                                    QLiveCallBack<Void> {
                                    override fun onError(code: Int, msg: String?) {
                                        Toast.makeText(
                                            kitContext!!.androidContext,
                                            msg,
                                            Toast.LENGTH_SHORT
                                        ).show()
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

                    override fun onDismiss(dialog: DialogFragment) {
                        super.onDismiss(dialog)
                        mApplyingDialogs.remove(qInvitation.invitationID)
                    }
                }
                ).build().apply {
                    mApplyingDialogs.put(qInvitation.invitationID, this)
                }
                .show(kitContext!!.fragmentManager, "")
        }

        override fun onApplyCanceled(qInvitation: QInvitation) {
            mApplyingDialogs.get(qInvitation.invitationID)?.dismiss()
            mApplyingDialogs.remove(qInvitation.invitationID)
        }

        override fun onApplyTimeOut(qInvitation: QInvitation) {
            mApplyingDialogs.get(qInvitation.invitationID)?.dismiss()
            mApplyingDialogs.remove(qInvitation.invitationID)
        }

        override fun onAccept(qInvitation: QInvitation) {}
        override fun onReject(qInvitation: QInvitation) {}
    }

    override fun attachLiveClient(client: QLiveClient) {
        super.attachLiveClient(client)
        client.getService(QLinkMicService::class.java)?.invitationHandler?.addInvitationHandlerListener(
            mInvitationListener
        )
    }

    override fun onDestroyed() {
        super.onDestroyed()
        mApplyingDialogs.clear()
    }
}