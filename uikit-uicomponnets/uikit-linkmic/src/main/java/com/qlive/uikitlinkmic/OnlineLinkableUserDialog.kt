package com.qlive.uikitlinkmic

import android.graphics.Color
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qlive.chatservice.QChatRoomService
import com.qlive.core.QLiveCallBack
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.roomservice.QRoomService
import com.qlive.sdk.QLive
import com.qlive.uikitcore.dialog.FinalDialogFragment
import com.qlive.uikitcore.ext.ViewUtil
import com.qlive.uikitcore.ext.bg
import com.qlive.uikitcore.refresh.CommonEmptyView
import com.qlive.uikitcore.view.SimpleDividerDecoration
import kotlinx.android.synthetic.main.kit_item_linkable.view.*
import kotlinx.android.synthetic.main.kit_online_linkable_dialog.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class OnlineLinkableUserDialog(private val roomService: QRoomService) : FinalDialogFragment() {

    init {
        applyGravityStyle(Gravity.BOTTOM)
    }

    private val mAdapter = OnlineUserAdapter()
    override fun getViewLayoutId(): Int {
        return R.layout.kit_online_linkable_dialog
    }
    fun setInviteCall(inviteCall: (room: QLiveUser) -> Unit) {
        mAdapter.inviteCall = inviteCall
    }
    private suspend fun suspendLoad(page: Int) = suspendCoroutine<List<QLiveUser>> { ct ->
        roomService.getOnlineUser(page + 1, 20, object : QLiveCallBack<List<QLiveUser>> {
            override fun onError(p0: Int, msg: String?) {
                ct.resumeWithException(Exception(msg))
            }

            override fun onSuccess(data: List<QLiveUser>?) {
                ct.resume(data ?: ArrayList<QLiveUser>())
            }
        })
    }

    private fun load(page: Int) {
        bg {
            doWork {
                val data = suspendLoad(page).filter {
                    it.userId != QLive.getLoginUser().userId
                }
                mSmartRecyclerView.onFetchDataFinish(data, true)
            }
            catchError {
                mSmartRecyclerView.onFetchDataError()
            }
        }
    }

    override fun init() {
        mSmartRecyclerView.recyclerView.addItemDecoration(
            SimpleDividerDecoration(
                requireContext(),
                Color.parseColor("#EAEAEA"), ViewUtil.dip2px(1f)
            )
        )
        mSmartRecyclerView.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mSmartRecyclerView.setUp(mAdapter, 3, true, true) {
            load(it)
        }
        mSmartRecyclerView.startRefresh()
    }

    class OnlineUserAdapter : BaseQuickAdapter<QLiveUser, BaseViewHolder>(
        R.layout.kit_item_linkable,
        ArrayList<QLiveUser>()
    ) {
        var inviteCall: (user: QLiveUser) -> Unit = {
        }

        override fun convert(helper: BaseViewHolder, item: QLiveUser) {
            Glide.with(mContext).load(item.avatar)
                .into(helper.itemView.ivAvatar)
            helper.itemView.tvUserName.text = item.nick
            helper.itemView.ivInvite.setOnClickListener {
                inviteCall.invoke(item)
            }
        }
    }
}