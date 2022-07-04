package sensetime.senseme.com.effects.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.utils.ContextHolder
import sensetime.senseme.com.effects.utils.GlideUtils
import sensetime.senseme.com.effects.utils.LogUtils
import sensetime.senseme.com.effects.view.MakeupItem
import sensetime.senseme.com.effects.view.StickerState
import sensetime.senseme.com.effects.view.widget.ILinkageAdapter
import java.util.*

class MakeupAdapter(context: Context) : RecyclerView.Adapter<MakeupAdapter.NormalViewHolder>(),
    ILinkageAdapter {
    companion object {
        private const val TAG = "MakeupAdapter"
    }

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mMakeupList: MutableList<MakeupItem> = ArrayList()
    private var mSelectedPosition = 0
    private var mListener: ILinkageAdapter.Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalViewHolder {
        val view = mLayoutInflater.inflate(R.layout.makeup_item, parent, false)
        return NormalViewHolder(view)
    }

    override fun onBindViewHolder(holder: NormalViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val entity = mMakeupList[position]
        //holder.siIcon.setImageBitmap(mMakeupList[position].icon)
        GlideUtils.load(ContextHolder.getContext(), entity.iconUrl, holder.siIcon);
        holder.tvDes.text = mMakeupList[position].name
        bindState(getItem(position), holder, position)
        if (mSelectedPosition == position) {
            holder.tvDes.setTextColor(Color.parseColor("#ffffff"))
            holder.siIcon.setStrokeColorResource(R.color.color_dd90fa)
        } else {
            holder.tvDes.setTextColor(Color.parseColor("#A7A7A7"))
            holder.siIcon.setStrokeColorResource(android.R.color.transparent)
        }
        holder.itemView.setOnClickListener {
            if (mListener != null) {
                if (mSelectedPosition == position) {
                    mSelectedPosition = 0
                    LogUtils.iTag(TAG, "onBindViewHolder: $mSelectedPosition")
                    mListener?.onItemClickSticker(0, entity, false, this@MakeupAdapter)
                } else {
                    mSelectedPosition = position
                    LogUtils.iTag(TAG, "onBindViewHolder: $mSelectedPosition")
                    mListener?.onItemClickSticker(position, entity, true, this@MakeupAdapter)
                }
                notifyDataSetChanged()
            }
        }
    }

    /**
     * loading 状态绑定
     *
     * @param stickerItem
     * @param holder
     * @param position
     */
    private fun bindState(stickerItem: MakeupItem?, holder: NormalViewHolder?, position: Int) {
        if (stickerItem != null) {
            when (stickerItem.state) {
                StickerState.NORMAL_STATE ->                     //设置为等待下载状态
                    if (holder!!.normalState.visibility != View.VISIBLE) {
                        LogUtils.iTag("StickerAdapter", "NORMAL_STATE")
                        holder.normalState.visibility = View.VISIBLE
                        holder.downloadingState.visibility = View.INVISIBLE
                        holder.downloadingState.isActivated = false
                        holder.loadingStateParent.visibility = View.INVISIBLE
                    }
                StickerState.LOADING_STATE ->                     //设置为loading 状态
                    if (holder!!.downloadingState.visibility != View.VISIBLE) {
                        LogUtils.iTag("StickerAdapter", "LOADING_STATE")
                        holder.normalState.visibility = View.INVISIBLE
                        holder.downloadingState.isActivated = true
                        holder.downloadingState.visibility = View.VISIBLE
                        holder.loadingStateParent.visibility = View.VISIBLE
                    }
                StickerState.DONE_STATE ->                     //设置为下载完成状态
                    if (holder!!.normalState.visibility != View.INVISIBLE || holder.downloadingState.visibility != View.INVISIBLE) {
                        LogUtils.iTag("StickerAdapter", "DONE_STATE")
                        holder.normalState.visibility = View.INVISIBLE
                        holder.downloadingState.visibility = View.INVISIBLE
                        holder.downloadingState.isActivated = false
                        holder.loadingStateParent.visibility = View.INVISIBLE
                    }
            }
        }
    }

    fun getItem(position: Int): MakeupItem? {
        return if (position >= 0 && position < itemCount) {
            mMakeupList[position]
        } else null
    }

    override fun getItemCount(): Int {
        return mMakeupList.size
    }

//    override fun refreshData(data: List<*>) {
//        TODO("Not yet implemented")
//    }
//
//    fun refreshData(data: List<*>?) {
//    }

    override fun refreshData(data: MutableList<*>) {
        mMakeupList.clear()
        mMakeupList.addAll((data as Collection<MakeupItem>?)!!)
        notifyDataSetChanged()
    }

    override fun getAdapter(): RecyclerView.Adapter<*> {
        return this
    }

    override fun setListener(listener: ILinkageAdapter.Listener) {
        mListener = listener
    }

    class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val siIcon = itemView.findViewById<ShapeableImageView>(R.id.si_icon)
        val tvDes = itemView.findViewById<TextView>(R.id.tv_des)

        //var imageView: RoundImageView
        //var textView: TextView
        var normalState: ImageView
        var downloadingState: ImageView
        var loadingStateParent: ViewGroup

        init {
            //imageView = itemView.findViewById<View>(R.id.iv_makeup_image) as RoundImageView
            //textView = itemView.findViewById<View>(R.id.makeup_text) as TextView
            normalState = itemView.findViewById<View>(R.id.normalState) as ImageView
            downloadingState = itemView.findViewById<View>(R.id.downloadingState) as ImageView
            loadingStateParent = itemView.findViewById<View>(R.id.loadingStateParent) as ViewGroup
        }
    }

    val data: List<MakeupItem>
        get() = mMakeupList

    override fun setSelectedPosition(position: Int) {
        LogUtils.iTag(TAG, "setSelectedPosition() called with: position = $position")
        var position = position
        if (position < 0) {
            position = 0
        }
        mSelectedPosition = position
        notifyDataSetChanged()
    }
}