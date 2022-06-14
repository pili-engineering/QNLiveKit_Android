package com.qncube.liveroomcore

enum class QLiveStatus(var tipMsg: String) {
    PREPARE("房间已创建"),
    ON("房间已发布"),
    ANCHOR_ONLINE("主播上线"),
    ANCHOR_OFFLINE("主播已离线"),
    OFF("房间已关闭")
}

fun Int.roomStatusToLiveStatus(): QLiveStatus {
    return when (this) {
        0 -> QLiveStatus.PREPARE
        1 -> QLiveStatus.ON
        else -> QLiveStatus.OFF
    }
}

fun Int.anchorStatusToLiveStatus(): QLiveStatus {
    return when (this) {
        1 -> QLiveStatus.ANCHOR_ONLINE
        0 -> QLiveStatus.ANCHOR_OFFLINE
        else -> QLiveStatus.ANCHOR_OFFLINE
    }
}