package com.qncube.uikitdanmaku

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qncube.danmakuservice.QNDanmakuService
import com.qncube.liveroomcore.QNLiveClient
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.QBaseRoomFrameLayout
import com.qncube.uikitcore.ext.ViewUtil

/**
 * 弹幕轨道管理view
 */
class DanmakuTrackManagerView : QBaseRoomFrameLayout {
    private val mTrackManager = TrackManager()
    private val mQNDanmakuServiceListener =
        QNDanmakuService.QNDanmakuServiceListener { model ->
            mTrackManager.onNewTrackArrive(model)
        }
    private var mDanmukeViewSlot: QNDanmukeViewSlot = object : QNDanmukeViewSlot {
        override fun createView(
            lifecycleOwner: LifecycleOwner,
            context: KitContext,
            client: QNLiveClient,
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
        client!!.getService(QNDanmakuService::class.java)
            .addDanmakuServiceListener(mQNDanmakuServiceListener)
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
            client?.getService(QNDanmakuService::class.java)
                ?.removeDanmakuServiceListener(mQNDanmakuServiceListener)
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
            context: KitContext,
            client: QNLiveClient,
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


