package com.qlive.core

import com.qlive.core.been.QInvitation

 interface QInvitationHandler {

     /**
      * 发起邀请或者申请
      */
     fun apply(
         expiration: Long,
         receiverRoomID: String,
         receiverUID: String,
         extension: HashMap<String, String>?,
         callBack: QLiveCallBack<QInvitation>?
     )

     /**
      * 取消申请
      */
     fun cancelApply(invitationID: Int, callBack: QLiveCallBack<Void?>?)

     /**
      * 接受
      */
     fun accept(
         invitationID: Int,
         extension: HashMap<String, String>?,
         callBack: QLiveCallBack<Void>?
     )

     /**
      * 拒绝
      */
     fun reject(
         invitationID: Int,
         extension: HashMap<String, String>?,
         callBack: QLiveCallBack<Void>?
     )

     fun removeInvitationHandlerListener(listener: QInvitationHandlerListener) //添加邀请监听

     fun addInvitationHandlerListener(listener: QInvitationHandlerListener)
}