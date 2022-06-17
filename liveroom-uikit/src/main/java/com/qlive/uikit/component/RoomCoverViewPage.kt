package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager.widget.ViewPager
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikitcore.QLiveComponent
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.view.CommonViewPagerAdapter

class RoomCoverViewPage : ViewPager, QLiveComponent {

    private val views = ArrayList<View>()
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        visibility = View.INVISIBLE
    }
    override fun onJoined(roomInfo: QLiveRoomInfo) {
        visibility = View.VISIBLE
        if(views.isEmpty()){
            views.clear()
            for (i in 0 until childCount) {
                views.add(getChildAt(i))
            }
            removeAllViews()
            adapter = CommonViewPagerAdapter(views)
            currentItem = 1
        }
    }

    override fun attachKitContext(context: QLiveUIKitContext) {
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
}