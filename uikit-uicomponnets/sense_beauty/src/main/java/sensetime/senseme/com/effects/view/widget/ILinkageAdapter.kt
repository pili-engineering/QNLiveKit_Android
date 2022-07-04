package sensetime.senseme.com.effects.view.widget

import androidx.recyclerview.widget.RecyclerView

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/23/21 8:45 PM
 */
interface ILinkageAdapter {

    fun refreshData(data: MutableList<*>)
    fun getAdapter(): RecyclerView.Adapter<*>
    fun setSelectedPosition(position: Int)

    fun setListener(listener: Listener)

    interface Listener {
        fun onItemClickSticker(position: Int, data: LinkageEntity, selected: Boolean, adapter: RecyclerView.Adapter<*>)
    }
}