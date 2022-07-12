package com.qlive.core;

import com.qlive.core.been.QLiveRoomInfo;

//房间客户端
public interface QLiveClient {

    /**
     * 获取服务实例
     * @param serviceClass
     * @param <T>
     * @return
     */
    <T extends QLiveService> T getService(Class<T> serviceClass);

    void setLiveStatusListener(QLiveStatusListener liveStatusListener);

    void destroy();
    QClientType getClientType();

}
