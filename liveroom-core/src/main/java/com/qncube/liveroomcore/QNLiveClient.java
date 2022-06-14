package com.qncube.liveroomcore;

//房间客户端
public interface QNLiveClient {

    /**
     * 获取服务实例
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    <T extends QNLiveService> T getService(Class<T> serviceClass);

    void setLiveStatusListener(QLiveStatusListener liveStatusListener);

    ClientType getClientType();
}
