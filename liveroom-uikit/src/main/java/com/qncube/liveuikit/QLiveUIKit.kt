package com.qncube.liveuikit

import android.content.Context

public class QLiveUIKit private constructor() {

    companion object {
        val liveUIKitInstance = QLiveUIKit()
    }

    private var mRoomListPage = RoomListPage()
    private var mRoomPage = RoomPage()

    /**
     * 房间列表页面 -ui组件表
     */
    fun getRoomListPage(): RoomListPage {
        return mRoomListPage
    }

    /**
     * 房间页面 - ui组件表
     */
    fun getRoomPage(): RoomPage {
        return mRoomPage
    }

    /**
     * 启动 跳转直播列表页面
     */
    fun launch(context: Context) {
        RoomListActivity.start(context)
    }

}