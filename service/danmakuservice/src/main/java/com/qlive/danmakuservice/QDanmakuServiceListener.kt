package com.qlive.danmakuservice

import com.qlive.core.been.QDanmaku

interface QDanmakuServiceListener {
    /**
     * 收到弹幕消息
     */
    fun onReceiveDanmaku(danmaku: QDanmaku)
}