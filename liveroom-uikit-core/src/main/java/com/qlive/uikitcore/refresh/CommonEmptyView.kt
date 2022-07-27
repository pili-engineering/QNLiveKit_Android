package com.qlive.uikitcore.refresh

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.qlive.uikitcore.R
import com.qlive.uikitcore.refresh.IEmptyView
import com.qlive.uikitcore.refresh.NetUtil

/**
 * 通用empty 待替换ui设计
 */
class CommonEmptyView : FrameLayout, View.OnClickListener, IEmptyView {
    private var clickEnable = true
    private var listener: OnClickListener? = null

    /**
     * 获取当前错误状态
     *
     * @return
     */
    var errorState = 0
        private set
    private var strNoDataContent = ""
    var img: ImageView? = null
    var emptyText: TextView? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private var emptyIcon = R.drawable.kit_pic_empty
    private var emptyNoNetIcon = R.drawable.kit_pic_empty_network
    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.kit_view_custom_empty, this)
        visibility = GONE
        // setBackgroundColor(-1);
        setOnClickListener(this)
        img = findViewById(R.id.img)
        emptyText = findViewById(R.id.empty_text)
        img?.setOnClickListener(this)
    }

    /**
     * 設置背景
     */
    fun setEmptyIcon(imgResource: Int) {
        emptyIcon = imgResource
        img!!.setImageResource(imgResource)
    }

    fun setEmptyNoNetIcon(imgResource: Int) {
        emptyNoNetIcon = imgResource
    }

    /**
     * 設置内容
     */
    fun setEmptyTips(noDataContent: String) {
        strNoDataContent = noDataContent
        if (emptyText != null) {
            emptyText!!.text = strNoDataContent
        }
    }

    override fun getContentView(): View {
        return this
    }

    /**
     * 根据状态設置当前view
     *
     * @param i
     */
    override fun setStatus(i: Int) {
        val disconnected = !NetUtil.isNetworkAvailable(context)
        if (refreshingView != null) {
            refreshingView!!.visibility = GONE
        }
        when (i) {
            IEmptyView.NETWORK_ERROR -> {
                visibility = VISIBLE
                errorState = IEmptyView.NETWORK_ERROR
                emptyText!!.text = "网络错误"
                img!!.setImageResource(emptyNoNetIcon)
                img!!.visibility = VISIBLE
                clickEnable = true
            }
            IEmptyView.NODATA -> {
                visibility = VISIBLE
                errorState = IEmptyView.NODATA
                img!!.setImageResource(emptyIcon)
                img!!.visibility = VISIBLE
                refreshEmptyView()
                clickEnable = true
            }
            IEmptyView.HIDE_LAYOUT -> visibility = GONE
            IEmptyView.START_REFREASH_WHEN_EMPTY -> if (refreshingView != null) {
                visibility = VISIBLE
                refreshingView!!.visibility = VISIBLE
            }
            else -> {}
        }
    }

    private var refreshingView: View? = null
    fun setRefreshingView(view: View?) {
        refreshingView = view
        refreshingView!!.visibility = GONE
        addView(refreshingView)
    }

    private fun refreshEmptyView() {
        emptyText!!.text = if (TextUtils.isEmpty(strNoDataContent)) "" else strNoDataContent
    }

    fun setOnLayoutClickListener(listener: OnClickListener?) {
        this.listener = listener
    }

    override fun setVisibility(visibility: Int) {
        if (visibility == GONE) {
            errorState = IEmptyView.HIDE_LAYOUT
        }
        super.setVisibility(visibility)
    }

    override fun onClick(v: View) {
        if (clickEnable) {
            if (errorState == IEmptyView.NETWORK_ERROR) {
                return
            }
            if (listener != null) {
                listener!!.onClick(v)
            }
        }
    }
}