package com.qncube.lcommon

interface QVideoFrameListener {
    fun onYUVFrameAvailable(
        data: ByteArray,
        type: QVideoFrameType,
        width: Int,
        height: Int,
        rotation: Int,
        timestampNs: Long
    )

    fun onTextureFrameAvailable(
        textureID: Int,
        type: QVideoFrameType,
        width: Int,
        height: Int,
        rotation: Int,
        timestampNs: Long,
        transformMatrix: FloatArray?
    ): Int
}

enum class QVideoFrameType {
    YUV_NV21, TEXTURE_RGB, TEXTURE_OES
}

