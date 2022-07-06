package com.qlive.core

/**
 * 直播状态回调
 */
interface QLiveStatusListener {
    /**
     * 当前直播间状态变更
     * @param liveStatus 状态枚举
     */
    fun onLiveStatusChanged(liveStatus: QLiveStatus) //直播间状态变化 业务状态

}