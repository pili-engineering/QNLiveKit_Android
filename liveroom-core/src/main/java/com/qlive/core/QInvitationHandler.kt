package com.qlive.core

import com.qlive.core.been.QInvitation

/**
 * 邀请处理器
 *
 */
interface QInvitationHandler {

    /**
     * 发起邀请/申请
     *
     * @param expiration 过期时间 单位毫秒 过期后不再响应
     * @param receiverRoomID 接收方所在房间ID
     * @param receiverUID   接收方用户ID
     * @param extension     扩展字段
     * @param callBack   回调函数
     */
    fun apply(
        expiration: Long,
        receiverRoomID: String,
        receiverUID: String,
        extension: HashMap<String, String>?,
        callBack: QLiveCallBack<QInvitation>?
    )

    /**
     * 取消邀请/申请
     *
     * @param invitationID 邀请ID
     * @param callBack
     */
    fun cancelApply(invitationID: Int, callBack: QLiveCallBack<Void?>?)

    /**
     * 接受对方的邀请/申请
     *
     * @param invitationID 邀请ID
     * @param extension    扩展字段
     * @param callBack
     */
    fun accept(
        invitationID: Int,
        extension: HashMap<String, String>?,
        callBack: QLiveCallBack<Void>?
    )

    /**
     * 拒绝对方
     *
     * @param invitationID 邀请ID
     * @param extension 扩展字段
     * @param callBack
     */
    fun reject(
        invitationID: Int,
        extension: HashMap<String, String>?,
        callBack: QLiveCallBack<Void>?
    )

    /**
     *移除监听
     *
     * @param listener
     */
    fun removeInvitationHandlerListener(listener: QInvitationHandlerListener) //添加邀请监听

    /**
     *添加监听
     *
     * @param listener
     */
    fun addInvitationHandlerListener(listener: QInvitationHandlerListener)
}