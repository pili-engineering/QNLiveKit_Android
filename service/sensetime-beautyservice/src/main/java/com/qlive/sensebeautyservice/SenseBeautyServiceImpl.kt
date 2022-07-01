package com.qlive.sensebeautyservice

import com.qiniu.droid.rtc.QNVideoFrameListener
import com.qiniu.droid.rtc.QNVideoFrameType
import com.qlive.core.QClientType
import com.qlive.core.QLiveClient
import com.qlive.coreimpl.BaseService
import com.qlive.rtclive.QRTCProvider
import com.qlive.sensebeautyservice.SenseBeautyServiceManager.Companion.sSenseTimePlugin
import com.qlive.sensebeautyservice.SenseBeautyServiceManager.Companion.sSubModelFromAssetsFile
import com.qlive.sensebeautyservice.SenseBeautyServiceManager.Companion.sSubModelFromFile

class SenseBeautyServiceImpl : SenseBeautyService, BaseService() {

    private var isInit = false
    private var mRotation = -1
    private val mVideoFrameListener = object : QNVideoFrameListener {
        override fun onYUVFrameAvailable(
            data: ByteArray,
            type: QNVideoFrameType,
            width: Int,
            height: Int,
            rotation: Int,
            timestampNs: Long
        ) {
        }

        private fun checkInit(): Boolean {
            if (SenseBeautyServiceManager.sSenseTimePlugin != null
            ) {
                if (!isInit) {
                    SenseBeautyServiceManager.sSenseTimePlugin!!.init()
                    sSubModelFromAssetsFile.forEach {
                        SenseBeautyServiceManager.sSenseTimePlugin!!.addSubModelFromAssetsFile(it)
                    }
                    sSubModelFromFile.forEach {
                        SenseBeautyServiceManager.sSenseTimePlugin!!.addSubModel(it)
                    }
                    sSenseTimePlugin!!.recoverEffects()
                    isInit = true
                }
                return true
            } else {
                return false
            }
        }

        override fun onTextureFrameAvailable(
            textureID: Int,
            type: QNVideoFrameType,
            width: Int,
            height: Int,
            rotation: Int,
            timestampNs: Long,
            transformMatrix: FloatArray?
        ): Int {
            return if (checkInit()) {
                if (mRotation != rotation) {
                    sSenseTimePlugin?.updateDirection(
                        rotation,
                        (rotation == 270 || rotation == 90),
                        false
                    )
                    mRotation = rotation
                }
                sSenseTimePlugin!!.processTexture(
                    textureID,
                    width,
                    height
                )
            } else {
                textureID;
            }
        }
    }

    /**
     * 获得rtc对象
     */
    private val rtcRoomGetter by lazy {
        (client as QRTCProvider).rtcRoomGetter.invoke()
    }

    override fun attachRoomClient(client: QLiveClient) {
        super.attachRoomClient(client)
        if (!SenseBeautyServiceManager.isEnable) {
            return
        }
        if (client.clientType != QClientType.PUSHER) {
            return
        }
        rtcRoomGetter.mInnerVideoFrameListener = mVideoFrameListener
    }

    override fun onDestroyed() {
        super.onDestroyed()
        SenseBeautyServiceManager.sSenseTimePlugin?.destroy()
    }

}