package com.qlive.avparam

interface QConnectionStatusLister {
    fun onConnectionStatusChanged(state: QRoomConnectionState) //rtc推流链接状态
}

enum class QRoomConnectionState {
    DISCONNECTED, CONNECTING, CONNECTED, RECONNECTING, RECONNECTED
}

