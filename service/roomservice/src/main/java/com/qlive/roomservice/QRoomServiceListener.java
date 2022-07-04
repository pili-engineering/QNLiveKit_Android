package com.qlive.roomservice;

import com.qlive.core.been.QExtension;

public interface QRoomServiceListener {
    /**
     * 直播间某个属性变化
     *
     * @param extension
     */
    void onRoomExtensionUpdate(QExtension extension);
}