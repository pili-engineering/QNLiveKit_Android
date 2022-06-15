package com.qncube.liveuikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.linveroominner.asToast
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveuikit.R
import com.qncube.liveuikit.RoomPage
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.QComponent
import com.qncube.uikitcore.ext.ViewUtil
import com.qncube.uikitcore.ext.bg
import com.qncube.uikitcore.refresh.CommonEmptyView
import kotlinx.android.synthetic.main.kit_roomlist_item_room.view.*
import kotlinx.android.synthetic.main.kit_view_room_list.view.*

class RoomListView : FrameLayout, QComponent {
    override var kitContext: KitContext? = null

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
    private var mLifecycleOwner: LifecycleOwner? = null

    override fun attachKitContext(context: KitContext) {
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
        mLifecycleOwner?.lifecycleScope?.launchWhenResumed {
            mSmartRecyclerView.startRefresh()
        }
    }

    private fun load(page: Int) {
        mLifecycleOwner?.bg {
            doWork {
                QLiveDelegate.qRooms.listRoom(page + 1, 20)
                mSmartRecyclerView.onFetchDataFinish(data.list, true)
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
            Glide.with(mContext).load(item.coverUrl)
                .apply(RequestOptions().transform(RoundedCorners(ViewUtil.dip2px(8f))))
                .into(helper.itemView.ivCover)
            helper.itemView.tvRoomId.text = item.anchor.nick
            helper.itemView.tvRoomName.text = item.title
            helper.itemView.tvOnlineCount.text = item.onlineCount.toString()
        }
    }
}