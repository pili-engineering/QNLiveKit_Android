package sensetime.senseme.com.effects.utils

import android.content.Context
import android.content.res.TypedArray
import android.view.View
import sensetime.senseme.com.effects.R

/**
 * @Description 设置涟漪效果
 * @Author Lu Guoqiang
 * @Time 2020/11/9 8:24 PM
 */
class RippleUtils {

    companion object {
        fun setForeground(context: Context, vararg views: View) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                val attrs = intArrayOf(R.attr.selectableItemBackground)
                val typedArray: TypedArray = context.obtainStyledAttributes(attrs)
                val backgroundResource: Int = typedArray.getResourceId(0, 0)
                for (view in views) view.foreground = context.getDrawable(backgroundResource)
                typedArray.recycle()
            }
        }

    } 
}