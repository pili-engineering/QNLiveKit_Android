package com.qncube.linveroominner;

import com.qncube.liveroomcore.QLiveClient
import java.lang.reflect.Field


fun QLiveClient.getRtc(): Field {
    val field = this.javaClass.getDeclaredField("mRtcLiveRoom")
    field.isAccessible = true
    return field
}

fun QLiveClient.getPlayer(): Field {
    val field = this.javaClass.getDeclaredField("player")
    field.isAccessible = true
    return field
}