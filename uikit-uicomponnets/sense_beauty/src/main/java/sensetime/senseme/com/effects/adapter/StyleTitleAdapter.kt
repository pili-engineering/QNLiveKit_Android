package sensetime.senseme.com.effects.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.view.BeautyOptionsItem

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 2019/7/17 10:47
 */
class StyleTitleAdapter(context: Context) : RecyclerView.Adapter<StyleTitleAdapter.NormalViewHolder>() {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mData: ArrayList<BeautyOptionsItem> = ArrayList()
    private val mOnClickStickerListener: View.OnClickListener? = null
    private var listener: OnItemClickListener? = null
    private var currentSelectedPosition = -1

    fun refreshData(data: List<BeautyOptionsItem>?) {
        mData.clear()
        data?.let { mData.addAll(it) }
        notifyDataSetChanged()
    }

    // 返回复用的View视图
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NormalViewHolder {
        val view = mLayoutInflater.inflate(R.layout.item_style_title, parent, false)
        mOnClickStickerListener?.let {
            view.setOnClickListener(it)
        }
        return NormalViewHolder(view)
    }

    // 此处设置view绑定数据
    override fun onBindViewHolder(holder: NormalViewHolder, position: Int) {
        val entity = mData[position]
        holder.tvTitle.text = entity.name

        // 选中
        if (position == getSelectPosition()) {
            holder.line.visibility = View.VISIBLE
        } else {
            holder.line.visibility = View.INVISIBLE
        }

        // 点击事件
        holder.itemView.setOnClickListener {
            listener?.let {
                it.onItemClick(position, entity)
                currentSelectedPosition = position
                notifyDataSetChanged()
            }
        }
    }

    fun getSelectPosition(): Int {
        return currentSelectedPosition
    }

    // 返回list的长度,加为null判断
    override fun getItemCount(): Int {
        return mData.size
    }

    // 初始化itemView的控件
    inner class NormalViewHolder(itemView: View) : ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        val line: View = itemView.findViewById(R.id.line)
    }


    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setSelectPosition(selectPosition: Int) {
        currentSelectedPosition = selectPosition
        notifyDataSetChanged()
    }

    /**
     * 定义选项单击事件的回调接口
     */
    interface OnItemClickListener {
        fun onItemClick(position: Int, entity: BeautyOptionsItem)
    }
}