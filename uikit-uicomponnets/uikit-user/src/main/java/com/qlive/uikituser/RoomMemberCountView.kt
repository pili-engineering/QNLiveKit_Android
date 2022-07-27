package com.qlive.uikituser

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.qlive.chatservice.QChatRoomService
import com.qlive.chatservice.QChatRoomServiceListener
import com.qlive.core.QLiveCallBack
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.roomservice.QRoomService
import com.qlive.uikitcore.QKitTextView
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.Scheduler
import com.qlive.uikitcore.ext.ViewUtil

class RoomMemberCountView : QKitTextView {


    companion object {
        /**
         * 点击事件回调
         */
        var onClickListener: (context: QLiveUIKitContext?,client:QLiveClient?, view: View) -> Unit =
            { _, _,_ -> }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        setOnClickListener {
            onClickListener.invoke(kitContext,client,this)
        }
    }

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
        if (text.length > 2) {
            setTextSize(TypedValue.COMPLEX_UNIT_SP,10f)
        } else {
            setTextSize(TypedValue.COMPLEX_UNIT_SP,14f)
        }
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

    override fun onDestroyed() {
        super.onDestroyed()
        mScheduler.cancel()
    }

    override fun attachLiveClient(client: QLiveClient) {
        super.attachLiveClient(client)
        client.getService(QChatRoomService::class.java)
            .addServiceListener(mChatRoomServiceListener)
    }
}





