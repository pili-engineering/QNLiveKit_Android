package com.qlive.uiwidghtbeauty

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.qiniu.sensetimeplugin.QNSenseTimePlugin
import com.qlive.sensebeautyservice.SenseBeautyServiceManager
import com.qlive.sensebeautyservice.SenseBeautyServiceManager.Companion.sSenseTimePlugin
import com.qlive.sensebeautyservice.SenseBeautyServiceManager.Companion.sSubModelFromAssetsFile
import com.qlive.uiwidghtbeauty.QSenseTimeManager
import com.qlive.uiwidghtbeauty.LoadResourcesTask.ILoadResourcesCallback
import com.qlive.uiwidghtbeauty.utils.SharedPreferencesUtils
import com.qlive.uiwidghtbeauty.LoadResourcesTask
import com.qlive.uiwidghtbeauty.utils.Constants

object QSenseTimeManager {

    var sSenseTimePlugin: QNSenseTimePlugin? = null
    private const val DST_FOLDER = "resource"
    var sAppContext: Context? = null
    var isAuthorized = false
    fun initEffectFromLocalLicense(appContext: Context) {
        sAppContext = appContext
        sSenseTimePlugin = QNSenseTimePlugin.Builder(appContext)
            .setLicenseAssetPath(Constants.LICENSE_FILE)
            .setModelActionAssetPath("M_SenseME_Face_Video_5.3.3.model")
            .setCatFaceModelAssetPath("M_SenseME_CatFace_3.0.0.model")
            .setDogFaceModelAssetPath("M_SenseME_DogFace_2.0.0.model") // 关闭在线拉取授权文件，使用离线授权文件
            .setOnlineLicense(false) // 关闭在线激活授权，使用离线激活授权
            .setOnlineActivate(false)
            .build()
        isAuthorized = sSenseTimePlugin?.checkLicense() ?: false
        if (!isAuthorized) {
            Toast.makeText(appContext, "鉴权失败，请检查授权文件", Toast.LENGTH_SHORT).show()
        } else {
            //绑定美颜插件
            SenseBeautyServiceManager.sSenseTimePlugin = sSenseTimePlugin
            sSubModelFromAssetsFile.add("M_SenseME_Face_Extra_5.23.0.model")
            sSubModelFromAssetsFile.add("M_SenseME_Iris_2.0.0.model")
            sSubModelFromAssetsFile.add("M_SenseME_Hand_5.4.0.model")
            sSubModelFromAssetsFile.add("M_SenseME_Segment_4.10.8.model")
            sSubModelFromAssetsFile.add("M_SenseAR_Segment_MouthOcclusion_FastV1_1.1.1.model")
        }
        checkLoadResourcesTask(appContext,
            object : LoadResourcesTask.ILoadResourcesCallback {
                override fun getContext(): Context {
                    return sAppContext!!
                }

                override fun onStartTask() {
                    Log.d("QSenseTimeManager", "onStartTask" + " ");
                }
                override fun onEndTask(result: Boolean) {
                    Log.d("QSenseTimeManager", "onStartTask" + " ");
                }
            });
        Log.d("QSenseTimeManager", "authorizeWithAppId" + "鉴权onSuccess ")
    }

    private fun checkLoadResourcesTask(
        context: Context,
        callback: ILoadResourcesCallback
    ) {
        SharedPreferencesUtils.resVersion = resourceVersion
        if (!SharedPreferencesUtils.resourceReady(context)) {
            callback.onStartTask()
            callback.onEndTask(true)
        } else {
            val mTask = LoadResourcesTask(callback)
            mTask.execute(DST_FOLDER)
        }
    }
}