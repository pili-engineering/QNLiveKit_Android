package com.qlive.qnlivekit

import android.app.Application
import android.content.Context
import com.qlive.sdk.QLive
import com.qlive.uikit.RoomPage
import com.qlive.uikitbeauty.QSenseBeautyComponent
import com.qlive.uiwidghtbeauty.LoadResourcesTask
import com.qlive.uiwidghtbeauty.QSenseTimeManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        QSenseTimeManager.initEffect(
            this,
            "b9fad8323e8147299a1282fde2abcdd7",
            "d85e0b7d53744e55bb8877fc286d9486"
        )
        QSenseTimeManager.checkLoadResourcesTask(this, 1,
            object : LoadResourcesTask.ILoadResourcesCallback {
                override fun onStartTask() {
                }

                override fun onEndTask(result: Boolean) {
                }
            })
        QLive.getLiveUIKit().getPage(RoomPage::class.java)
            .addFunctionComponent(QSenseBeautyComponent())
    }
}