package sensetime.senseme.com.effects.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_button.view.*
import sensetime.senseme.com.effects.R

class HomeBtnView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mTitle: String? = null
    private var mColor: Int? = null
    private var mIcon: Int? = null

    init {
        attrs?.let { initCustomAttrs(it) }
        initView(context)
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_button, this, true)
        tv_title.text = mTitle
        mIcon?.let { iv_icon.setImageResource(it) }
        mColor?.let { iv_action.setImageResource(it) }
    }

    private fun initCustomAttrs(attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.HomeBtnView)
        mTitle = array.getString(R.styleable.HomeBtnView_title)
        mColor = array.getResourceId(R.styleable.HomeBtnView_bg, R.color.black)
        mIcon = array.getResourceId(R.styleable.HomeBtnView_icon, R.drawable.ic_main_filter)
        array.recycle()
    }

}