package com.qlive.uikitpublicchat

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.qlive.core.been.QPublicChat
import com.qlive.pubchatservice.QPublicChatService
import com.qlive.pubchatservice.QPublicChatServiceLister
import com.qlive.uikitcore.*
import kotlinx.android.synthetic.main.kit_view_publicchatslotview.view.*

class PublicChatView : QBaseRoomFrameLayout {

    private val mAdapter = PubChatAdapter().apply {
        mAvatarClickCall = { item: QPublicChat, view: View ->
            //点击头像事件
        }
    }

    //消息监听
    private val mPublicChatServiceLister =
        QPublicChatServiceLister {
//            if (it.senderRoomId != roomInfo?.liveID) {
//                return@QPublicChatServiceLister
//            }
            mAdapter.addData(it)
            val position = mAdapter.data.size - 1
            recyChat.post {
                recyChat.smoothScrollToPosition(position)
            }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        recyChat.layoutManager = LinearLayoutManager(context)
        recyChat.adapter = mAdapter
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_view_publicchatslotview
    }

    override fun initView() {
        client!!.getService(QPublicChatService::class.java)
            .addServiceLister(mPublicChatServiceLister)
    }

    override fun onLeft() {
        super.onLeft()
        mAdapter.data.clear()
        mAdapter.notifyDataSetChanged()
    }

}