package com.qlive.uikitpublicchat

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.qlive.core.QLiveClient
import com.qlive.core.been.QPublicChat
import com.qlive.pubchatservice.QPublicChatService
import com.qlive.pubchatservice.QPublicChatServiceLister
import com.qlive.uikitcore.*

class PublicChatView : QKitRecyclerView {

    private val mAdapter = PubChatAdapter().apply {
        mAvatarClickCall = { item: QPublicChat, view: View ->
            //点击头像事件
        }
    }

    //消息监听
    private val mPublicChatServiceLister =
        QPublicChatServiceLister {
            mAdapter.addData(it)
            val position = mAdapter.data.size - 1
            this.post {
                this.smoothScrollToPosition(position)
            }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.layoutManager = LinearLayoutManager(context)
        this.adapter = mAdapter
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
    }

    override fun attachLiveClient(client: QLiveClient) {
        super.attachLiveClient(client)
        client.getService(QPublicChatService::class.java)
            .addServiceLister(mPublicChatServiceLister)
    }

    override fun onLeft() {
        super.onLeft()
        mAdapter.data.clear()
        mAdapter.notifyDataSetChanged()
    }
}