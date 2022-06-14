package com.qncube.linkmicservice

import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.been.QInvitation
import java.util.HashMap

/**
 * 邀请处理器
 */
interface QNLinkMicInvitationHandler {
    //邀请监听
    interface InvitationListener {
        fun onReceivedApply(qInvitation: QInvitation)
        fun onApplyCanceled(qInvitation: QInvitation)
        fun onApplyTimeOut(qInvitation: QInvitation)
        fun onAccept(qInvitation: QInvitation)
        fun onReject(qInvitation: QInvitation)
    }

    //注册邀请监听
    fun addInvitationLister(listener: InvitationListener)
    fun removeInvitationLister(listener: InvitationListener)

    /**
     * 邀请/申请
     */
    fun apply(
        expiration: Long,
        receiverRoomId: String,
        receiverUid: String,
        extensions: HashMap<String, String>?,
        callBack: QLiveCallBack<QInvitation>?
    )

    /**
     * 取消申请
     */
    fun cancelApply(invitationId: Int, callBack: QLiveCallBack<Void>?)

    /**
     * 接受连麦
     */
    fun accept(
        invitationId: Int,
        extensions: HashMap<String, String>?,
        callBack: QLiveCallBack<Void>?
    )

    /**
     * 拒绝连麦
     */
    fun reject(
        invitationId: Int,
        extensions: HashMap<String, String>?,
        callBack: QLiveCallBack<Void>?
    )
}