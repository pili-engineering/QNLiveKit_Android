package com.qlive.uikitpublicchat

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qlive.core.QLiveClient
import com.qlive.pubchatservice.QPublicChat
import com.qlive.pubchatservice.QPublicChatService
import com.qlive.pubchatservice.QPublicChatServiceLister
import com.qlive.uikitcore.*

//公屏
class PublicChatView : QKitRecyclerView {

    companion object {
        /**
         * 点击事件
         */
        var onItemMsgClickListener: (context: QLiveUIKitContext, client: QLiveClient,view: View, msgMode: QPublicChat) -> Unit =
            { _, _,_, _ -> }

        /**
         * 列表适配器
         */
        var adapterProvider: (context: QLiveUIKitContext, client: QLiveClient) -> BaseQuickAdapter<QPublicChat, BaseViewHolder> = {
           _,_->
            PubChatAdapter()
        }
    }

    private val mAdapter by lazy {
        adapterProvider.invoke(kitContext!!,client!!)
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

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
    }

    override fun attachLiveClient(client: QLiveClient) {
        super.attachLiveClient(client)
        client.getService(QPublicChatService::class.java)
            .addServiceLister(mPublicChatServiceLister)
        this.adapter = mAdapter
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            onItemMsgClickListener.invoke(kitContext!!,client, view, mAdapter.data[position])
        }
    }

    override fun onLeft() {
        super.onLeft()
        mAdapter.data.clear()
        mAdapter.notifyDataSetChanged()
    }
}