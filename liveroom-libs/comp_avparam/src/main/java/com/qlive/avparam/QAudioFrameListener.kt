package com.qlive.avparam

import java.nio.ByteBuffer

interface QAudioFrameListener {
    fun onAudioFrameAvailable(
        srcBuffer: ByteBuffer,
        size: Int,
        bitsPerSample: Int,
        sampleRate: Int,
        numberOfChannels: Int
    )
}