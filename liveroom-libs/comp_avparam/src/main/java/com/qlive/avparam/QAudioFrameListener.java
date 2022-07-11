package com.qlive.avparam;

import java.nio.ByteBuffer;

public interface QAudioFrameListener {
    void onAudioFrameAvailable(
            ByteBuffer srcBuffer,
            int size,
            int bitsPerSample,
            int sampleRate,
            int numberOfChannels
    );
}
