package com.qlive.uikitlinkmic

import android.content.Context
import android.util.AttributeSet
import com.qlive.core.QInvitationHandlerListener
import com.qlive.core.QLiveCallBack
import com.qlive.core.been.QInvitation
import com.qlive.linkmicservice.QLinkMicService
import com.qlive.pkservice.QPKService
import com.qlive.roomservice.QRoomService
import com.qlive.uikitcore.QBaseRoomFrameLayout
import com.qlive.uikitcore.dialog.FinalDialogFragment
import com.qlive.uikitcore.dialog.LoadingDialog
import com.qlive.uikitcore.ext.asToast
import com.qlive.uikitcore.ext.setDoubleCheckClickListener

/**
 * 主播邀请用户连麦按钮
 * 暂时没用到
 */
class AnchorInvitePlayerLinkView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    //连麦邀请监听
    private val mInvitationListener = object : QInvitationHandlerListener {

        override fun onReceivedApply(qInvitation: QInvitation) {}

        override fun onApplyCanceled(qInvitation: QInvitation) {
            MyLinkerInfoDialog.StartLinkStore.isInviting = false
        }

        override fun onApplyTimeOut(qInvitation: QInvitation) {
            MyLinkerInfoDialog.StartLinkStore.isInviting = false
            LoadingDialog.cancelLoadingDialog()
        }

        override fun onAccept(qInvitation: QInvitation) {
            "${qInvitation.receiver?.nick}接受了连麦".asToast(context)
            LoadingDialog.cancelLoadingDialog()
        }

        override fun onReject(qInvitation: QInvitation) {
            "${qInvitation.receiver?.nick}拒绝了连麦".asToast(context)
            LoadingDialog.cancelLoadingDialog()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_view_start_link
    }

    override fun initView() {
        client!!.getService(QLinkMicService::class.java).invitationHandler.addInvitationHandlerListener(
            mInvitationListener
        )
        this.setDoubleCheckClickListener {
            if (roomInfo == null || client == null || user == null) {
                return@setDoubleCheckClickListener
            }
            //已经有连麦者了
            if ((client?.getService(QLinkMicService::class.java)?.allLinker?.size ?: 0) >= 2) {
                return@setDoubleCheckClickListener
            }
            //主播PK中 不准申请
            if (client?.getService(QPKService::class.java)?.currentPKingSession() != null) {
                "主播pk中".asToast(context)
                return@setDoubleCheckClickListener
            }
        }
        OnlineLinkableUserDialog(client!!.getService(QRoomService::class.java)).apply {
            setInviteCall { user ->
                client!!.getService(QLinkMicService::class.java).invitationHandler.apply(10 * 1000,
                    roomInfo!!.liveID,
                    user.userId,
                    null,
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
        }.show(kitContext!!.fragmentManager, "")
    }
}