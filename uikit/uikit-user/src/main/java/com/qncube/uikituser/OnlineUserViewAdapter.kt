package com.qncube.uikituser

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qncube.liveroomcore.been.QLiveUser
import kotlinx.android.synthetic.main.kit_item_online_user.view.*

class OnlineUserViewAdapter : BaseQuickAdapter<QLiveUser, BaseViewHolder>(
    R.layout.kit_item_online_user,
    ArrayList<QLiveUser>()
) {
    override fun convert(helper: BaseViewHolder, item: QLiveUser) {
        Glide.with(mContext)
            .load(item.avatar)
            .into(helper.itemView.ivAvatar)
    }
}