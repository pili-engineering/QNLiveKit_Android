package sensetime.senseme.com.effects.adapter

import android.graphics.PorterDuff
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.utils.ContextHolder
import sensetime.senseme.com.effects.utils.GlideUtils
import sensetime.senseme.com.effects.view.StickerItem
import sensetime.senseme.com.effects.view.StickerState

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 2021/8/14 9:39 下午
 */
class TryOnAdapter : BaseQuickAdapter<StickerItem?, BaseViewHolder>(R.layout.item_makeup, null) {
    override fun convert(holder: BaseViewHolder, item: StickerItem?) {
        GlideUtils.loadCircle(item?.iconUrl, holder.getView(R.id.iv_icon))
        holder.setText(R.id.tv_title, item?.name)
        when (item?.selected) {
            true -> {
                holder.getView<ShapeableImageView>(R.id.iv_icon)
                    .setStrokeColorResource(R.color.color_dd90fa)
            }
            else -> {
                holder.getView<ShapeableImageView>(R.id.iv_icon)
                    .setStrokeColorResource(android.R.color.transparent)
            }
        }

        holder.getView<ContentLoadingProgressBar>(R.id.progressbar).indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(
                ContextHolder.getContext(),
                R.color.color_f383fd
            ), PorterDuff.Mode.MULTIPLY
        )
        when (item?.state) {
            StickerState.NORMAL_STATE -> {
                holder.getView<ContentLoadingProgressBar>(R.id.progressbar).visibility =
                    View.INVISIBLE
            }
            StickerState.LOADING_STATE -> {
                holder.getView<ContentLoadingProgressBar>(R.id.progressbar).visibility =
                    View.VISIBLE
            }
            else -> {
                holder.getView<ContentLoadingProgressBar>(R.id.progressbar).visibility =
                    View.INVISIBLE
            }
        }


    }
}