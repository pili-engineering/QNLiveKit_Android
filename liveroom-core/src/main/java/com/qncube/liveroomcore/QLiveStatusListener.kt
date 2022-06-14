package com.qncube.liveroomcore

interface QLiveStatusListener {
    fun onLiveStatusChanged(liveStatus: QLiveStatus) //直播间状态变化 业务状态

}