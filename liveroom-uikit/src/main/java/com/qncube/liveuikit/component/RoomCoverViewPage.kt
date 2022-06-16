package com.qncube.liveuikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.qncube.liveroomcore.QLiveClient
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser
import com.qncube.uikitcore.QUIKitContext
import com.qncube.uikitcore.QLiveComponent
import com.qncube.uikitcore.QLiveKitUIContext
import com.qncube.uikitcore.view.CommonViewPagerAdapter

class RoomCoverViewPage : ViewPager, QLiveComponent {

    override var client: QLiveClient? = null
    override var roomInfo: QLiveRoomInfo? = null
    override var user: QLiveUser? = null
    override var kitContext: QLiveKitUIContext? = null
    private val views = ArrayList<View>()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        visibility = View.INVISIBLE
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
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
}