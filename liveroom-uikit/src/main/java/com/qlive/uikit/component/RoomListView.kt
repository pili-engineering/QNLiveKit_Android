package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qlive.coreimpl.QLiveDelegate
import com.qlive.core.QLiveCallBack
import com.qlive.coreimpl.asToast
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.uikit.R
import com.qlive.uikit.RoomPage
import com.qlive.uikitcore.QUIKitContext
import com.qlive.uikitcore.QComponent
import com.qlive.uikitcore.ext.ViewUtil
import com.qlive.uikitcore.ext.bg
import com.qlive.uikitcore.refresh.CommonEmptyView
import kotlinx.android.synthetic.main.kit_roomlist_item_room.view.*
import kotlinx.android.synthetic.main.kit_view_room_list.view.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RoomListView : FrameLayout, QComponent {
    override var kitContext: QUIKitContext? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.kit_view_room_list, this, true)
        mSmartRecyclerView.recyclerView.layoutManager = GridLayoutManager(context, 2)
    }

    private val mAdapter = RoomListAdapter()

    override fun attachKitContext(context: QUIKitContext) {
        super.attachKitContext(context)
        mSmartRecyclerView.setUp(
            mAdapter,
            CommonEmptyView(context.androidContext),
            10,
            true,
            true
        ) {
            load(it)
        }
        mAdapter.goJoinCall = {
            RoomPage.joinRoom(context.currentActivity, it, object :
                QLiveCallBack<QLiveRoomInfo> {
                override fun onError(code: Int, msg: String?) {
                    msg?.asToast()
                }

                override fun onSuccess(data: QLiveRoomInfo?) {
                }
            })
        }

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
        if (event == Lifecycle.Event.ON_RESUME) {
            mSmartRecyclerView.startRefresh()
        }
    }

    private suspend fun suspendLoad(page: Int) = suspendCoroutine<List<QLiveRoomInfo>> { ct ->
        QLiveDelegate.qRooms.listRoom(page + 1, 20, object : QLiveCallBack<List<QLiveRoomInfo>> {
            override fun onError(code: Int, msg: String?) {
                ct.resumeWithException(Exception(msg))
            }

            override fun onSuccess(data: List<QLiveRoomInfo>?) {
                ct.resume(data ?: ArrayList<QLiveRoomInfo>())
            }
        })
    }

    private fun load(page: Int) {
        kitContext?.lifecycleOwner?.bg {
            doWork {
                val list = suspendLoad(page)
                mSmartRecyclerView.onFetchDataFinish(list, true)
            }
            catchError {
                mSmartRecyclerView.onFetchDataError()
            }
        }
    }

    class RoomListAdapter : BaseQuickAdapter<QLiveRoomInfo, BaseViewHolder>(
        R.layout.kit_roomlist_item_room,
        ArrayList()
    ) {

        var goJoinCall: (item: QLiveRoomInfo) -> Unit = {}
        override fun convert(helper: BaseViewHolder, item: QLiveRoomInfo) {
            helper.itemView.setOnClickListener {
                goJoinCall.invoke(item)
            }
            Glide.with(mContext).load(item.coverURL)
                .apply(RequestOptions().transform(RoundedCorners(ViewUtil.dip2px(8f))))
                .into(helper.itemView.ivCover)
            helper.itemView.tvRoomId.text = item.anchor.nick
            helper.itemView.tvRoomName.text = item.title
            helper.itemView.tvOnlineCount.text = item.onlineCount.toString()
        }
    }
}