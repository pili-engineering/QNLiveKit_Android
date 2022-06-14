package com.qncube.liveroomcore;

import com.qncube.liveroomcore.mode.QLiveRoomInfo;
import com.qncube.liveroomcore.mode.QNLiveUser;

import org.jetbrains.annotations.NotNull;

/**
 * 房间生命周期
 */
public interface QClientLifeCycleListener {

    /**
     * 进入回调
     * @param user 进入房间的用户
     * @param liveId 房间ID
     */
    void onEntering(@NotNull String liveId, @NotNull QNLiveUser user);

    /**
     * 加入回调
     * @param roomInfo 房间信息
     */
    void onJoined(@NotNull QLiveRoomInfo roomInfo);

    /**
     * 用户离开回调
     */
    void onLeft();

    /**
     * 销毁
     */
    void onDestroyed();


}
