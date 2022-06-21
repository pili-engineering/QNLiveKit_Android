package com.qlive.uikitpk

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qlive.core.been.QLiveRoomInfo
import kotlinx.android.synthetic.main.kit_item_pkable.view.*

class PKAnchorListAdapter :
    BaseQuickAdapter<QLiveRoomInfo, BaseViewHolder>(R.layout.kit_item_pkable, ArrayList()) {
    var inviteCall: (room: QLiveRoomInfo) -> Unit = {
    }
    override fun convert(helper: BaseViewHolder, item: QLiveRoomInfo) {
        Glide.with(mContext).load(item.anchor.avatar)
            .into(helper.itemView.ivAvatar)
        helper.itemView.tvRoomName.text = item.title
        helper.itemView.tvAnchorName.text = item.anchor.nick
        helper.itemView.ivInvite.setOnClickListener {
            inviteCall.invoke(item)
        }
    }
}