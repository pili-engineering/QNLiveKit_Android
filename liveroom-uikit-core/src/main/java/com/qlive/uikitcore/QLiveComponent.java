package com.qlive.uikitcore;

import androidx.lifecycle.LifecycleEventObserver;

import com.qlive.core.QClientLifeCycleListener;
import com.qlive.core.QLiveClient;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

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
     * 注册UI组件之间的通信事件
     * @param clz 事件类
     * @param call 回调函数
     * @param <T>
     */
    default <T extends UIEvent> void registerEventAction(Class<T> clz, Function1<T, Unit> call) {
        QLiveUIEventManager.INSTANCE.getActionMap(this).put(UIEvent.Companion.getAction(clz), (Function1<UIEvent, Unit>) call);
    }

    /**
     * 发送UI通信事件
     * 该event 将会被发送到所有注册关心该事件的UI组件中去
     * @param event 事件对象
     * @param <T>
     */
    default <T extends UIEvent> void sendUIEvent(T event) {
        QLiveUIEventManager.INSTANCE.sendUIEvent(event);
    }

}
