package com.qlive.uikitlinkmic

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qlive.core.been.QMicLinker
import com.qlive.uikitcore.LinkerUIHelper
import kotlinx.android.synthetic.main.kit_item_linker.view.*

/***
 *麦位item
 */
class LinkerAdapter() :
    BaseQuickAdapter<QMicLinker, BaseViewHolder>(R.layout.kit_item_linker, ArrayList()) {

    var isOnMic: () -> Boolean = {
        false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val helper = super.onCreateViewHolder(parent, viewType)

        val sfLp = helper.itemView.tempMix.layoutParams
        sfLp.width = LinkerUIHelper.uiMicWidth
        sfLp.height = LinkerUIHelper.uiMicHeight + LinkerUIHelper.micBottomUIMargin
        helper.itemView.tempMix.layoutParams = sfLp

        val lp = helper.itemView.tempMix.layoutParams as FrameLayout.LayoutParams
        lp.marginEnd = LinkerUIHelper.micRightUIMargin

        return helper
    }

    override fun convert(helper: BaseViewHolder, item: QMicLinker) {
        if (!item.isOpenCamera) {
            helper.itemView.flAudioMic.visibility = View.VISIBLE
            helper.itemView.ivMicStatusMix.visibility = View.INVISIBLE
        } else {
            //开摄像头
            helper.itemView.flAudioMic.visibility = View.INVISIBLE
            if (isOnMic()) {
                helper.itemView.ivMicStatusMix.visibility = View.INVISIBLE
            } else {
                helper.itemView.ivMicStatusMix.visibility = View.VISIBLE
            }
        }

        Glide.with(mContext).load(item.user.avatar)
            .into(helper.itemView.ivAvatarInner)
        helper.itemView.ivMicStatusInner.isSelected = item.isOpenMicrophone
        helper.itemView.ivMicStatusMix.isSelected = item.isOpenMicrophone
    }

}




