package com.qncube.linkmicservice

import com.niucube.rtm.RtmCallBack
import com.niucube.rtminvitation.*
import com.qiniu.jsonutil.JsonUtils
import com.qncube.linveroominner.backGround
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.been.QInvitation
import com.qncube.liveroomcore.service.BaseService
import java.util.*
import kotlin.collections.HashMap

class QLinkMicInvitationHandlerImpl : QNLinkMicInvitationHandler, BaseService() {

    private val listeners = LinkedList<QNLinkMicInvitationHandler.InvitationListener>()
    private val invitationMap = HashMap<Int, Invitation>()

    private val mInvitationProcessor =
        InvitationProcessor("liveroom-linkmic-invitation",
            object : InvitationCallBack {

                override fun onReceiveInvitation(invitation: Invitation) {

                    val qInvitation =
                        JsonUtils.parseObject(invitation.msg, QInvitation::class.java) ?: return

                    if (qInvitation.receiverRoomId != roomInfo?.liveId) {
                        return
                    }
                    qInvitation.invitationId = invitation.flag
                    invitationMap[invitation.flag] = invitation
                    listeners.forEach { it.onReceivedApply(qInvitation) }
                }

                override fun onInvitationTimeout(invitation: Invitation) {
                    val qInvitation =
                        JsonUtils.parseObject(invitation.msg, QInvitation::class.java) ?: return

                    invitationMap.remove(invitation.flag)
                    if (qInvitation.initiatorRoomId != roomInfo?.liveId) {
                        return
                    }
                    listeners.forEach { it.onApplyTimeOut(qInvitation) }
                    qInvitation.invitationId = invitation.flag

                }

                override fun onReceiveCanceled(invitation: Invitation) {
                    val qInvitation =
                        JsonUtils.parseObject(invitation.msg, QInvitation::class.java) ?: return

                    qInvitation.invitationId = invitation.flag
                    invitationMap.remove(invitation.flag)
                    if (qInvitation.receiverRoomId != roomInfo?.liveId) {
                        return
                    }
                    listeners.forEach { it.onApplyCanceled(qInvitation) }

                }

                override fun onInviteeAccepted(invitation: Invitation) {
                    val qInvitation =
                        JsonUtils.parseObject(invitation.msg, QInvitation::class.java) ?: return
                    qInvitation.invitationId = invitation.flag
                    invitationMap.remove(invitation.flag)
                    if (qInvitation.initiatorRoomId != roomInfo?.liveId) {
                        return
                    }
                    listeners.forEach { it.onAccept(qInvitation) }

                }

                override fun onInviteeRejected(invitation: Invitation) {
                    val qInvitation =
                        JsonUtils.parseObject(invitation.msg, QInvitation::class.java) ?: return
                    qInvitation.invitationId = invitation.flag
                    invitationMap.remove(invitation.flag)
                    if (qInvitation.initiatorRoomId != roomInfo?.liveId) {
                        return
                    }

                    listeners.forEach { it.onReject(qInvitation) }

                }
            })

    override fun addInvitationLister(listener: QNLinkMicInvitationHandler.InvitationListener) {
        listeners.add(listener)
    }

    override fun removeInvitationLister(listener: QNLinkMicInvitationHandler.InvitationListener) {
        listeners.remove(listener)
    }

    /**
     * 邀请/申请
     */
    override fun apply(
        expiration: Long,
        receiverRoomId: String,
        receiverUid: String,
        extensions: HashMap<String, String>?,
        callBack: QLiveCallBack<QInvitation>?
    ) {
        if (roomInfo == null) {
            callBack?.onError(0, "roomInfo == null)")
            return
        }
        backGround {
            doWork {
                val receiver =
                    com.qncube.linveroominner.UserDataSource().searchUserByUserId(receiverUid)

                val qInvitation = QInvitation()
                qInvitation.linkType = 1
                qInvitation.initiator = user
                qInvitation.initiatorRoomId = roomInfo?.liveId
                qInvitation.extensions = extensions
                qInvitation.receiver = receiver
                qInvitation.receiverRoomId = receiverRoomId
                val channel = if (receiverRoomId == roomInfo!!.liveId) {
                    roomInfo!!.chatId
                } else {
                    ""
                }
                val iv = mInvitationProcessor.suspendInvite(
                    JsonUtils.toJson(qInvitation),
                    receiver.imUid, channel, expiration
                )
                qInvitation.invitationId = iv.flag
                invitationMap[iv.flag] = iv
                callBack?.onSuccess(qInvitation)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    /**
     * 取消申请
     */
    override fun cancelApply(invitationId: Int, callBack: QLiveCallBack<Void>?) {
        mInvitationProcessor.cancel(invitationMap[invitationId], object : RtmCallBack {
            override fun onSuccess() {
                invitationMap.remove(invitationId)
                callBack?.onSuccess(null)
            }

            override fun onFailure(code: Int, msg: String) {
                callBack?.onError(code, msg)
            }
        })
    }

    /**
     * 接受连麦
     */
    override fun accept(
        invitationId: Int,
        extensions: HashMap<String, String>?,
        callBack: QLiveCallBack<Void>?
    ) {
        val invitation = invitationMap[invitationId]
        if (invitation == null) {
            callBack?.onError(-1, "invitation==null")
            return
        }
        val qInvitation =
            JsonUtils.parseObject(invitation.msg, QInvitation::class.java) ?: return
        extensions?.entries?.forEach {
            qInvitation.extensions[it.key] = it.value
        }
        invitation.msg = JsonUtils.toJson(qInvitation)
        mInvitationProcessor.accept(invitation, object : RtmCallBack {
            override fun onSuccess() {
                invitationMap.remove(invitationId)
                callBack?.onSuccess(null)
            }

            override fun onFailure(code: Int, msg: String) {
                callBack?.onError(code, msg)
            }
        })

    }

    /**
     * 拒绝连麦
     */
    override fun reject(
        invitationId: Int,
        extensions: HashMap<String, String>?,
        callBack: QLiveCallBack<Void>?
    ) {
        val invitation = invitationMap[invitationId]
        if (invitation == null) {
            callBack?.onError(-1, "invitation==null")
            return
        }
        val qInvitation =
            JsonUtils.parseObject(invitation.msg, QInvitation::class.java) ?: return
        extensions?.entries?.forEach {
            qInvitation.extensions[it.key] = it.value
        }
        invitation.msg = JsonUtils.toJson(qInvitation)
        mInvitationProcessor.reject(invitation, object : RtmCallBack {
            override fun onSuccess() {
                invitationMap.remove(invitationId)
                callBack?.onSuccess(null)
            }

            override fun onFailure(code: Int, msg: String) {
                callBack?.onError(code, msg)
            }
        })
    }


    override fun onDestroyed() {
        super.onDestroyed()
        InvitationManager.removeInvitationProcessor(mInvitationProcessor)
    }

    override fun attachRoomClient(client: QLiveClient) {
        super.attachRoomClient(client)
        InvitationManager.addInvitationProcessor(mInvitationProcessor)
    }

}