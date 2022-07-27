package com.qlive.uikitlinkmic

import android.content.Context
import android.util.AttributeSet
import com.qlive.core.QInvitationHandlerListener
import com.qlive.core.QLiveCallBack
import com.qlive.core.QLiveClient
import com.qlive.core.been.QInvitation
import com.qlive.linkmicservice.QLinkMicService
import com.qlive.pkservice.QPKService
import com.qlive.roomservice.QRoomService
import com.qlive.uikitcore.QKitFrameLayout
import com.qlive.uikitcore.QKitImageView
import com.qlive.uikitcore.dialog.LoadingDialog
import com.qlive.uikitcore.ext.asToast
import com.qlive.uikitcore.ext.setDoubleCheckClickListener

/**
 * 主播邀请用户连麦按钮
 * 暂时没用到
 */
class AnchorInvitePlayerLinkView : QKitImageView {

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
        }

        override fun onApplyTimeOut(qInvitation: QInvitation) {
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

    override fun attachLiveClient(client: QLiveClient) {
        super.attachLiveClient(client)
        client.getService(QLinkMicService::class.java).invitationHandler.addInvitationHandlerListener(
            mInvitationListener
        )
        this.setDoubleCheckClickListener {
            if (roomInfo == null || user == null) {
                return@setDoubleCheckClickListener
            }
            //主播PK中 不准申请
            if (client.getService(QPKService::class.java)?.currentPKingSession() != null) {
                "主播pk中".asToast(context)
                return@setDoubleCheckClickListener
            }
            if (client.getService(QLinkMicService::class.java).allLinker.size > 1) {
                client.getService(QLinkMicService::class.java).kickOutUser(
                    client.getService(QLinkMicService::class.java).allLinker[1].user.userId,
                    "",
                    null
                )
                return@setDoubleCheckClickListener
            }
            OnlineLinkableUserDialog(client.getService(QRoomService::class.java)).apply {
                setInviteCall { user ->
                    dismiss()
                    client.getService(QLinkMicService::class.java).invitationHandler.apply(10 * 1000,
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
}