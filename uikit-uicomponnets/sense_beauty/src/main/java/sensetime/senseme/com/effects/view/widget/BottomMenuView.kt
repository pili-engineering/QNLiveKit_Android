package sensetime.senseme.com.effects.view.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.IntDef
import kotlinx.android.synthetic.main.view_bottom_menu.view.*
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.utils.RippleUtils

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/18/21 11:08 AM
 */

class BottomMenuView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    lateinit var mIvStyle: ImageView

    companion object {
        const val BLACK_STYLE = 2
        const val WRITE_STYLE = 3

        @Retention(AnnotationRetention.SOURCE)
        @IntDef(BLACK_STYLE, WRITE_STYLE)
        annotation class Style
    }

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_bottom_menu, this, true)
        RippleUtils.setForeground(context, iv_style)
        setOpenVar()
        dt_sticker.setOnClickListener {
            visibility = View.INVISIBLE
            mListener?.onClickSticker()
        }

        dt_effect.setOnClickListener {
            mListener?.onClickBeauty()
        }

        dt_filter.setOnClickListener {
            mListener?.onClickFilter()
        }

        dt_makeup.setOnClickListener {
            mListener?.onClickMakeup()
        }
    }

    private fun setOpenVar() {
        mIvStyle = iv_style
    }

    var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setGrayColor() {
        dt_sticker?.apply {
            setDrawableTop(R.drawable.ic_camera_sticker_gray)
            setTextColor(Color.parseColor("#4E4E4E"))
        }

        dt_makeup?.apply {
            setDrawableTop(R.drawable.ic_camera_makeup_gray)
            setTextColor(Color.parseColor("#4E4E4E"))
        }

        dt_filter?.apply {
            setDrawableTop(R.drawable.ic_camera_filter_gray)
            setTextColor(Color.parseColor("#4E4E4E"))
        }

        dt_effect?.apply {
            setDrawableTop(R.drawable.ic_camera_effect_gray)
            setTextColor(Color.parseColor("#4E4E4E"))
        }
    }

    fun setWriteColor() {
        dt_sticker?.apply {
            setDrawableTop(R.drawable.ic_camera_sticker)
            setTextColor(Color.parseColor("#ffffff"))
        }

        dt_makeup?.apply {
            setDrawableTop(R.drawable.ic_camera_makeup)
            setTextColor(Color.parseColor("#ffffff"))
        }

        dt_filter?.apply {
            setDrawableTop(R.drawable.ic_camera_filter)
            setTextColor(Color.parseColor("#ffffff"))
        }

        dt_effect?.apply {
            setDrawableTop(R.drawable.ic_camera_effect)
            setTextColor(Color.parseColor("#ffffff"))
        }
    }

    fun setStyle(@Style style: Int) {
        when (style) {
            BLACK_STYLE -> {
                cl_root.setBackgroundColor(Color.parseColor("#ffffff"))
            }
            WRITE_STYLE -> {
                cl_root.setBackgroundColor(Color.parseColor("#00000000"))
            }
        }
    }

    interface Listener {
        fun onClickSticker()
        fun onClickMakeup()
        fun onClickFilter()
        fun onClickBeauty()
    }
}