package com.qlive.uikitpublicchat

import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qlive.core.been.QPublicChat
import com.qlive.uikitcore.ext.toHtml
import kotlinx.android.synthetic.main.kit_item_pubcaht.view.*

class PubChatAdapter :
    BaseQuickAdapter<QPublicChat, BaseViewHolder>(R.layout.kit_item_pubcaht, ArrayList()) {

    var mAvatarClickCall: (item: QPublicChat, view: View) -> Unit = { i, v -> }
    override fun convert(helper: BaseViewHolder, item: QPublicChat) {
        Glide.with(mContext)
            .load(item.sendUser.avatar)
            .into(helper.itemView.ivAvatar)
        helper.itemView.tvName.text = item.sendUser.nick
        helper.itemView.tvContent.text = showHtml(item).toHtml() ?: ""
        helper.itemView.ivAvatar.setOnClickListener {
            mAvatarClickCall.invoke(item, it)
        }
    }

    private fun showHtml(mode: QPublicChat): String {
        return "<font color='#ffffff'>${mode.content}</font>"
    }

}