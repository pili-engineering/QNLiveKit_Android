package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikitcore.QLiveComponent
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.view.CommonViewPagerAdapter

/**
 * 房间覆盖层ViewPage
 * 左滑 隐藏 右滑显示
 */
class RoomCoverViewPage : ViewPager, QLiveComponent {

    private val views = ArrayList<View>()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        visibility = View.INVISIBLE
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        visibility = View.VISIBLE

    }

    override fun attachKitContext(context: QLiveUIKitContext) {
        post {
            if (views.isEmpty()) {
                views.clear()
                val prentView = (parent as ViewGroup)
                for (i in 1 until prentView.childCount) {
                    views.add(prentView.getChildAt(i))
                }
                prentView.removeViews(1, prentView.childCount - 1)
                removeAllViews()
                adapter = CommonViewPagerAdapter(views)
                currentItem = views.size - 1
            }
        }
    }

    override fun attachLiveClient(client: QLiveClient) {
    }

    override fun onEntering(liveId: String, user: QLiveUser) {
    }

    override fun onLeft() {
    }

    override fun onDestroyed() {
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    }

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