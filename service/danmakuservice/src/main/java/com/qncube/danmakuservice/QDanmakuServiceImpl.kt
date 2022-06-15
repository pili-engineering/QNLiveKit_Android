package com.qncube.danmakuservice

import com.niucube.rtm.*
import com.niucube.rtm.msg.RtmTextMsg
import com.qiniu.jsonutil.JsonUtils
import com.qncube.linveroominner.BaseService
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.QLiveClient

class QDanmakuServiceImpl : QDanmakuService, BaseService() {
    private val mDanmakuServiceListeners = ArrayList<QDanmakuServiceListener>()
    private val rtmMsgListener = object : RtmMsgListener {
        override fun onNewMsg(msg: String, fromID: String, toID: String): Boolean {
            if (msg.optAction() == QDanmaku.action_danmu) {
                val mode = JsonUtils.parseObject(msg.optData(), QDanmaku::class.java) ?: return true
                mDanmakuServiceListeners.forEach {
                    it.onReceiveDanmaku(mode)
                }
                return true
            }
            return false
        }
    }

    override fun attachRoomClient(client: QLiveClient) {
        super.attachRoomClient(client)
        RtmManager.addRtmChannelListener(rtmMsgListener)
    }

    override fun onDestroyed() {
        super.onDestroyed()
        RtmManager.removeRtmChannelListener(rtmMsgListener)
    }

    override fun addDanmakuServiceListener(listener:QDanmakuServiceListener) {
        mDanmakuServiceListeners.add(listener)
    }

    override fun removeDanmakuServiceListener(listener:QDanmakuServiceListener) {
        mDanmakuServiceListeners.remove(listener)
    }

    /**
     * 发送弹幕消息
     */
    override fun sendDanmaku(
        msg: String,
        extensions: HashMap<String, String>?,
        callBack: QLiveCallBack<QDanmaku>?
    ) {
        val mode = QDanmaku().apply {
            sendUser = user
            content = msg
            senderRoomID = currentRoomInfo?.liveId
            this.extension = extensions
        }
        val rtmMsg = RtmTextMsg<QDanmaku>(
            QDanmaku.action_danmu,
            mode
        )
        RtmManager.rtmClient.sendChannelMsg(rtmMsg.toJsonString(), currentRoomInfo?.chatId ?: "", true,
            object : RtmCallBack {
                override fun onSuccess() {
                    callBack?.onSuccess(mode)
                }

                override fun onFailure(code: Int, msg: String) {
                    callBack?.onError(code, msg)
                }
            })
    }
}