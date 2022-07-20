package com.qlive.uikitcore;

import androidx.lifecycle.LifecycleEventObserver;

import com.qlive.core.QClientLifeCycleListener;
import com.qlive.core.QLiveClient;

import org.jetbrains.annotations.NotNull;

/**
 * 直播间内小组件
 * <p>
 * 父接口：
 * QClientLifeCycleListener -> client 生命周期
 * LifecycleEventObserver ->  房间客户端生命周期
 */
public interface QLiveComponent extends QClientLifeCycleListener, LifecycleEventObserver {
    /**
     * 绑定UI组件上下文生命
     * 每个组件都拥有组件上下文 能获取到业务逻辑能力能和UI能力
     *
     * @param context UI组件上下文
     */
    void attachKitContext(@NotNull QLiveUIKitContext context);

    /**
     * 绑定房间客户端
     *
     * @param client 推流/拉流客户端
     */
    void attachLiveClient(@NotNull QLiveClient client);
}
