package com.qlive.uikit

import com.qlive.sdk.QPage
import com.qlive.uikit.component.CreateRoomButton
import com.qlive.uikit.component.QView
import com.qlive.uikit.component.RoomListToolbar
import com.qlive.uikit.component.RoomListView

class RoomListPage : QPage {
    /**
     * 自定义布局 如果需要替换自定义布局
     */
    var customLayoutID = -1
    set(value) {
        field = value
        RoomListActivity.replaceLayoutID = value
    }
    var appbar = QView(RoomListToolbar::class.java)
    var roomListView = QView(RoomListView::class.java)
    var createRoomButton = QView(CreateRoomButton::class.java)
}