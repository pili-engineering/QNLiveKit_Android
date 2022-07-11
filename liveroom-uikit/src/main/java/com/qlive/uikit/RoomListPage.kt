package com.qlive.uikit

import com.qlive.sdk.QPage

/**
 * Room list page
 */
class RoomListPage : QPage {
    /**
     * 自定义布局 如果需要替换自定义布局
     */
    var customLayoutID = -1
    set(value) {
        field = value
        RoomListActivity.replaceLayoutID = value
    }
}