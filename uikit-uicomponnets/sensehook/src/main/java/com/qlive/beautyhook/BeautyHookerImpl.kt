package com.qlive.beautyhook

import android.content.Context
import com.qiniu.droid.rtc.QNVideoFrameListener
import com.qiniu.sensetimeplugin.QNSenseTimePlugin
import com.qlive.rtclive.BeautyHooker
import com.qlive.uiwidghtbeauty.EffectBeautyDialogFragment
import com.qlive.uiwidghtbeauty.QSenseTimeManager
import com.qlive.uiwidghtbeauty.StickerDialog

class BeautyHookerImpl : BeautyHooker {

    private var mCurrentFrameListener: SenseVideoFrameListener? = null
    var mEffectBeautyDialogFragment: EffectBeautyDialogFragment? = null
        private set
    var mStickerDialog: StickerDialog? = null
        private set

    companion object {
        var senseTimePlugin: QNSenseTimePlugin? = null
        val addSubModels = ArrayList<String>()
        val addSubModelFromAssetsFiles = ArrayList<String>()
    }

    override fun init(context: Context) {
        QSenseTimeManager.initEffectFromLocalLicense(context, true)
        com.qlive.uikitcore.BeautyComponent.showBeautyStickDialog = {
            mStickerDialog?.show(it, "")
        }
        com.qlive.uikitcore.BeautyComponent.showBeautyEffectDialog = {
            mEffectBeautyDialogFragment?.show(it, "")
        }
    }

    override fun provideVideoFrameListener(): QNVideoFrameListener {
        mCurrentFrameListener = SenseVideoFrameListener()
        return mCurrentFrameListener!!
    }

    override fun attach() {
        if (mEffectBeautyDialogFragment == null) {
            mEffectBeautyDialogFragment = EffectBeautyDialogFragment()
        }
        if (mStickerDialog == null) {
            mStickerDialog = StickerDialog()
        }
    }

    override fun detach() {
        mStickerDialog = null
        mEffectBeautyDialogFragment = null
        mCurrentFrameListener?.release()
    }

}