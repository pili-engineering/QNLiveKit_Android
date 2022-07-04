package sensetime.senseme.com.effects.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import sensetime.senseme.com.effects.utils.LogUtils
import kotlinx.android.synthetic.main.view_gradient_seekbar.view.*
import sensetime.senseme.com.effects.R
import kotlin.math.roundToInt

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 2021/8/19 3:20 下午
 */
private const val TAG = "GradientSeekbar"

class GradientSeekbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        initView(context)
        initListener()
    }

    fun setValue(float: Float) {
        LogUtils.iTag(TAG, "setValue() called with: float = $float")
        val sb = seekbar as TextThumbSeekBar
        sb.setValue(float)
    }

    @Suppress("SYNTHETIC_UNRESOLVED_WIDGET_TYPE")
    fun getValue(): Float {
        val sb = seekbar as TextThumbSeekBar
        return sb.progress / 100f
    }

    @Suppress("SYNTHETIC_UNRESOLVED_WIDGET_TYPE")
    private fun initListener() {
        val sb = seekbar as TextThumbSeekBar
        sb.setListener(object : Listener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean) {
                mListener?.onProgressChanged(seekBar, progress, fromUser)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mListener?.onStopTrackingTouch(seekBar)
            }

        })
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_gradient_seekbar, this, true)
    }

    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    @Suppress("unused")
    fun setColor(startColor: String, endColor: String) {
        v_bg.background = getGradientDrawable(startColor, endColor)
    }

    fun setColor(startColor: Int, endColor: Int) {
        v_bg.background = getGradientDrawable(startColor, endColor)
    }

    @Suppress("SYNTHETIC_UNRESOLVED_WIDGET_TYPE")
    fun setProgress(float: Float) {
        val s = seekbar as TextThumbSeekBar
        s.setValue(float)
    }

    /**
     * #FF000000
     */
    private fun getGradientDrawable(startColor: String, endColor: String): GradientDrawable {
        return GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(Color.parseColor(startColor), Color.parseColor(endColor))
        )
    }

    private fun getGradientDrawable(startColor: Int, endColor: Int): GradientDrawable {
        return GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf(startColor, endColor)
        )
    }

    interface Listener {
        // [0, 1.0]取值 或者[-1, 1]取值
        fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean)

        fun onStopTrackingTouch(seekBar: SeekBar?)
    }

    @SuppressLint("AppCompatCustomView")
    class TextThumbSeekBar(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
        SeekBar(context, attrs, defStyleAttr) {

        // 画笔
        private lateinit var mPaint: Paint

        // 进度文字位置信息
        private val mProgressTextRect = Rect()

        // 滑块按钮宽度
        private val mThumbWidth = dp2px(13f)
        private var mSeekBarMin = 0

        init {
            initData()
            setListener()
        }

        private fun setListener() {
            setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val result = progress / 100f
                    mListener?.onProgressChanged(seekBar, result, fromUser)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }

            })
        }

        @JvmOverloads
        constructor(context: Context?, attrs: AttributeSet? = null) : this(
            context,
            attrs,
            android.R.attr.seekBarStyle
        ) {
            initData()
        }

        private fun initData() {
            mPaint = TextPaint()
            mPaint.isAntiAlias = true
            mPaint.color = ContextCompat.getColor(context, R.color.black)
            mPaint.textSize = sp2px(11f).toFloat()
            setPadding(dp2px(0f), 0, dp2px(5f), 0)
        }

        @Synchronized
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            val progressText = "" + progress / 100f
            mPaint.getTextBounds(progressText, 0, progressText.length, mProgressTextRect)
            val progressRatio = progress.toFloat() / max
            val thumbOffset =
                (mThumbWidth - mProgressTextRect.width()) / 2 - mThumbWidth * progressRatio
            val thumbX = width * progressRatio + thumbOffset
            val thumbY = height / 2f - mProgressTextRect.height() / 2f - dp2px(8f)
            canvas.drawText(progressText, thumbX, thumbY, mPaint)
        }

        fun setMix(min: Int) {
            mSeekBarMin = min
        }

        private var mListener: Listener? = null

        fun setListener(listener: Listener) {
            mListener = listener
        }

        fun setValue(param: Float) {
            LogUtils.iTag(TAG, "setValue() called with: param = $param")
            progress = (param * 100).roundToInt()
            LogUtils.iTag(TAG, "setValue: progress:${progress}")
        }

        private fun convertToProgressValue(displayValue: Int): Int {
            LogUtils.iTag(TAG, "显示值: $displayValue")
            return if (displayValue < -200 || displayValue > 200) {
                0
            } else (displayValue + 100)
        }

        private fun convertToDisplayValue(progressValue: Int): Int {
            LogUtils.iTag(TAG, "进度值转换显示值 $")
            return if (progressValue < 0 || progressValue > 200) {
                0
            } else progressValue - 100
        }

        private fun dp2px(dp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,
                resources.displayMetrics
            ).toInt()
        }

        /**
         * sp转px
         *
         * @param sp sp值
         * @return px值
         */
        @Suppress("SameParameterValue")
        private fun sp2px(sp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, sp,
                resources.displayMetrics
            ).toInt()
        }
    }
}


