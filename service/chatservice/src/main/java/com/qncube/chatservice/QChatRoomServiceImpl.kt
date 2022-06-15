package com.qncube.chatservice

import com.niucube.rtm.*
import com.qiniu.droid.imsdk.QNIMClient
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.service.BaseService
import im.floo.floolib.BMXErrorCode
import im.floo.floolib.BMXGroup
import im.floo.floolib.BMXGroupServiceListener
import im.floo.floolib.ListOfLongLong
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class QChatRoomServiceImpl : BaseService(),
    QChatRoomService {

    private val mC2CRtmMsgListener = object : RtmMsgListener {
        /**
         * 收到消息
         * @return 是否继续分发
         */
        override fun onNewMsg(msg: String, fromID: String, toID: String): Boolean {
            return if (msg.optAction() == "") {
                mChatServiceListeners.forEach {
                    it.onReceivedC2CMsg(msg, fromID, toID)
                }
                true
            } else {
                false
            }
        }
    }

    private val mGroupRtmMsgListener = object : RtmMsgListener {
        /**
         * 收到消息
         * @return 是否继续分发
         */
        override fun onNewMsg(msg: String, fromID: String, toID: String): Boolean {
            return if (msg.optAction() == "") {
                mChatServiceListeners.forEach {
                    it.onReceivedGroupMsg(msg, fromID, toID)
                }
                true
            } else {
                false
            }
        }
    }

    private val mBMXGroupServiceListener = object : BMXGroupServiceListener() {
        override fun onAdminsAdded(group: BMXGroup, members: ListOfLongLong) {
            super.onAdminsAdded(group, members)
            if (group.groupId().toString() != currentRoomInfo?.chatId) {
                return
            }
            for (i in 0 until members.size().toInt()) {
                val id = members[i]
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        mChatServiceListeners.forEach {
                            it.onAdminAdd(id.toString())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        override fun onAdminsRemoved(group: BMXGroup, members: ListOfLongLong, reason: String?) {
            super.onAdminsRemoved(group, members, reason)
            if (group.groupId().toString() != currentRoomInfo?.chatId) {
                return
            }
            for (i in 0 until members.size().toInt()) {
                val id = members[i]
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        mChatServiceListeners.forEach {
                            it.onAdminRemoved(id.toString(), reason ?: "")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        override fun onMemberLeft(group: BMXGroup, memberID: Long, reason: String?) {
            super.onMemberLeft(group, memberID, reason)
            if (group.groupId().toString() != currentRoomInfo?.chatId) {
                return
            }
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    mChatServiceListeners.forEach {
                        it.onUserLeft(memberID.toString())
                    }
                }catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }

        override fun onMemberJoined(group: BMXGroup, memberID: Long, inviter: Long) {
            super.onMemberJoined(group, memberID, inviter)
            if (group.groupId().toString() != currentRoomInfo?.chatId) {
                return
            }

            GlobalScope.launch(Dispatchers.Main) {
                try {
                    mChatServiceListeners.forEach {
                        it.onUserJoin(memberID.toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun onMembersBanned(group: BMXGroup, members: ListOfLongLong, duration: Long) {
            super.onMembersBanned(group, members, duration)
            if (group.groupId().toString() != currentRoomInfo?.chatId) {
                return
            }
            for (i in 0 until members.size().toInt()) {
                val id = members[i]

                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        mChatServiceListeners.forEach {
                            it.onUserBeMuted(true, id.toString(), duration)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        }

        override fun onMembersUnbanned(group: BMXGroup, members: ListOfLongLong) {
            super.onMembersUnbanned(group, members)
            if (group.groupId().toString() != currentRoomInfo?.chatId) {
                return
            }
            for (i in 0 until members.size().toInt()) {
                val id = members[i]

                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        mChatServiceListeners.forEach {
                            it.onUserBeMuted(false, id.toString(),0)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        }
    }

    private val mChatServiceListeners = ArrayList<QChatRoomServiceListener>()
    override fun addChatServiceListener(chatServiceListener: QChatRoomServiceListener) {
        mChatServiceListeners.add(chatServiceListener)
    }

    override fun removeChatServiceListener(chatServiceListener: QChatRoomServiceListener) {
        mChatServiceListeners.remove(chatServiceListener)
    }

    override fun attachRoomClient(client: QLiveClient) {
        super.attachRoomClient(client)
        QNIMClient.getGroupManager().addGroupListener(mBMXGroupServiceListener)
        RtmManager.addRtmC2cListener(mC2CRtmMsgListener)
        RtmManager.addRtmChannelListener(mGroupRtmMsgListener)

    }

    override fun onDestroyed() {
        super.onDestroyed()
        QNIMClient.getGroupManager().removeGroupListener(mBMXGroupServiceListener)
        RtmManager.removeRtmC2cListener(mC2CRtmMsgListener)
        RtmManager.removeRtmChannelListener(mGroupRtmMsgListener)
    }

    /**
     * 发c2c消息
     * @param msg
     * @param memberID
     * @param callBack
     */
    override fun sendCustomC2CMsg(
        msg: String,
        memberID: String,
        callBack: QLiveCallBack<Void>?
    ) {
        RtmManager.rtmClient.sendC2cMsg(msg, memberID, true, object : RtmCallBack {
            override fun onSuccess() {
                callBack?.onSuccess(null)
            }

            override fun onFailure(code: Int, msg: String) {
                callBack?.onError(code, msg)
            }
        })
    }

    /**
     * 发群消息
     * @param msg
     * @param callBack
     */
    override fun sendCustomGroupMsg(msg: String, callBack: QLiveCallBack<Void>?) {
        RtmManager.rtmClient.sendChannelMsg(
            msg,
            currentRoomInfo?.chatId ?: "",
            true,
            object : RtmCallBack {
                override fun onSuccess() {
                    callBack?.onSuccess(null)
                }

                override fun onFailure(code: Int, msg: String) {
                    callBack?.onError(code, msg)
                }
            })
    }

    /**
     * 踢人
     * @param msg
     * @param memberID
     * @param callBack
     */
    override fun kickUser(msg: String, memberID: String, callBack: QLiveCallBack<Void>?) {
        QNIMClient.getGroupManager().getGroupList(
            currentRoomInfo?.chatId?.toLong() ?: 0L, true
        ) { code, data ->
            if (code == BMXErrorCode.NoError) {
                QNIMClient.getGroupManager()
                    .removeMembers(
                        data, ListOfLongLong().apply { add(memberID.toLong()) }, msg
                    ) {
                        if (it == BMXErrorCode.NoError) {
                            callBack?.onSuccess(null)
                        } else {
                            callBack?.onError(it.swigValue(), it.name)
                        }
                    }
            } else {
                callBack?.onError(code.swigValue(), code.name)
            }
        }

    }

    /**
     * 禁言
     * @param isMute
     * @param msg
     * @param memberID
     * @param duration
     * @param callBack
     */
    override fun muteUser(
        isMute: Boolean,
        msg: String,
        memberID: String,
        duration: Long,
        callBack: QLiveCallBack<Void>?
    ) {

        QNIMClient.getGroupManager().getGroupList(
            currentRoomInfo?.chatId?.toLong() ?: 0L, true
        ) { code, data ->
            if (code == BMXErrorCode.NoError) {
                if (isMute) {
                    QNIMClient.getGroupManager()
                        .banMembers(
                            data, ListOfLongLong().apply { add(memberID.toLong()) }, duration, msg
                        ) {
                            if (it == BMXErrorCode.NoError) {
                                callBack?.onSuccess(null)
                            } else {
                                callBack?.onError(it.swigValue(), it.name)
                            }
                        }
                } else {
                    QNIMClient.getGroupManager()
                        .unbanMembers(
                            data, ListOfLongLong().apply { add(memberID.toLong()) }
                        ) {
                            if (it == BMXErrorCode.NoError) {
                                callBack?.onSuccess(null)
                            } else {
                                callBack?.onError(it.swigValue(), it.name)
                            }
                        }
                }

            } else {
                callBack?.onError(code.swigValue(), code.name)
            }
        }
    }

    /**
     * 添加管理员
     * @param memberID
     * @param callBack
     */
    override fun addAdmin(memberID: String, callBack: QLiveCallBack<Void>?) {
        QNIMClient.getGroupManager().getGroupList(
            currentRoomInfo?.chatId?.toLong() ?: 0L, true
        ) { code, data ->
            if (code == BMXErrorCode.NoError) {
                QNIMClient.getGroupManager()
                    .addAdmins(
                        data, ListOfLongLong().apply { add(memberID.toLong()) }, ""
                    ) {
                        if (it == BMXErrorCode.NoError) {
                            callBack?.onSuccess(null)
                        } else {
                            callBack?.onError(it.swigValue(), it.name)
                        }
                    }
            } else {
                callBack?.onError(code.swigValue(), code.name)
            }
        }
    }

    /**
     * 移除管理员
     * @param msg
     * @param memberID
     * @param callBack
     */
    override fun removeAdmin(msg: String, memberID: String, callBack: QLiveCallBack<Void>?) {
        QNIMClient.getGroupManager().getGroupList(
            currentRoomInfo?.chatId?.toLong() ?: 0L, true
        ) { code, data ->
            if (code == BMXErrorCode.NoError) {
                QNIMClient.getGroupManager()
                    .removeAdmins(
                        data, ListOfLongLong().apply { add(memberID.toLong()) }, msg
                    ) {
                        if (it == BMXErrorCode.NoError) {
                            callBack?.onSuccess(null)
                        } else {
                            callBack?.onError(it.swigValue(), it.name)
                        }
                    }
            } else {
                callBack?.onError(code.swigValue(), code.name)
            }
        }
    }

}