package com.qlive.uikit

import com.qlive.sdk.QPage
import com.qlive.uikit.component.CreateRoomButton
import com.qlive.uikit.component.QView
import com.qlive.uikit.component.RoomListToolbar
import com.qlive.uikit.component.RoomListView

/**
 * 房间列表页面
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

    /**
     * toolbar
     */
    var appbar = QView(RoomListToolbar::class.java)

    /**
     * 房间列表
     */
    var roomListView = QView(RoomListView::class.java)

    /**
     * 创建房间按钮
     */
    var createRoomButton = QView(CreateRoomButton::class.java)
}