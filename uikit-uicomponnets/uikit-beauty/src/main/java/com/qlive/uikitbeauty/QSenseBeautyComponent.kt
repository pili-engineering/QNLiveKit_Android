package com.qlive.uikitbeauty

import android.util.Log
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
import com.qlive.uiwidghtbeauty.QSenseTimeManager
import com.qlive.uiwidghtbeauty.QSenseTimeManager.sSenseTimePlugin

/**
 * 美颜插件
 */
class QSenseBeautyComponent : BaseQLiveComponent(), ShowDialogAble {

    private var mBeautyDialogFragment: BeautyDialogFragment? = null

    override fun getComponentName(): String {
        return "senseBeautyComponent"
    }

    private val mVideoFrameListener = object : QVideoFrameListener {
        override fun onYUVFrameAvailable(
            data: ByteArray,
            type: QVideoFrameType,
            width: Int,
            height: Int,
            rotation: Int,
            timestampNs: Long
        ) {
            Log.d("onYUVFrameAvailable","rotation ${rotation}")
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
        mBeautyDialogFragment = BeautyDialogFragment()
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
        mBeautyDialogFragment?.dismissCall = {
            listener?.onDismiss(mBeautyDialogFragment!!)
            mBeautyDialogFragment?.dismissCall = {}
        }
        mBeautyDialogFragment!!.show(kitContext!!.fragmentManager, "")
        return mBeautyDialogFragment!!
    }
}