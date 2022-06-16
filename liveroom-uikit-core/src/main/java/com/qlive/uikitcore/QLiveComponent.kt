package com.qlive.uikitcore

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.qlive.core.QLiveClient
import com.qlive.core.QClientLifeCycleListener
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser

interface QLiveComponent :
    QClientLifeCycleListener, LifecycleEventObserver {

    var client: QLiveClient?
    var roomInfo: QLiveRoomInfo?
    var user: QLiveUser?
    var kitContext: QLiveUIKitContext?

    /**
     * 绑定上下文回调
     */
    fun attachKitContext(context: QLiveUIKitContext) {
        this.kitContext = context
        context.lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            kitContext = null
        }
    }

    /**
     * 绑定房间客户端回调
     * @param client 组件拿到client 就能拿到所有访问业务服务的能力 如发消息 设置监听
     */
    open fun attachLiveClient(client: QLiveClient) {
        this.client = client

    }

    /**
     * 房间加入成功回调
     * @param roomInfo 加入哪个房间
     */
    override fun onJoined(roomInfo: QLiveRoomInfo) {
        this.roomInfo = roomInfo
    }

    /**
     * 房间正在进入回调
     */
    override fun onEntering(roomId: String, user: QLiveUser) {
        this.user = user
    }

    /**
     * 当前房间已经离开回调 - 我是观众-离开 我是主播对应关闭房间
     */
    override fun onLeft() {

    }

    /**
     * client销毁回调 == 房间页面将要退出
     */
    override fun onDestroyed() {
        client = null
    }

}