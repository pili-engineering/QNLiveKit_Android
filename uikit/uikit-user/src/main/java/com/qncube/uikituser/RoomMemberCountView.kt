package com.qncube.uikituser

import android.content.Context
import android.util.AttributeSet
import com.qncube.chatservice.QClientService
import com.qncube.chatservice.QNChatRoomServiceListener
import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.liveroomcore.Scheduler
import com.qncube.uikitcore.QBaseRoomFrameLayout
import kotlinx.android.synthetic.main.kit_view_room_member_count.view.*

class RoomMemberCountView : QBaseRoomFrameLayout{
    
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
  //  private val mRoomDaraSource = RoomDataSource()
    private val mChatRoomServiceListener = object : QNChatRoomServiceListener {
        override fun onUserJoin(memberId: String) {
            refresh(true)
        }

        override fun onUserLevel(memberId: String) {
            refresh(true)
        }

        override fun onUserBeKicked(memberId: String) {
            refresh(false)
        }
    }


    private fun refresh(add: Boolean?) {

        var count = -1
        try {
            count = (tvCount.text.toString().toInt())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (count == -1) {
            return
        }
        if (add == true) {
            count++
        }
        if (add == false) {
            count--
        }
        tvCount.text = count.toString()
        checkTextSize()
    }

    private fun checkTextSize() {
//        if (tvCount.text.length > 2) {
//            tvCount.textSize = ViewUtil.sp2px(3f).toFloat()
//        } else {
//            tvCount.textSize = ViewUtil.sp2px(14f).toFloat()
//        }
    }

    private val mScheduler = Scheduler(15000) {
//        if (roomInfo == null) {
//            return@Scheduler
//        }
//        try {
//            val room = client?.getService(Rooms).refreshRoomInfo(roomInfo!!.liveId)
//            tvCount.text = room.onlineCount.toString()
//            checkTextSize()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
       // todo
    }


    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        mScheduler.start()
    }

    override fun onLeft() {
        super.onLeft()
        mScheduler.cancel()
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_view_room_member_count
    }

    override fun initView() {
        client!!.getService(QClientService::class.java)
            .addChatServiceListener(mChatRoomServiceListener)
    }

}





