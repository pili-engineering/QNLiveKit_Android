package com.qlive.uikitcore

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser

abstract class QBaseRoomFrameLayout : FrameLayout, QLiveComponent {
     var client: QLiveClient? = null
     var roomInfo: QLiveRoomInfo? = null
     var user: QLiveUser? = null
     var kitContext: QLiveUIKitContext? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val layoutId = getLayoutId()
        if (layoutId > 0) {
            LayoutInflater.from(context).inflate(layoutId, this, true)
        }
    }

    /**
     * 绑定上下文回调
     */
    override fun attachKitContext(context: QLiveUIKitContext) {
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
    override fun attachLiveClient(client: QLiveClient) {
        this.client = client
        initView()
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

    abstract fun getLayoutId(): Int

    abstract fun initView()
}

abstract class QBaseRoomLinearLayout : LinearLayout, QLiveComponent {
     var client: QLiveClient? = null
     var roomInfo: QLiveRoomInfo? = null
     var user: QLiveUser? = null
     var kitContext: QLiveUIKitContext? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    abstract fun initView()

    /**
     * 绑定上下文回调
     */
    override fun attachKitContext(context: QLiveUIKitContext) {
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
    override fun attachLiveClient(client: QLiveClient) {
        this.client = client
        initView()
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

class QLiveEmptyView : View, QLiveComponent {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        visibility  = View.GONE
    }

    override fun attachKitContext(context: QLiveUIKitContext) {
    }
    override fun attachLiveClient(client: QLiveClient) {
    }
    override fun onEntering(liveId: String, user: QLiveUser) {
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
    }

    override fun onLeft() {
    }
    override fun onDestroyed() {
    }
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    }
}

class QEmptyView : View, QComponent {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }
    override var kitContext: QUIKitContext? = null

}