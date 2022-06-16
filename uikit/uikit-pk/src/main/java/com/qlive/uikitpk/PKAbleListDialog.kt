package com.qlive.uikitpk

import android.graphics.Color
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.coreimpl.datesource.RoomDataSource
import com.qlive.uikitcore.dialog.FinalDialogFragment
import com.qlive.uikitcore.ext.ViewUtil
import com.qlive.uikitcore.ext.bg
import com.qlive.uikitcore.refresh.CommonEmptyView
import com.qlive.uikitcore.view.SimpleDividerDecoration
import kotlinx.android.synthetic.main.kit_dialog_pklist.*

/**
 * pk列表弹窗
 */
class PKAbleListDialog() : FinalDialogFragment() {

    init {
        applyGravityStyle(Gravity.BOTTOM)
    }

    private val mRoomDataSource = RoomDataSource()
    private val mAdapter = PKAnchorListAdapter()

    fun setInviteCall(inviteCall: (room: QLiveRoomInfo) -> Unit) {
        mAdapter.inviteCall = inviteCall
    }

    override fun getViewLayoutId(): Int {
        return R.layout.kit_dialog_pklist
    }

    private fun load(page: Int) {
        bg {
            doWork {
                val data = mRoomDataSource.listRoom(page + 1, 20)
                mSmartRecyclerView.onFetchDataFinish(data.list, true)
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
        mSmartRecyclerView.setUp(mAdapter, CommonEmptyView(requireContext()), 3, true, true) {
            load(it)
        }
        mSmartRecyclerView.startRefresh()

    }
}

