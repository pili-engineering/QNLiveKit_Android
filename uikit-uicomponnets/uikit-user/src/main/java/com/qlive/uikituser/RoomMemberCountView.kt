package com.qlive.uikituser

import android.content.Context
import android.util.AttributeSet
import com.qlive.chatservice.QChatRoomService
import com.qlive.chatservice.QChatRoomServiceListener
import com.qlive.core.QLiveCallBack
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.roomservice.QRoomService
import com.qlive.uikitcore.QKitTextView
import com.qlive.uikitcore.Scheduler

class RoomMemberCountView : QKitTextView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    //  private val mRoomDaraSource = RoomDataSource()
    private val mChatRoomServiceListener = object :
        QChatRoomServiceListener {
        override fun onUserJoin(memberID: String) {
            refresh(true)
        }

        override fun onUserLeft(memberID: String) {
            refresh(false)
        }

        override fun onUserKicked(memberID: String) {
            refresh(false)
        }
    }


    private fun refresh(add: Boolean?) {

        var count = -1
        try {
            count = (text.toString().toInt())
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
        if (count < 1) {
            count = 1
        }
        text = count.toString()
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
        if (roomInfo == null) {
            return@Scheduler
        }
        try {
            val room = client?.getService(QRoomService::class.java)?.getRoomInfo(object :
                QLiveCallBack<QLiveRoomInfo> {
                override fun onError(code: Int, msg: String?) {
                }

                override fun onSuccess(data: QLiveRoomInfo) {
                    text = data.onlineCount.toString()
                    checkTextSize()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        mScheduler.start()
    }

    override fun onLeft() {
        super.onLeft()
        mScheduler.cancel()
    }

    override fun attachLiveClient(client: QLiveClient) {
        super.attachLiveClient(client)
        client.getService(QChatRoomService::class.java)
            .addServiceListener(mChatRoomServiceListener)
    }
}





