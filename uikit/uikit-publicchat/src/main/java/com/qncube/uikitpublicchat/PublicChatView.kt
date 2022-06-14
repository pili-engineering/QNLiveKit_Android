package com.qncube.uikitpublicchat

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.qncube.publicchatservice.PubChatModel
import com.qncube.publicchatservice.QNPublicChatService
import com.qncube.uikitcore.*
import kotlinx.android.synthetic.main.kit_view_publicchatslotview.view.*

class PublicChatView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mRoomNoticeHeader: RoomNoticeView? = null
    private var hasHeader = false
    private val mAdapter = PubChatAdapter().apply {
        mAvatarClickCall = { item: PubChatModel, view: View ->
            //点击头像事件
        }
    }

    //消息监听
    private val mPublicChatServiceLister = QNPublicChatService.QNPublicChatServiceLister {
        if (it.senderRoomId != roomInfo?.liveId) {
            return@QNPublicChatServiceLister
        }
        mAdapter.addData(it)
        val position = if (hasHeader) {
            mAdapter.data.size
        } else {
            mAdapter.data.size - 1
        }
        recyChat.smoothScrollToPosition(position)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
        if (event == Lifecycle.Event.ON_DESTROY) {
            client?.getService(QNPublicChatService::class.java)
                ?.removePublicChatServiceLister(mPublicChatServiceLister)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_view_publicchatslotview
    }

    override fun initView() {
        client!!.getService(QNPublicChatService::class.java)
            .addPublicChatServiceLister(mPublicChatServiceLister)

        recyChat.layoutManager = LinearLayoutManager(context)
        recyChat.adapter = mAdapter
        if (RoomNoticeView.isAddToPubChatListHeader) {
            mRoomNoticeHeader = RoomNoticeView(context)
            hasHeader = true
            mRoomNoticeHeader?.attachKitContext(kitContext!!)
            mRoomNoticeHeader?.attachLiveClient(client!!)
            mAdapter.addHeaderView(mRoomNoticeHeader)
        }
    }

    override fun onLeft() {
        super.onLeft()
        mAdapter.data.clear()
        mAdapter.notifyDataSetChanged()
    }

}