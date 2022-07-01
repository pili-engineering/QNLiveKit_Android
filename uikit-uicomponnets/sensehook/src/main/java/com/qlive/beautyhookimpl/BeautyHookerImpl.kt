package com.qlive.beautyhookimpl

import android.content.Context
import com.qiniu.droid.rtc.QNVideoFrameListener
import com.qiniu.sensetimeplugin.QNSenseTimePlugin
import com.qlive.beautyhook.BeautyHookManager
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
    }

    override fun init(context: Context) {
        QSenseTimeManager.initEffectFromLocalLicense(context, true)
        BeautyHookManager.showBeautyStickDialog = {
            mStickerDialog?.show(it,"")
        }
        BeautyHookManager.showBeautyEffectDialog ={
            mEffectBeautyDialogFragment?.show(it,"")
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