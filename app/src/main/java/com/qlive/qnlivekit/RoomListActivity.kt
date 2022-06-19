package com.qlive.qnlivekit

import android.content.Context
import android.content.Intent
import com.qlive.uikitcore.activity.BaseFrameActivity

class RoomListActivity : BaseFrameActivity() {

    companion object {
        fun start(context: Context) {
            val i = Intent(context, RoomListActivity::class.java)
            context.startActivity(i)
        }
    }

    override fun init() {
        title = "直播列表"
//        roomListView.attach(this)
//        tvCreateRoom.setOnClickListener {
//            QNLiveRoomUIKit.createAndJoinRoom(this, object :
//                QLiveCallBack<QLiveRoomInfo> {
//                override fun onError(code: Int, msg: String?) {
//                    msg?.asToast()
//                }
//                override fun onSuccess(data: QLiveRoomInfo?) {}
//            })
//        }
    }


    override fun getLayoutId(): Int {
        return R.layout.kit_activity_room_list
    }

}