package com.qncube.danmakuservice

interface QDanmakuServiceListener {
    /**
     * 收到弹幕消息
     */
    fun onReceiveDanmaku(danmaku: QDanmaku)
}