package com.qncube.uikitpk

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qbcube.pkservice.PKInvitation
import com.qbcube.pkservice.QNPKInvitationHandler
import com.qbcube.pkservice.QNPKService
import com.qbcube.pkservice.QNPKSession
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.mode.QLiveRoomInfo
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
    private var mPkSession: QNPKSession? = null

    private val mPKServiceListener = object : QNPKService.PKServiceListener {
        override fun onInitPKer(pkSession: QNPKSession) {}
        override fun onStart(pkSession: QNPKSession) {
            llStartPK.visibility = View.GONE
            tvStopPK.visibility = View.VISIBLE
            mPkSession = pkSession
        }

        override fun onStop(pkSession: QNPKSession, code: Int, msg: String) {
            llStartPK.visibility = View.VISIBLE
            tvStopPK.visibility = View.GONE
            mPkSession = null
        }

        override fun onWaitPeerTimeOut(pkSession: QNPKSession) {
            "等待主播 ${pkSession.receiver.nick} 推流超时".asToast()
        }

        override fun onPKExtensionUpdate(pkSession: QNPKSession, extension: Extension) {}
    }

    private val mPKInvitationListener = object : QNPKInvitationHandler.PKInvitationListener {
        override fun onReceivedApply(pkInvitation: PKInvitation) {}
        override fun onApplyCanceled(pkInvitation: PKInvitation) {}
        override fun onApplyTimeOut(pkInvitation: PKInvitation) {
            LoadingDialog.cancelLoadingDialog()
            "邀请主播 ${pkInvitation.receiver.nick} 超时".asToast()
        }

        override fun onAccept(pkInvitation: PKInvitation) {
            LoadingDialog.cancelLoadingDialog()
            "主播 ${pkInvitation.receiver.nick} 接受".asToast()
            client?.getService(QNPKService::class.java)?.start(20 * 1000,
                pkInvitation.receiverRoomId, pkInvitation.receiver.userId, null,
                object : QLiveCallBack<QNPKSession> {
                    override fun onError(code: Int, msg: String) {
                        "开始pk失败 ${msg.asToast()}"
                    }

                    override fun onSuccess(data: QNPKSession) {}
                })
            showingPKListDialog?.dismiss()
            showingPKListDialog = null
        }

        override fun onReject(pkInvitation: PKInvitation) {
            "主播 ${pkInvitation.receiver.nick} 拒绝".asToast()
            LoadingDialog.cancelLoadingDialog()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_start_pk_view
    }

    override fun initView() {
        client!!.getService(QNPKService::class.java).addPKServiceListener(mPKServiceListener)
        client!!.getService(QNPKService::class.java).pkInvitationHandler.addPKInvitationListener(
            mPKInvitationListener
        )

        flPkBtn.setDoubleCheckClickListener {
            if (mPkSession != null) {
                client?.getService(QNPKService::class.java)?.stop(object :
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
        client!!.getService(QNPKService::class.java)
            .pkInvitationHandler
            .applyJoin(10 * 1000, room.liveId, room.anchorInfo.userId, null,
                object : QLiveCallBack<PKInvitation> {
                    override fun onError(code: Int, msg: String?) {
                        "邀请失败${msg}".asToast()
                    }

                    override fun onSuccess(data: PKInvitation) {
                        "等待对方接受".asToast()
                        LoadingDialog.showLoading(kitContext!!.fm)
                    }
                })
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
        if (event == Lifecycle.Event.ON_DESTROY) {
            client?.getService(QNPKService::class.java)?.removePKServiceListener(mPKServiceListener)
            client?.getService(QNPKService::class.java)?.pkInvitationHandler?.removePKInvitationListener(
                mPKInvitationListener
            )
        }
    }
}