package com.qlive.avparam;

public interface QVideoFrameListener {
    default void onYUVFrameAvailable(
            byte[] data,
            QVideoFrameType type,
            int width,
            int height,
            int rotation,
            long timestampNs
    ) {
    }
    default int onTextureFrameAvailable(
            int textureID,
            QVideoFrameType type,
            int width,
            int height,
            int rotation,
            long timestampNs,
            float[] transformMatrix
    ) {
        return textureID;
    }
}
