package sensetime.senseme.com.effects.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.RecyclerView
import sensetime.senseme.com.effects.utils.LogUtils
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.utils.ContextHolder
import sensetime.senseme.com.effects.utils.GlideUtils
import sensetime.senseme.com.effects.view.StickerItem
import sensetime.senseme.com.effects.view.StickerState

/**
 * Created by luguoqiang on 06.16.2021.
 */
class StyleContentAdapter(context: Context) :
    RecyclerView.Adapter<StyleContentAdapter.NormalViewHolder>() {
    companion object {
        const val TAG = "StyleContentAdapter"
        const val MAX_SCALE = 1.3f
    }

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)

    private val mData: ArrayList<StickerItem> = ArrayList()
    private var currentSelectedPosition = -1

    private var parentRecycler: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        parentRecycler = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalViewHolder {
        val view = mLayoutInflater.inflate(R.layout.item_style_content, parent, false)
        return NormalViewHolder(view)
    }

    override fun onBindViewHolder(holder: NormalViewHolder, position: Int) {
        val entity = mData[position]
        holder.progressbar.indeterminateDrawable.setColorFilter(
            ContextCompat.getColor(
                ContextHolder.getContext(),
                R.color.color_f383fd
            ), PorterDuff.Mode.MULTIPLY
        )
//        holder.imageView.setImageBitmap(entity.icon)
        GlideUtils.loadCircle(entity.iconUrl, holder.imageView)
//        holder.imageView.setImageResource(forecast.cityIcon)
        if (position == currentSelectedPosition) {
            holder.imageView.scaleX = MAX_SCALE
            holder.imageView.scaleY = MAX_SCALE
            holder.iv_bg.visibility = View.VISIBLE
        } else {
            holder.imageView.scaleX = 1.0f
            holder.imageView.scaleY = 1.0f
            holder.iv_bg.visibility = View.INVISIBLE
        }

        when (entity.state) {
            StickerState.NORMAL_STATE -> {
                holder.progressbar.visibility = View.INVISIBLE
            }
            StickerState.LOADING_STATE -> {
                holder.progressbar.visibility = View.VISIBLE
            }
            else -> {
                holder.progressbar.visibility = View.INVISIBLE
            }
        }

        holder.container.setOnClickListener {
            LogUtils.iTag(TAG, "currentSelectedPosition:$currentSelectedPosition , position:$position")
            if ((currentSelectedPosition == -1 && position == 2) || mData.size == 1) {
                LogUtils.iTag(TAG, "onBindViewHolder: ----------")
                mListener?.onItemClickStyleContent(position, entity)
            } else {
                parentRecycler?.smoothScrollToPosition(position)
            }
            currentSelectedPosition = position
            //setSelectPosition(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_bg: ImageView = itemView.findViewById(R.id.iv_bg)
        val imageView: ImageView = itemView.findViewById(R.id.city_image)
        val container: ConstraintLayout = itemView.findViewById(R.id.container)
        val tv_name: TextView = itemView.findViewById(R.id.tv_name)
        val progressbar: ContentLoadingProgressBar = itemView.findViewById(R.id.progressbar)
    }

    fun refreshData(data: List<StickerItem>?) {
        try {
            mData.clear()
            data?.let { mData.addAll(it) }
            notifyDataSetChanged()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun setSelectPosition(selectPosition: Int) {
        try {
            currentSelectedPosition = selectPosition
            LogUtils.iTag(TAG, "setSelectPosition: $currentSelectedPosition")
            notifyDataSetChanged()
        } catch (e: Exception) {
            //e.printStackTrace()
        }
    }

    fun setSelectPosition2(selectPosition: Int) {
        currentSelectedPosition = selectPosition
    }

    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onItemClickStyleContent(position: Int, entity: StickerItem)
    }
}