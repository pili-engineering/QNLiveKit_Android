package com.qlive.beautyhook

import com.qiniu.droid.rtc.QNVideoFrameListener
import com.qiniu.droid.rtc.QNVideoFrameType

class BeautyRender : QNVideoFrameListener {
    override fun onYUVFrameAvailable(
        p0: ByteArray?,
        p1: QNVideoFrameType?,
        p2: Int,
        p3: Int,
        p4: Int,
        p5: Long
    ) {

    }

    override fun onTextureFrameAvailable(
        p0: Int,
        p1: QNVideoFrameType?,
        p2: Int,
        p3: Int,
        p4: Int,
        p5: Long,
        p6: FloatArray?
    ): Int {
        TODO("Not yet implemented")
    }


}