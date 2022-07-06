package com.qlive.danmakuservice

import com.qlive.core.been.QDanmaku

/**
 *弹幕消息
 */
interface QDanmakuServiceListener {
    /**
     * 收到弹幕消息
     * @param danmaku 弹幕实体
     */
    fun onReceiveDanmaku(danmaku: QDanmaku)
}