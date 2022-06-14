package com.qncube.playerclient

interface QPlayerEventListener {
    fun onPrepared(preparedTime: Int) //拉流器准备中

    fun onInfo(what: Int, extra: Int) //拉流器信息回调

    fun onBufferingUpdate(percent: Int) //拉流缓冲跟新

    fun onVideoSizeChanged(width: Int, height: Int) //视频尺寸变化回调

    fun onError(errorCode: Int): Boolean
}