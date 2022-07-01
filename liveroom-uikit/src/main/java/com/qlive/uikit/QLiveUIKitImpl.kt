package com.qlive.uikit

import android.content.Context
import com.qlive.uikitcore.BeautyComponent
import com.qlive.rtclive.QInnerVideoFrameHook
import com.qlive.uikit.RoomListActivity.Companion.start
import com.qlive.sdk.QLiveUIKit
import com.qlive.sdk.QPage

class QLiveUIKitImpl(val appContext: Context) : QLiveUIKit {

    companion object {
        private val mRoomListPage = RoomListPage()
        private val mRoomPage = RoomPage()
    }

    init {
        QInnerVideoFrameHook.checkHasHooker()
        if (BeautyComponent.isEnable) {
            QInnerVideoFrameHook.mBeautyHooker?.init(appContext)
        }
    }

    override fun <T : QPage> getPage(pageClass: Class<T>): T? {
        val page: T? = when (pageClass.simpleName) {
            RoomListPage::class.simpleName -> mRoomListPage as T?
            RoomPage::class.simpleName -> mRoomPage as T?
            else -> null
        }
        return page
    }

    override fun launch(context: Context) {
        start(context)
    }
}