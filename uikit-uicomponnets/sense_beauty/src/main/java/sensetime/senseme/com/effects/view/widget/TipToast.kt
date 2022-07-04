package sensetime.senseme.com.effects.view.widget

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import sensetime.senseme.com.effects.R

/**
 * @Description 自定义显示的View
 * @Author Lu Guoqiang
 * @Time 2021/6/30 13:25
 */
class TipToast private constructor(context: Context, text: CharSequence, duration: Int) {
    private val mToast: Toast?
    fun show() {
        mToast?.show()
    }

    private fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        mToast?.setGravity(gravity, xOffset, yOffset)
    }

    companion object {
        fun makeText(context: Context, text: CharSequence, duration: Int): TipToast {
            return TipToast(context, text, duration)
        }

        fun makeText(context: Context, duration: Int): TipToast {
            return TipToast(context, "", duration)
        }
    }

    init {
        val v = LayoutInflater.from(context).inflate(R.layout.toast_tip, null)
        val tvTip = v.findViewById<TextView>(R.id.tv_tip)
        if (!TextUtils.isEmpty(text)) {
            tvTip.text = text
        }
        mToast = Toast(context)
        setGravity(Gravity.CENTER, 0, 0)
        mToast.duration = duration
        mToast.view = v
        val dt = mToast.duration
        println(dt)
    }
}