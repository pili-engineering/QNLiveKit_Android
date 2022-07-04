package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikitcore.QBaseLiveComponent
import com.qlive.uikitcore.QLiveComponent
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.view.CommonViewPagerAdapter

/**
 * 房间覆盖层ViewPage
 * 左滑 隐藏 右滑显示
 */
class RoomCoverViewPager : FrameLayout, QBaseLiveComponent {
    override var client: QLiveClient? = null
    override var roomInfo: QLiveRoomInfo? = null
    override var user: QLiveUser? = null
    override var kitContext: QLiveUIKitContext? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

    }

    private val views = ArrayList<View>()
    private var vp: RoomCoverViewPage? = null
    override fun attachKitContext(context: QLiveUIKitContext) {
        visibility = View.INVISIBLE
        post {
            for (i in 0 until childCount) {
                views.add(getChildAt(i))
            }
            removeAllViews()
            vp = RoomCoverViewPage(context.androidContext).apply {
                adapter = CommonViewPagerAdapter(views)
                currentItem = 0
            }
            addView(vp)
        }
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        visibility = View.VISIBLE
        if (views.size > 1) {
            vp?.setCurrentItem(1, true)
        } else {
            vp?.currentItem = views.size - 1
        }
    }

    class RoomCoverViewPage : ViewPager {
        constructor(context: Context) : this(context, null)
        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

        private var lastX = 0f
        var deal = false
        override fun onTouchEvent(ev: MotionEvent): Boolean {
            super.onTouchEvent(ev)
            if (deal) {
                return true
            }
            var cX = 0f;
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = ev.x;
                    deal = false
                }
                MotionEvent.ACTION_MOVE -> {
                    cX = ev.x;
                    if (cX - lastX > 30) {
                        deal = true
                    }
                }
            }
            if (deal || MotionEvent.ACTION_DOWN == ev.action) {
                return true
            }
            return false
        }
    }
}