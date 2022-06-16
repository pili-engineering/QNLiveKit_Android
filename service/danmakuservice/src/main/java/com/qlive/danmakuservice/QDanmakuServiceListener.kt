package com.qlive.danmakuservice

interface QDanmakuServiceListener {
    /**
     * 收到弹幕消息
     */
    fun onReceiveDanmaku(danmaku: QDanmaku)
}