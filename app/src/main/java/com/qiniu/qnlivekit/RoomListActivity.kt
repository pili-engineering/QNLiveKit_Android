package com.qiniu.qnlivekit

import android.content.Context
import android.content.Intent
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.liveroomcore.asToast
import com.qncube.liveuikit.QNLiveRoomUIKit
import com.qncube.uikitcore.activity.BaseFrameActivity
import kotlinx.android.synthetic.main.activity_room_list.*

class RoomListActivity : BaseFrameActivity() {

    companion object {

        fun start(context: Context) {
            val i = Intent(context, RoomListActivity::class.java)
            context.startActivity(i)
        }
    }

    override fun init() {

        title = "直播列表"

        roomListView.attach(this)
        tvCreateRoom.setOnClickListener {
            QNLiveRoomUIKit.createAndJoinRoom(this, object :
                QLiveCallBack<QLiveRoomInfo> {
                override fun onError(code: Int, msg: String?) {
                    msg?.asToast()
                }

                override fun onSuccess(data: QLiveRoomInfo?) {}
            })
        }
    }

    override fun isTitleCenter(): Boolean {
        return true
    }

    override fun isToolBarEnable(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_room_list
    }

}