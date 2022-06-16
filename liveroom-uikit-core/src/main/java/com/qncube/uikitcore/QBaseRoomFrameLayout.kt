package com.qncube.uikitcore

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.qncube.liveroomcore.QLiveClient
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser

abstract class QBaseRoomFrameLayout : FrameLayout, QLiveComponent {
    override var client: QLiveClient? = null
    override var roomInfo: QLiveRoomInfo? = null
    override var user: QLiveUser? = null
    override var kitContext: QLiveKitUIContext? = null

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

    override fun attachLiveClient(client: QLiveClient) {
        super.attachLiveClient(client)
        initView()
    }

    abstract fun getLayoutId(): Int

    abstract fun initView()
}

abstract class QBaseRoomLinearLayout : LinearLayout, QLiveComponent {
    override var client: QLiveClient? = null
    override var roomInfo: QLiveRoomInfo? = null
    override var user: QLiveUser? = null
    override var kitContext: QLiveKitUIContext? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    abstract fun initView()
    override fun attachLiveClient(client: QLiveClient) {
        super.attachLiveClient(client)
        initView()
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
    }

    override var client: QLiveClient? = null
    override var roomInfo: QLiveRoomInfo? = null
    override var user: QLiveUser? = null
    override var kitContext: QLiveKitUIContext? = null

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