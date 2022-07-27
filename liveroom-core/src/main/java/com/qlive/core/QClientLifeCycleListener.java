package com.qlive.core;

import com.qlive.core.been.QLiveRoomInfo;
import com.qlive.core.been.QLiveUser;

import org.jetbrains.annotations.NotNull;

/**
 * ui组件实现的房间生命周期
 */
public interface QClientLifeCycleListener {

    /**
     * 进入回调
     * @param user 进入房间的用户
     * @param liveId 房间ID
     */
    void onEntering(@NotNull String liveId, @NotNull QLiveUser user);

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
