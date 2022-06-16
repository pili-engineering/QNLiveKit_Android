package com.qlive.core


import com.qlive.rtm.RtmException
import com.qlive.avparam.RtcException

fun Throwable.getCode(): Int {
    if (this is RtmException) {
        return code
    }
    if (this is RtcException) {
        return code
    }
    return -1
}