package com.qlive.uikitshopping

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Paint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qlive.core.QLiveCallBack
import com.qlive.core.QLiveClient
import com.qlive.core.been.QExtension
import com.qlive.shoppingservice.QItem
import com.qlive.shoppingservice.QShoppingService
import com.qlive.shoppingservice.QShoppingServiceListener
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.Scheduler
import com.qlive.uikitcore.dialog.FinalDialogFragment
import com.qlive.uikitcore.refresh.CommonEmptyView
import com.qlive.uikitshopping.TagItem.Companion.strToTagItem
import com.qlive.uikitshopping.ui.flowlayout.FlowLayout
import com.qlive.uikitshopping.ui.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.kit_dialog_player_shopping.*
import kotlinx.android.synthetic.main.kit_item_player_goods.view.*

class PlayerShoppingDialog(
    private val kitContext: QLiveUIKitContext,
    private val client: QLiveClient
) : FinalDialogFragment() {

    /**
     * Companion静态配置
     * 用户自定义 UI和事件入口
     * @constructor Create empty Companion
     */
    companion object {
        /**
         * 自定义布局
         */
        var layoutId = R.layout.kit_dialog_player_shopping

        /**
         * 自定义列表适配
         */
        var adapterCreate: (context: QLiveUIKitContext, client: QLiveClient) -> BaseQuickAdapter<QItem, BaseViewHolder>? =
            { _, _ ->
                null
            }

        /**
         * 点击事件
         */
        var onItemClickListener: (context: QLiveUIKitContext, client: QLiveClient, view: View, item: QItem) -> Unit =
            { _, _, _, _ ->

            }
    }

    init {
        applyGravityStyle(Gravity.BOTTOM)
        applyDimAmount(0f)
    }

    private val shoppingService get() = client.getService(QShoppingService::class.java)!!
    private val adapter = adapterCreate.invoke(kitContext, client) ?: PlayerShoppingGoodsAdapter()

    var lastExplainingIndex = -1
    private val mShoppingServiceListener = object : QShoppingServiceListener {
        @SuppressLint("NotifyDataSetChanged")
        override fun onExplainingUpdate(item: QItem?) {
            if (item == null) {
                if (lastExplainingIndex >= 0) {
                    adapter.notifyItemChanged(lastExplainingIndex)
                    lastExplainingIndex = 0
                }
            } else {
                if (lastExplainingIndex >= 0) {
                    adapter.notifyItemChanged(lastExplainingIndex)
                }
                lastExplainingIndex = checkExpIndex();
                if (lastExplainingIndex < 0) {
                    return
                }
                adapter.notifyItemChanged(lastExplainingIndex)
            }
        }

        override fun onExtensionUpdate(item: QItem, extension: QExtension) {
        }

        override fun onItemListUpdate() {
            loadItem()
        }

        private fun checkExpIndex(): Int {
            var lastIndex = -1
            adapter.data.forEachIndexed { index, qItem ->
                if (shoppingService.explaining.itemID == qItem.itemID) {
                    lastIndex = index
                }
            }
            return lastIndex;
        }
    }

    override fun getViewLayoutId(): Int {
        return layoutId
    }

    private fun loadItem() {
        shoppingService.getItemList(object : QLiveCallBack<List<QItem>> {
            override fun onError(code: Int, msg: String) {
                recyclerViewGoods?.onFetchDataError()
            }

            override fun onSuccess(data: List<QItem>) {
                data.forEachIndexed { index, qItem ->
                    if (qItem.itemID == shoppingService.explaining?.itemID) {
                        lastExplainingIndex = index
                    }
                }
                recyclerViewGoods?.onFetchDataFinish(data, true, true)
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        shoppingService.removeServiceListener(mShoppingServiceListener)
    }

    override fun init() {
        shoppingService.addServiceListener(mShoppingServiceListener)
        ivClose?.setOnClickListener {
            dismiss()
        }
        recyclerViewGoods.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewGoods.setUp(adapter, 3, false, true) {
            loadItem()
        }
        recyclerViewGoods.startRefresh()
    }

    private inner class PlayerShoppingGoodsAdapter : BaseQuickAdapter<QItem, BaseViewHolder>(
        R.layout.kit_item_player_goods,
        ArrayList<QItem>()
    ) {

        override fun convert(helper: BaseViewHolder, item: QItem) {
            Glide.with(mContext)
                .load(item.thumbnail)
                .into(helper.itemView.ivCover)
            helper.itemView.mAutoVoiceWaveView.attach(kitContext.lifecycleOwner)
            if (shoppingService.explaining?.itemID == item.itemID) {
                lastExplainingIndex = data.indexOf(item)
                helper.itemView.llItemShowing.visibility = View.VISIBLE
                helper.itemView.mAutoVoiceWaveView.setAutoPlay(true)
                lastExplainingIndex = data.indexOf(item)
            } else {
                helper.itemView.llItemShowing.visibility = View.GONE
                helper.itemView.mAutoVoiceWaveView.setAutoPlay(false)
            }
            helper.itemView.tvOrder.text = item.order.toString()
            helper.itemView.tvGoodsName.text = item.title

            helper.itemView.flGoodsTag.adapter =
                object : TagAdapter<TagItem>(strToTagItem(item.tags)) {
                    override fun getView(parent: FlowLayout, position: Int, t: TagItem): View {
                        val v = LayoutInflater.from(mContext)
                            .inflate(R.layout.kit_item_goods_tag, parent, false)
                        (v as TextView).text = t.tagStr
                        v.setBackgroundResource(t.color)
                        return v
                    }
                }
            helper.itemView.tvNowPrice.text = item.currentPrice
            helper.itemView.tvOriginPrice.text = item.originPrice
            helper.itemView.tvOriginPrice.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG;
            helper.itemView.tvGoBuy.setOnClickListener {
                Companion.onItemClickListener.invoke(kitContext, client, it, item)
            }
        }
    }
}
