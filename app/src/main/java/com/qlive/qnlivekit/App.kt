package com.qlive.qnlivekit

import android.app.Application
import android.util.Log
import com.qlive.sdk.QLive
import com.qlive.uikit.RoomPage
import com.qlive.uikitbeauty.QSenseBeautyComponent
import com.qlive.uiwidghtbeauty.LoadResourcesTask
import com.qlive.uiwidghtbeauty.QSenseTimeManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        QSenseTimeManager.initEffectFromLocalLicense(
            this
        )
        QSenseTimeManager.addSubModelFromAssetsFile("M_SenseME_Face_Extra_5.23.0.model");
        QSenseTimeManager.addSubModelFromAssetsFile("M_SenseME_Iris_2.0.0.model");
        QSenseTimeManager.addSubModelFromAssetsFile("M_SenseME_Hand_5.4.0.model");
        QSenseTimeManager.addSubModelFromAssetsFile("M_SenseME_Segment_4.10.8.model");
        QSenseTimeManager.addSubModelFromAssetsFile("M_SenseAR_Segment_MouthOcclusion_FastV1_1.1.1.model");
        QSenseTimeManager.checkLoadResourcesTask(this, 1,
            object : LoadResourcesTask.ILoadResourcesCallback {
                override fun onStartTask() {
                    Log.d("QSenseTimeManager", "onStartTask" + " ")
                }

                override fun onEndTask(result: Boolean) {
                    Log.d("QSenseTimeManager", "onEndTask  ")
                }
            })
        QLive.getLiveUIKit().getPage(RoomPage::class.java)
            .addFunctionComponent(QSenseBeautyComponent())
    }
}