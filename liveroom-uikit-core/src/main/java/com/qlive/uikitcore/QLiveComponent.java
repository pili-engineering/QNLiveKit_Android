package com.qlive.uikitcore;

import androidx.lifecycle.LifecycleEventObserver;

import com.qlive.core.QClientLifeCycleListener;
import com.qlive.core.QLiveClient;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

    /**
     * 发送UI事件给所有UI组件
     *
     * @param action 事件名字
     * @param data   数据
     */
    default void sendUIEvent(@NotNull  String action,@NotNull String data) {
        QLiveUIEventManager.INSTANCE.sendUIEvent(Objects.requireNonNull(this.getClass().getCanonicalName()),action,data);
    }

    /**
     * 发送UI事件给目标组件
     *
     * @param targetComponentClassName 目标组件的类全限定名
     * @param action                   事件名字
     * @param data                     数据
     */
    default void sendUIEvent(@NotNull String targetComponentClassName,@NotNull String action,@NotNull String data) {
        QLiveUIEventManager.INSTANCE.sendUIEvent(Objects.requireNonNull(this.getClass().getCanonicalName()),targetComponentClassName, action, data);
    }

    /**
     * 收到UI事件
     * @param srcComponentClassName 发送事件的组件
     * @param action                   事件名字
     * @param data                     数据
     */
    default void onReceiveUIEvent(@NotNull String srcComponentClassName,@NotNull String action, @NotNull String data) {

    }

}
