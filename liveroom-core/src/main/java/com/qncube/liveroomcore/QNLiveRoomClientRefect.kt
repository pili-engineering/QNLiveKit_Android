package com.qncube.liveroomcore

import java.lang.reflect.Field


fun QNLiveClient.getRtc(): Field {
    val field = this.javaClass.getDeclaredField("mRtcLiveRoom")
    field.isAccessible = true
    return field
}