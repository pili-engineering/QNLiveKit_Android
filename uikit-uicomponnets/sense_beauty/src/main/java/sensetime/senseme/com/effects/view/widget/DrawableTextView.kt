package sensetime.senseme.com.effects.view.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import sensetime.senseme.com.effects.R

class DrawableTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var drawableLeft: Drawable? = null
    private var drawableTop: Drawable? = null
    private var drawableRight: Drawable? = null
    private var drawableBottom: Drawable? = null

    private var drawableWidth: Int = 0
    private var drawableHeight: Int = 0

    init {
        attrs?.let { initCustomAttrs(it) }
    }

    private fun initCustomAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView)
        val count = typedArray.indexCount
        for (i in 0 until count) {
            when (val attr = typedArray.getIndex(i)) {
                R.styleable.DrawableTextView_drawableRight -> drawableRight =
                    typedArray.getDrawable(attr)
                R.styleable.DrawableTextView_drawableLeft -> drawableLeft =
                    typedArray.getDrawable(attr)
                R.styleable.DrawableTextView_drawableTop -> drawableTop =
                    typedArray.getDrawable(attr)
                R.styleable.DrawableTextView_drawableBottom -> drawableBottom =
                    typedArray.getDrawable(attr)
                R.styleable.DrawableTextView_drawableWidth -> drawableWidth =
                    typedArray.getDimensionPixelSize(attr, 0)
                R.styleable.DrawableTextView_drawableHeight -> drawableHeight =
                    typedArray.getDimensionPixelSize(attr, 0)
            }
        }
        if (drawableWidth != 0 && drawableHeight != 0) {
            if (null != drawableLeft) {
                drawableLeft?.setBounds(0, 0, drawableWidth, drawableHeight)
            }
            if (null != drawableRight) {
                drawableRight?.setBounds(0, 0, drawableWidth, drawableHeight)
            }
            if (null != drawableTop) {
                drawableTop?.setBounds(0, 0, drawableWidth, drawableHeight)
            }
            if (null != drawableBottom) {
                drawableBottom?.setBounds(0, 0, drawableWidth, drawableHeight)
            }
        }

        setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom)
        typedArray.recycle()
    }

    fun setDrawableTop(resId: Int) {
        val drawableTop = ContextCompat.getDrawable(context, resId)
        drawableTop?.setBounds(0, 0, drawableWidth, drawableHeight)
        setCompoundDrawables(null, drawableTop, null, null)
    }

}