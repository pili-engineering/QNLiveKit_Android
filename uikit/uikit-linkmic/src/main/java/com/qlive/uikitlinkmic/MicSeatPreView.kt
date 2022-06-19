package com.qlive.uikitlinkmic

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.qlive.linkmicservice.QLinkMicService
import com.qlive.core.been.QMicLinker
import com.qlive.uikitcore.LinkerUIHelper
import com.qlive.uikitcore.ext.ViewUtil
import kotlinx.android.synthetic.main.kit_item_linker_surface.view.*

//麦位预览
class MicSeatPreView : LinearLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun addItemView(micLinker: QMicLinker, linkService: QLinkMicService) {
        val itemView: View =
            LayoutInflater.from(context).inflate(R.layout.kit_item_linker_surface, this, false)

        val sfLp = itemView.llContiner.layoutParams
        sfLp.width = LinkerUIHelper.uiMicWidth
        sfLp.height = LinkerUIHelper.uiMicHeight + LinkerUIHelper.micBottomUIMargin

        itemView.llContiner.layoutParams = sfLp

        addView(itemView)

        val container = itemView.flSurfaceContainer as FrameLayout
        val size = ViewUtil.dip2px(96f)
        container.addView(
            RoundTextureView(context).apply {
                linkService.setUserPreview(micLinker.user?.userId ?: "", this)
                setRadius((size / 2).toFloat())
            },
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
            )
        )

        itemView.ivMicStatus.isSelected = micLinker.isOpenMicrophone
        if (micLinker.isOpenCamera) {
            itemView.ivMicStatus.visibility = View.VISIBLE
        } else {
            itemView.ivMicStatus.visibility = View.GONE
        }
    }

    fun removeItem(index: Int) {
        if (index > childCount - 1) {
            return
        }
        removeViewAt(index)
    }

    fun convertItem(index: Int, item: QMicLinker) {
        if (index > childCount - 1) {
            return
        }
        val container = getChildAt(index).flSurfaceContainer
        if (item.isOpenCamera) {
            container.visibility = View.VISIBLE
        } else {
            container.visibility = View.INVISIBLE
        }
        val mic = getChildAt(index).ivMicStatus
        mic.isSelected = item.isOpenMicrophone
        if (item.isOpenCamera) {
            mic.visibility = View.VISIBLE
        } else {
            mic.visibility = View.GONE
        }
    }

    fun clear() {
        removeAllViews()
    }
}