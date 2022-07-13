package com.qlive.uikitshopping

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qlive.core.QLiveClient
import com.qlive.shoppingservice.QItem
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.dialog.FinalDialogFragment
import com.qlive.uikitcore.refresh.SmartRecyclerView

class ShoppingManagerDialog(
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
        var layoutId = R.layout.kit_dialog_shopping_manager

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
    }

    override fun getViewLayoutId(): Int {
        return R.layout.kit_dialog_shopping_manager
    }

    override fun init() {

    }

    class ShoppingManagerPage : SmartRecyclerView {

        constructor(context: Context) : this(context, null)
        constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
        ) {

        }
       
    }

}