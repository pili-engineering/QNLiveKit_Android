package com.qlive.uikit

import android.content.Context
import com.qlive.uikit.RoomListActivity.Companion.start
import com.qlive.sdk.QLiveUIKit
import com.qlive.sdk.QPage
import com.qlive.uikit.hook.SenseBeautyComponent

class QLiveUIKitImpl(val appContext: Context) : QLiveUIKit {

    companion object{
        private val mRoomListPage = RoomListPage()
        private val mRoomPage = RoomPage()
    }

    init {
        SenseBeautyComponent.init(appContext)
    }

    override fun <T : QPage> getPage(pageClass: Class<T>): T? {
        val page : T?= when (pageClass.simpleName) {
            RoomListPage::class.simpleName-> mRoomListPage as T?
            RoomPage::class.simpleName -> mRoomPage as T?
            else -> null
        }
        return page
    }

    override fun launch(context: Context) {
        start(context)
    }
}