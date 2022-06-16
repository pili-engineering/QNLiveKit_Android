package com.qlive.qnim

import android.content.Context
import com.qlive.rtm.RtmManager
import im.floo.floolib.BMXClientType
import im.floo.floolib.BMXLogLevel
import im.floo.floolib.BMXPushEnvironmentType
import im.floo.floolib.BMXSDKConfig
import java.io.File

object QNIMManager {

    val mRtmAdapter by lazy { com.qlive.qnim.QNIMAdapter() }

    fun init(appId: String, context: Context, config: BMXSDKConfig? = null) {
        mRtmAdapter.init(if (config == null) {
            val appPath = context.filesDir.path
            val dataPath = File("$appPath/data_dir")
            val cachePath = File("$appPath/cache_dir")
            dataPath.mkdirs()
            cachePath.mkdirs()
            // 配置sdk config
            BMXSDKConfig(
                BMXClientType.Android, "1", dataPath.absolutePath,
                cachePath.absolutePath, "MaxIM"
            ).apply {
                consoleOutput = true
                logLevel = BMXLogLevel.Debug
                appID = appId
                setEnvironmentType(BMXPushEnvironmentType.Production)
            }
        } else {
            config
        }, context)
        RtmManager.setRtmAdapter(mRtmAdapter)
    }
}