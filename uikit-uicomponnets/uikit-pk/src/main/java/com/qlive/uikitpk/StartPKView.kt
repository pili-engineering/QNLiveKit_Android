package com.qlive.uikitpk

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.DialogFragment
import com.qlive.pkservice.*
import com.qlive.core.*
import com.qlive.core.been.QInvitation
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.pkservice.QPKSession
import com.qlive.linkmicservice.QLinkMicService
import com.qlive.uikitcore.QKitFrameLayout
import com.qlive.uikitcore.dialog.FinalDialogFragment
import com.qlive.uikitcore.dialog.LoadingDialog
import com.qlive.uikitcore.ext.asToast
import com.qlive.uikitcore.ext.setDoubleCheckClickListener
import kotlinx.android.synthetic.main.kit_start_pk_view.view.*

class StartPKView : QKitFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var showingPKListDialog: PKAbleListDialog? = null
    private var mPkSession: QPKSession? = null

    private val mQPKServiceListener = object :
        QPKServiceListener {

        override fun onStart(pkSession: QPKSession) {
            llStartPK.visibility = View.GONE
            tvStopPK.visibility = View.VISIBLE
            mPkSession = pkSession
        }

        override fun onStop(pkSession: QPKSession, code: Int, msg: String) {
            llStartPK.visibility = View.VISIBLE
            tvStopPK.visibility = View.GONE
            mPkSession = null
        }

        override fun onStartTimeOut(pkSession: QPKSession) {
            "等待主播 ${pkSession.receiver.nick} 推流超时".asToast(context)
        }
    }

    private val mPKInvitationListener = object : QInvitationHandlerListener {
        override fun onReceivedApply(invitation: QInvitation) {}
        override fun onApplyCanceled(invitation: QInvitation) {}
        override fun onApplyTimeOut(invitation: QInvitation) {
            LoadingDialog.cancelLoadingDialog()
            "邀请主播 ${invitation.receiver.nick} 超时".asToast(context)
        }

        override fun onAccept(invitation: QInvitation) {
            LoadingDialog.cancelLoadingDialog()
            "主播 ${invitation.receiver.nick} 接受".asToast(context)
            client?.getService(QPKService::class.java)?.start(20 * 1000,
                invitation.receiverRoomID, invitation.receiver.userId, null,
                object : QLiveCallBack<QPKSession> {
                    override fun onError(code: Int, msg: String) {
                        "开始pk失败 ${msg.asToast(context)}"
                    }

                    override fun onSuccess(data: QPKSession) {}
                })
            showingPKListDialog?.dismiss()
            showingPKListDialog = null
        }

        override fun onReject(invitation: QInvitation) {
            "主播 ${invitation.receiver.nick} 拒绝".asToast(context)
            LoadingDialog.cancelLoadingDialog()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_start_pk_view
    }

    override fun initView() {
        client!!.getService(QPKService::class.java).addServiceListener(mQPKServiceListener)
        client!!.getService(QPKService::class.java).invitationHandler.addInvitationHandlerListener(
            mPKInvitationListener
        )

        flPkBtn.setDoubleCheckClickListener {
            if (mPkSession != null) {
                client?.getService(QPKService::class.java)?.stop(object :
                    QLiveCallBack<Void> {
                    override fun onError(code: Int, msg: String?) {
                        msg?.asToast(context)
                    }

                    override fun onSuccess(data: Void?) {
                    }
                })
            } else {
                if ((client?.getService(QLinkMicService::class.java)?.allLinker?.size ?: 0) > 1) {
                    "当前正在连麦中".asToast(context)
                    return@setDoubleCheckClickListener
                }
                showingPKListDialog = PKAbleListDialog()
                showingPKListDialog?.setInviteCall {
                    showInvite(it)
                }
                showingPKListDialog?.setDefaultListener(object :
                    FinalDialogFragment.BaseDialogListener() {
                    override fun onDismiss(dialog: DialogFragment) {
                        super.onDismiss(dialog)
                        showingPKListDialog = null
                    }
                })
                showingPKListDialog?.show(kitContext!!.fragmentManager, "")
            }
        }
    }

    private fun showInvite(room: QLiveRoomInfo) {
        client!!.getService(QPKService::class.java)
            .invitationHandler
            .apply(10 * 1000, room.liveID, room.anchor.userId, null,
                object : QLiveCallBack<QInvitation> {
                    override fun onError(code: Int, msg: String?) {
                        "邀请失败${msg}".asToast(context)
                    }

                    override fun onSuccess(data: QInvitation) {
                        "等待对方接受".asToast(context)
                        LoadingDialog.showLoading(kitContext!!.fragmentManager)
                    }
                })
    }

}