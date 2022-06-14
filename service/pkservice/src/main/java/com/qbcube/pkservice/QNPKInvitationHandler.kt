package com.qbcube.pkservice

import com.qncube.liveroomcore.QLiveCallBack

/**
 * pk邀请器
 */
interface QNPKInvitationHandler {

    interface PKInvitationListener {
        fun onReceivedApply(pkInvitation: PKInvitation)
        fun onApplyCanceled(pkInvitation: PKInvitation)
        fun onApplyTimeOut(pkInvitation: PKInvitation)
        fun onAccept(pkInvitation: PKInvitation)
        fun onReject(pkInvitation: PKInvitation)
    }

    fun addPKInvitationListener(listener: PKInvitationListener)

    fun removePKInvitationListener(listener: PKInvitationListener)

    /**
     * 申请pk
     */
    fun applyJoin(
        expiration: Long,
        receiverRoomId: String,
        receiverUid: String,
        extensions: HashMap<String,String>? = null,
        callBack: QLiveCallBack<PKInvitation>?
    )

    /**
     * 取消申请
     */
    fun cancelApply(invitationId: Int,callBack: QLiveCallBack<Void>?)

    /**
     * 接受连麦
     */
    fun accept(
        invitationId: Int,
        extensions: HashMap<String,String>? = null,
        callBack: QLiveCallBack<Void>?
    )

    /**
     * 拒绝连麦
     */
    fun reject(
        invitationId: Int,
        extensions: HashMap<String,String>? = null,
        callBack: QLiveCallBack<Void>?
    )
}
