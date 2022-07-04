package sensetime.senseme.com.effects.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebSettings
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_terms.view.*
import sensetime.senseme.com.effects.R

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/17/21 8:29 PM
 */
@Suppress("DEPRECATION")
class TermsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_terms, this, true)
        wv_docs.loadUrl("file:///android_asset/SenseME_Provisions_v1.0.html")
        wv_docs.settings.textSize = WebSettings.TextSize.SMALLER
        tv_back_btn.setOnClickListener {
            visibility = View.INVISIBLE
        }
    }

}