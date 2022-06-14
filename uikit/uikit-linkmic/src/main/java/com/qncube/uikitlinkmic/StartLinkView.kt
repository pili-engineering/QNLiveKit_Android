package com.qncube.uikitlinkmic

import android.content.Context
import android.util.AttributeSet
import androidx.fragment.app.DialogFragment
import com.qncube.lcommon.QCameraParam
import com.qncube.lcommon.QMicrophoneParam
import com.qncube.liveroomcore.been.QInvitation
import com.qncube.linkmicservice.QNLinkMicInvitationHandler
import com.qncube.linkmicservice.QLinkMicService
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.asToast
import com.qncube.uikitcore.QBaseRoomFrameLayout
import com.qncube.uikitcore.dialog.FinalDialogFragment
import com.qncube.uikitcore.dialog.LoadingDialog

//开始连麦按钮
class StartLinkView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    //连麦邀请监听
    private val mInvitationListener = object : QNLinkMicInvitationHandler.InvitationListener {

        override fun onReceivedApply(qInvitation: QInvitation) {}

        override fun onApplyCanceled(qInvitation: QInvitation) {
            MyLinkerInfoDialog.StartLinkStore.isInviting = false
        }

        override fun onApplyTimeOut(qInvitation: QInvitation) {
            MyLinkerInfoDialog.StartLinkStore.isInviting = false
            LoadingDialog.cancelLoadingDialog()
        }

        override fun onAccept(qInvitation: QInvitation) {
            MyLinkerInfoDialog.StartLinkStore.isInviting = false
            LoadingDialog.cancelLoadingDialog()
            "主播同意你的申请".asToast()
            client?.getService(QLinkMicService::class.java)
                ?.audienceMicLinker
                ?.startLink(
                    null, if (MyLinkerInfoDialog.StartLinkStore.isVideoLink) {
                        QCameraParam()
                    } else {
                        null
                    }, QMicrophoneParam(),
                    object : QLiveCallBack<Void> {
                        override fun onError(code: Int, msg: String?) {
                            msg?.asToast()
                        }

                        override fun onSuccess(data: Void?) {
                            MyLinkerInfoDialog.StartLinkStore.startTime = System.currentTimeMillis()
                        }
                    }
                )
        }

        override fun onReject(qInvitation: QInvitation) {
            MyLinkerInfoDialog.StartLinkStore.isInviting = false
            "主播拒绝你的连麦申请".asToast()
            LoadingDialog.cancelLoadingDialog()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_view_start_link
    }

    override fun initView() {
        client!!.getService(QLinkMicService::class.java).linkMicInvitationHandler.addInvitationLister(
            mInvitationListener
        )
        this.setOnClickListener {
            if (roomInfo == null || client == null || user == null) {
                return@setOnClickListener
            }

            if (MyLinkerInfoDialog.StartLinkStore.isInviting) {
                "正在申请中，请稍后".asToast()
                return@setOnClickListener
            }
            if (client?.getService(QLinkMicService::class.java)?.audienceMicLinker?.isLinked() == true) {
                MyLinkerInfoDialog(client!!.getService(QLinkMicService::class.java), user!!).show(
                    kitContext!!.fm,
                    ""
                )
                return@setOnClickListener
            }

            LinkApplyDialog().apply {
                mDefaultListener = object : FinalDialogFragment.BaseDialogListener() {
                    override fun onDialogPositiveClick(dialog: DialogFragment, any: Any) {
                        super.onDialogPositiveClick(dialog, any)
                        with(LoadingDialog) { showLoading(kitContext!!.fm) }
                        MyLinkerInfoDialog.StartLinkStore.isVideoLink = any as Boolean
                        client!!.getService(QLinkMicService::class.java).linkMicInvitationHandler.apply(
                            15 * 1000,
                            roomInfo!!.liveId,
                            roomInfo!!.anchorInfo.userId,
                            null,
                            object :
                                QLiveCallBack<QInvitation> {
                                override fun onError(code: Int, msg: String?) {
                                    msg?.asToast()
                                    LoadingDialog.cancelLoadingDialog()
                                }

                                override fun onSuccess(data: QInvitation) {
                                    MyLinkerInfoDialog.StartLinkStore.isInviting = true
                                    "等待主播同意".asToast()
                                }
                            }
                        )
                    }
                }
            }.show(kitContext!!.fm, "")
        }
    }

}