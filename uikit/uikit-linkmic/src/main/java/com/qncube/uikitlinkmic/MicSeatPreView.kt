package com.qncube.uikitlinkmic
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.qncube.linkmicservice.QLinkMicService
import com.qncube.liveroomcore.been.QMicLinker
import com.qncube.uikitcore.LinkerUIHelper
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

        val tempLp: LinearLayout.LayoutParams =
            itemView.tempView.layoutParams as LinearLayout.LayoutParams
        tempLp.height = LinkerUIHelper.micBottomUIMargin
        itemView.tempView.layoutParams = tempLp

        val sfLp = itemView.flSurfaceContainer.layoutParams
        sfLp.width = LinkerUIHelper.uiMicWidth
        sfLp.height = LinkerUIHelper.uiMicHeight
        itemView.flSurfaceContainer.layoutParams = sfLp
        addView(itemView)

        val container = itemView.flSurfaceContainer as FrameLayout
        val size = Math.min(LinkerUIHelper.uiMicWidth, LinkerUIHelper.uiMicHeight)
        container.addView(
            RoundTextureView(context).apply {
                linkService.setUserPreview(micLinker.user?.userId ?: "", this)
                setRadius((size / 2).toFloat())
            },
            FrameLayout.LayoutParams(
                size,
                size,
                Gravity.CENTER
            )
        )
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
    }
}