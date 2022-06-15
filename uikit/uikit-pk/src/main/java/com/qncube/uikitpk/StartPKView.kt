package com.qncube.uikitpk

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.DialogFragment
import com.qbcube.pkservice.*
import com.qncube.linveroominner.asToast
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.been.QExtension
import com.qncube.liveroomcore.been.QInvitation
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.uikitcore.QBaseRoomFrameLayout
import com.qncube.uikitcore.dialog.FinalDialogFragment
import com.qncube.uikitcore.dialog.LoadingDialog
import com.qncube.uikitcore.ext.setDoubleCheckClickListener
import kotlinx.android.synthetic.main.kit_start_pk_view.view.*

class StartPKView : QBaseRoomFrameLayout {

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
            "等待主播 ${pkSession.receiver.nick} 推流超时".asToast()
        }

        override fun onPKExtensionUpdate(pkSession: QPKSession, extension: QExtension) {}
    }

    private val mPKInvitationListener = object : QInvitationHandlerListener {
        override fun onReceivedApply(invitation: QInvitation) {}
        override fun onApplyCanceled(invitation: QInvitation) {}
        override fun onApplyTimeOut(invitation: QInvitation) {
            LoadingDialog.cancelLoadingDialog()
            "邀请主播 ${invitation.receiver.nick} 超时".asToast()
        }

        override fun onAccept(invitation: QInvitation) {
            LoadingDialog.cancelLoadingDialog()
            "主播 ${invitation.receiver.nick} 接受".asToast()
            client?.getService(QPKService::class.java)?.start(20 * 1000,
                invitation.receiverRoomID, invitation.receiver.userId, null,
                object : QLiveCallBack<QPKSession> {
                    override fun onError(code: Int, msg: String) {
                        "开始pk失败 ${msg.asToast()}"
                    }

                    override fun onSuccess(data: QPKSession) {}
                })
            showingPKListDialog?.dismiss()
            showingPKListDialog = null
        }

        override fun onReject(invitation: QInvitation) {
            "主播 ${invitation.receiver.nick} 拒绝".asToast()
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
                        msg?.asToast()
                    }

                    override fun onSuccess(data: Void?) {
                    }
                })
            } else {
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
                showingPKListDialog?.show(kitContext!!.fm, "")
            }
        }
    }

    private fun showInvite(room: QLiveRoomInfo) {
        client!!.getService(QPKService::class.java)
            .invitationHandler
            .apply(10 * 1000, room.liveId, room.anchor.userId, null,
                object : QLiveCallBack<QInvitation> {
                    override fun onError(code: Int, msg: String?) {
                        "邀请失败${msg}".asToast()
                    }

                    override fun onSuccess(data: QInvitation) {
                        "等待对方接受".asToast()
                        LoadingDialog.showLoading(kitContext!!.fm)
                    }
                })
    }

}