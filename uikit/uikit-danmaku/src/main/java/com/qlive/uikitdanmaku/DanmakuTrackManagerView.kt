package com.qlive.uikitdanmaku

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qlive.danmakuservice.QDanmaku
import com.qlive.danmakuservice.QDanmakuService
import com.qlive.danmakuservice.QDanmakuServiceListener
import com.qlive.core.QLiveClient
import com.qlive.uikitcore.QBaseRoomFrameLayout
import com.qlive.uikitcore.QLiveKitUIContext
import com.qlive.uikitcore.ext.ViewUtil

/**
 * 弹幕轨道管理view
 */
class DanmakuTrackManagerView : QBaseRoomFrameLayout {
    private val mTrackManager = TrackManager()
    private val mQDanmakuServiceListener = object : QDanmakuServiceListener {
        override fun onReceiveDanmaku(danmaku: QDanmaku) {
            mTrackManager.onNewTrackArrive(danmaku)
        }
    }
    private var mDanmukeViewSlot: QNDanmukeViewSlot = object : QNDanmukeViewSlot {
        override fun createView(
            lifecycleOwner: LifecycleOwner,
            context: QLiveKitUIContext,
            client: QLiveClient,
            container: ViewGroup?
        ): IDanmakuView {
            return DanmuTrackView(context.androidContext)
        }

        override fun getIDanmakuViewCount(): Int {
            return 3
        }

        override fun topMargin(): Int {
            return ViewUtil.dip2px(120f)
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getLayoutId(): Int {
        return R.layout.kit_danmaku_track_manager
    }

    override fun initView() {
        client!!.getService(QDanmakuService::class.java)
            .addDanmakuServiceListener(mQDanmakuServiceListener)
        for (i in 0 until mDanmukeViewSlot.getIDanmakuViewCount()) {
            val itemView = mDanmukeViewSlot.createView(
                kitContext!!.lifecycleOwner,
                kitContext!!,
                client!!,
                this
            )
            val lp = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            lp.topMargin = mDanmukeViewSlot.topMargin()
            addView(itemView.getView(), lp)
            mTrackManager.addTrackView(itemView)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
        if (event == Lifecycle.Event.ON_DESTROY) {
            client?.getService(QDanmakuService::class.java)
                ?.removeDanmakuServiceListener(mQDanmakuServiceListener)
        }
    }

    override fun onLeft() {
        super.onLeft()
        mTrackManager.onRoomLeft()
    }

    interface QNDanmukeViewSlot {
        /**
         * 创建单个弹幕轨道
         * @param container
         * @return
         */
        fun createView(
            lifecycleOwner: LifecycleOwner,
            context: QLiveKitUIContext,
            client: QLiveClient,
            container: ViewGroup?
        ): IDanmakuView

        /**
         * 弹幕轨道个数
         * @return
         */
        fun getIDanmakuViewCount(): Int

        /**
         * 距离上一个轨道的上间距
         * @return
         */
        fun topMargin(): Int
    }
}


