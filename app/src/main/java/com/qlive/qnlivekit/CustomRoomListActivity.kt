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

        roomListView.attachKitContext(QUIKitContext(this,supportFragmentManager,this,this))
      //  roomListView.set
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