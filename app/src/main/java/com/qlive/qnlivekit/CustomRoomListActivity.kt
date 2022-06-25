package com.qlive.qnlivekit

import android.content.Context
import android.content.Intent
import com.qlive.sdk.QLive
import com.qlive.uikit.RoomPage
import com.qlive.uikitcore.QUIKitContext
import com.qlive.uikitcore.activity.BaseFrameActivity
import kotlinx.android.synthetic.main.activity_room_list.*

class CustomRoomListActivity : BaseFrameActivity() {

    companion object {
        fun start(context: Context) {
            val i = Intent(context, CustomRoomListActivity::class.java)
            context.startActivity(i)
        }
    }

    override fun init() {
        title = "直播列表"
        roomListView.attachKitContext(QUIKitContext(this, supportFragmentManager, this, this))
        //主动跳转观众直播间
       // QLive.getLiveUIKit().getPage(RoomPage::class.java).joinPlayerRoom()
        //主动跳转主播直播间
       // QLive.getLiveUIKit().getPage(RoomPage::class.java).joinAnchorRoom()
        tvCreateRoom.setOnClickListener {
            //自定义开播跳转预览创建
            QLive.getLiveUIKit().getPage(RoomPage::class.java).createAndJoinRoom(this, null)
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.kit_activity_room_list
    }

}