package com.qncube.liveuikit

import com.qncube.liveuikit.component.CreateRoomButton
import com.qncube.liveuikit.component.QLiveView
import com.qncube.liveuikit.component.QView
import com.qncube.liveuikit.component.RoomListView

class RoomListPage {
    /**
     * 自定义布局 如果需要替换自定义布局
     */
    var customLayoutID = -1

    var appbar = QView(CreateRoomButton::class.java)

    var roomListView = QView(RoomListView::class.java)

    var createRoomButton = QView(CreateRoomButton::class.java)

}