package com.qncube.liveroomcore;

import com.qncube.liveroomcore.been.QLiveRoomInfo;
import com.qncube.liveroomcore.service.QLiveService;

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
    void joinRoom( String roomID, QLiveCallBack<QLiveRoomInfo> callBack);        //加入房间
    void destroy();
    QClientType getClientType();
}
