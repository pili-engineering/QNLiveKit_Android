package com.qlive.uikituser

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qlive.chatservice.QChatRoomService
import com.qlive.chatservice.QChatRoomServiceListener
import com.qlive.core.QLiveCallBack
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.roomservice.QRoomService
import com.qlive.uikitcore.*
import com.qlive.uikitcore.ext.bg
import kotlinx.android.synthetic.main.kit_view_online.view.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class OnlineUserView : QKitFrameLayout {

    companion object {
        var layoutId = R.layout.kit_view_online
        /**
         * 点击事件
         */
        var onItemUserClickListener: (context: QLiveUIKitContext, view: View, user: QLiveUser) -> Unit =
            { _, _, _ -> }

        /**
         * 列表适配器
         */
        var adapterProvider: (context: QLiveUIKitContext, client: QLiveClient) -> BaseQuickAdapter<QLiveUser, BaseViewHolder> = {
            _,_->
            OnlineUserViewAdapter()
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val adapter: BaseQuickAdapter<QLiveUser, BaseViewHolder> by lazy {
        adapterProvider.invoke(kitContext!!,client!!)
    }

    //聊天室监听
    private val mChatRoomServiceListener = object :
        QChatRoomServiceListener {
        override fun onUserJoin(memberID: String) {
            refresh()
        }

        override fun onUserLeft(memberID: String) {
            refresh()
        }

        override fun onReceivedC2CMsg(msg: String, fromID: String, toID: String) {}
        override fun onReceivedGroupMsg(msg: String, fromID: String, toID: String) {}
        override fun onUserKicked(memberID: String) {}
        override fun onUserBeMuted(isMute: Boolean, memberID: String, duration: Long) {}
        override fun onAdminAdd(memberID: String) {}
        override fun onAdminRemoved(memberID: String, reason: String) {}
    }

    override fun getLayoutId(): Int {
        return layoutId
    }

    override fun initView() {
        client!!.getService(QChatRoomService::class.java)
            .addServiceListener(mChatRoomServiceListener)
        recyOnline?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter.setOnItemClickListener { _, view, position ->
            onItemUserClickListener.invoke(kitContext!!, view, adapter.data[position])
        }
        recyOnline.adapter = adapter
    }

    private var roomId = ""

    private val lazyFreshJob = Scheduler(30000) {
        refresh()
    }

    private suspend fun getOnlineUser() = suspendCoroutine<List<QLiveUser>> { ct ->
        client?.getService(QRoomService::class.java)?.getOnlineUser(1, 10,
            object : QLiveCallBack<List<QLiveUser>> {
                override fun onError(code: Int, msg: String?) {
                    ct.resumeWithException(Exception(msg))
                }

                override fun onSuccess(data: List<QLiveUser>) {
                    ct.resume(data)
                }
            })
    }

    private fun refresh() {
        if (roomId.isEmpty()) {
            return
        }
        kitContext?.lifecycleOwner?.bg {
            doWork {
                val users = getOnlineUser().filter {
                    it.userId != roomInfo?.anchor?.userId
                }
                adapter.setNewData(users)
            }
            catchError {
            }
        }
    }

    override fun onEntering(roomId: String, user: QLiveUser) {
        super.onEntering(roomId, user)
        this.roomId = roomId
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        lazyFreshJob.start()
    }

    override fun onDestroyed() {
        super.onDestroyed()
        lazyFreshJob.cancel()
    }
    override fun onLeft() {
        super.onLeft()
        roomId = ""
        adapter.setNewData(ArrayList<QLiveUser>())
        lazyFreshJob.cancel()
    }
}

