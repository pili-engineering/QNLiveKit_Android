package com.qncube.roomservice;

import com.qncube.liveroomcore.been.QExtension;

public interface QRoomServiceListener {
    /**
     * 直播间某个属性变化
     *
     * @param extension
     */
    void onRoomExtensionUpdate(QExtension extension);
}