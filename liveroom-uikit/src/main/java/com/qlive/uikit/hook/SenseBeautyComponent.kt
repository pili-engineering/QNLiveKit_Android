package com.qlive.uikit.hook

import android.content.Context
import androidx.fragment.app.DialogFragment
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikitcore.QBaseLiveComponent
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.backGround
import com.qlive.uiwidghtbeauty.BeautyDialogFragment
import com.qlive.uiwidghtbeauty.EffectBeautyDialogFragment
import com.qlive.uiwidghtbeauty.QSenseTimeManager
import com.qlive.uiwidghtbeauty.StickerDialog

object SenseBeautyComponent {

    var isInit = false
    val mInnerComponentProxy by lazy { InnerComponentProxy() }

    fun init(appContext: Context) {
        try {
            //校验有没有依赖
            QSenseTimeManager.isAuthorized
            QSenseTimeManager.initEffectFromLocalLicense(appContext)
            isInit = true
        } catch (e: NoClassDefFoundError) {
            e.printStackTrace()
            isInit = false
        }
    }

    class InnerComponentProxy : QBaseLiveComponent {
        override var client: QLiveClient? = null
        override var roomInfo: QLiveRoomInfo? = null
        override var user: QLiveUser? = null
        override var kitContext: QLiveUIKitContext? = null


        var mEffectBeautyDialogFragment: EffectBeautyDialogFragment? = null
            private set
        var mStickerDialog: StickerDialog? = null
            private set

        override fun onDestroyed() {
            super.onDestroyed()
            if (isInit) {
                destroy()
            }
        }

        fun showDialog(
            code: Int
        ): DialogFragment? {
            if (!isInit) {
                return null
            }
            if (code == 0) {
                if (mEffectBeautyDialogFragment == null) {
                    mEffectBeautyDialogFragment = EffectBeautyDialogFragment()
                }
                mEffectBeautyDialogFragment!!.show(kitContext!!.fragmentManager, "")
                return mEffectBeautyDialogFragment!!
            } else {
                if (mStickerDialog == null) {
                    mStickerDialog = StickerDialog()
                }
                mStickerDialog!!.show(kitContext!!.fragmentManager, "")
                return mStickerDialog!!
            }
        }

        private fun destroy() {
            mEffectBeautyDialogFragment = null
            mStickerDialog = null
        }
    }
}