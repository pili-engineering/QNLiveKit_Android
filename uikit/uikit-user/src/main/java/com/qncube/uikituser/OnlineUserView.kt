package com.qncube.uikituser

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qncube.chatservice.QChatRoomService
import com.qncube.chatservice.QChatRoomServiceListener
import com.qncube.linveroominner.Scheduler
import com.qncube.liveroomcore.mode.QNLiveUser
import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.uikitcore.*
import com.qncube.uikitcore.ext.bg
import kotlinx.android.synthetic.main.kit_view_online.view.*

class OnlineUserView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var adapter: BaseQuickAdapter<QNLiveUser, BaseViewHolder> = OnlineUserViewAdapter()

    //聊天室监听
    private val mChatRoomServiceListener = object :
        QChatRoomServiceListener {
        override fun onUserJoin(memberId: String) {
            refresh()
        }

        override fun onUserLevel(memberId: String) {
            refresh()
        }

        override fun onReceivedC2CMsg(msg: String, fromId: String, toId: String) {}
        override fun onReceivedGroupMsg(msg: String, fromId: String, toId: String) {}
        override fun onUserBeKicked(memberId: String) {}
        override fun onUserBeMuted(isMute: Boolean, memberId: String, duration: Long) {}
        override fun onAdminAdd(memberId: String) {}
        override fun onAdminRemoved(memberId: String, reason: String) {}
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_view_online
    }

    override fun initView() {
        client!!.getService(QChatRoomService::class.java)
            .addChatServiceListener(mChatRoomServiceListener)
        recyOnline?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.setOnItemClickListener { _, view, position ->

        }
        recyOnline.adapter = adapter
    }

    private var roomId = ""

    private val lazyFreshJob = com.qncube.linveroominner.Scheduler(30000) {
        refresh()
    }

    private fun refresh() {
        if (roomId.isEmpty()) {
            return
        }
        kitContext?.lifecycleOwner?.bg {
            doWork {
                //todo
//                client?.getService(Rooms)
//                val users = mUserDataSource.getOnlineUser(roomId, 1, 10).list.filter {
//                    it.userId != roomInfo?.anchorInfo?.userId
//                }

                //      adapter.setNewData(users)
            }
            catchError {
            }
        }
    }

    override fun onEntering(roomId: String, user: QNLiveUser) {
        super.onEntering(roomId, user)
        this.roomId = roomId
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        lazyFreshJob.start()
    }

    override fun onLeft() {
        super.onLeft()
        roomId = ""
        adapter.setNewData(ArrayList<QNLiveUser>())
        lazyFreshJob.cancel()
    }

}

