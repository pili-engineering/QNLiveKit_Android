package com.nucube.rtclive

/**
 * 依赖rtc 的插件
 */
interface QRTCLiveProvider {
    /**
     * 获得rtc对象
     */
    var rtcRoomGetter: (() -> QRtcLiveRoom)
}