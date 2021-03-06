package com.qlive.rtclive

import android.util.Log
import com.qlive.rtclive.rtc.ExtQNClientEventListener
import com.qiniu.droid.rtc.*
import com.qlive.liblog.QLiveLogUtil

interface DefaultExtQNClientEventListener: ExtQNClientEventListener {
    //private val tag = "QNLiveRoom"
    override fun onLocalPublished(var1: String, var2: List<QNLocalTrack>) {
    }

    override fun onLocalUnpublished(var1: String, var2: List<QNLocalTrack>) {
    }

    override fun onConnectionStateChanged(
        p0: QNConnectionState,
        p1: QNConnectionDisconnectedInfo?
    ) {
    }

    override fun onUserJoined(p0: String, p1: String?) {
    }

    override fun onUserReconnecting(p0: String?) {
    }

    override fun onUserReconnected(p0: String?) {
    }

    override fun onUserLeft(p0: String) {

    }

    override fun onUserPublished(p0: String?, p1: MutableList<QNRemoteTrack>?) {

    }

    override fun onUserUnpublished(p0: String?, p1: MutableList<QNRemoteTrack>?) {

    }

    override fun onSubscribed(
        p0: String?,
        p1: MutableList<QNRemoteAudioTrack>?,
        p2: MutableList<QNRemoteVideoTrack>?
    ) {
    }

    override fun onMessageReceived(p0: QNCustomMessage?) {
    }

    override fun onMediaRelayStateChanged(p0: String?, p1: QNMediaRelayState?) {
    }
}