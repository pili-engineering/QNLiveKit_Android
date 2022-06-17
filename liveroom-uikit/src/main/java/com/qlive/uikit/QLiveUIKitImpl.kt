package com.qlive.uikit

import android.content.Context
import com.qlive.uikit.RoomListActivity.Companion.start
import com.qlive.sdk.QLiveUIKit
import com.qlive.uikit.RoomListPage
import com.qlive.uikit.RoomPage
import com.qlive.sdk.QPage

class QLiveUIKitImpl : QLiveUIKit {

    private val mRoomListPage = RoomListPage()
    private val mRoomPage = RoomPage()

    override fun <T : QPage> getPage(pageClass: Class<T>): T? {
        when (pageClass.simpleName) {
            RoomListPage::class.simpleName -> mRoomListPage
            RoomPage::class.java.simpleName -> mRoomPage
        }
        return null
    }

    override fun launch(context: Context) {
        start(context)
    }
}