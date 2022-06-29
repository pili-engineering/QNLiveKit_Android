package com.qlive.uikitbeauty

import androidx.fragment.app.DialogFragment
import com.qlive.avparam.QVideoFrameListener
import com.qlive.avparam.QVideoFrameType
import com.qlive.core.QClientType
import com.qlive.core.QLiveClient
import com.qlive.pushclient.QPusherClient
import com.qlive.uikitcore.BaseQLiveComponent
import com.qlive.uikitcore.ShowDialogAble
import com.qlive.uikitcore.dialog.FinalDialogFragment
import com.qlive.uiwidghtbeauty.BeautyDialogFragment
import com.qlive.uiwidghtbeauty.EffectBeautyDialogFragment
import com.qlive.uiwidghtbeauty.QSenseTimeManager
import com.qlive.uiwidghtbeauty.QSenseTimeManager.sSenseTimePlugin
import com.qlive.uiwidghtbeauty.StickerDialog

/**
 * 美颜插件
 */
class QSenseBeautyComponent : BaseQLiveComponent(), ShowDialogAble {

    private var mEffectBeautyDialogFragment: BeautyDialogFragment? = null
    private var mStickerDialog: StickerDialog? = null
    private val mVideoFrameListener = object : QVideoFrameListener {
        override fun onYUVFrameAvailable(
            data: ByteArray,
            type: QVideoFrameType,
            width: Int,
            height: Int,
            rotation: Int,
            timestampNs: Long
        ) {
            // 前置摄像头返回的图像是横向镜像的
            sSenseTimePlugin.updateDirection(rotation, (rotation == 270 || rotation == 90), false)
        }

        override fun onTextureFrameAvailable(
            textureID: Int,
            type: QVideoFrameType,
            width: Int,
            height: Int,
            rotation: Int,
            timestampNs: Long,
            transformMatrix: FloatArray?
        ): Int {
            return sSenseTimePlugin.processTexture(textureID, width, height)
        }
    }

    override fun attachLiveClient(client: QLiveClient) {
        super.attachLiveClient(client)
        if (client.clientType != QClientType.PUSHER) {
            return
        }
        QSenseTimeManager.initSenseTime()
        mEffectBeautyDialogFragment = EffectBeautyDialogFragment()
        mStickerDialog = StickerDialog()
        (client as QPusherClient).setVideoFrameListener(mVideoFrameListener)
    }

    override fun onDestroyed() {
        super.onDestroyed()
        QSenseTimeManager.destroySenseTime()
    }

    override fun showDialog(
        code: Int,
        arg: Any,
        listener: FinalDialogFragment.BaseDialogListener?
    ): DialogFragment {
        if(code==0){
            mEffectBeautyDialogFragment?.dismissCall = {
                listener?.onDismiss(mEffectBeautyDialogFragment!!)
                mEffectBeautyDialogFragment?.dismissCall = {}
            }
            mEffectBeautyDialogFragment!!.show(kitContext!!.fragmentManager, "")
            return mEffectBeautyDialogFragment!!
        }else{
            mStickerDialog?.dismissCall = {
                listener?.onDismiss(mStickerDialog!!)
                mStickerDialog?.dismissCall = {}
            }
            mStickerDialog!!.show(kitContext!!.fragmentManager, "")
            return mStickerDialog!!
        }
    }
}