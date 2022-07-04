package sensetime.senseme.com.effects.view.widget

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/25/21 9:10 PM
 */
@SuppressLint("AppCompatCustomView")
class TextThumbSeekBar(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    SeekBar(context, attrs, defStyleAttr) {
    companion object {
        //private const val TAG = "TextThumbSeekBar"
    }

    private lateinit var mPaint: Paint

    private val mProgressTextRect = Rect()

    private val mThumbWidth = dp2px(50f)
    private var mSeekBarMin = 0

    init {
        initData()
        setListener()
    }

    private fun setListener() {
        setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val result: Float = when (mType) {
                    Type.START_LEFT -> {
                        progress / 100f
                    }
                    Type.START_CENTER -> {
                        convertToDisplayValue(progress) / 100f
                    }
                }
                mListener?.onProgressChanged(seekBar, result, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mListener?.onStopTrackingTouch(seekBar)
            }

        })
    }

    enum class Type {
        START_LEFT,
        START_CENTER
    }

    private var mType: Type = Type.START_LEFT

    fun setShowType(type: Type) {
        mType = type
        max = when (mType) {
            Type.START_LEFT -> {
                100
            }
            Type.START_CENTER -> {
                200
            }
        }
    }

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null) : this(
        context,
        attrs,
        R.attr.seekBarStyle
    ) {
        initData()
    }

    private fun initData() {
        mPaint = TextPaint()
        mPaint.isAntiAlias = true
        mPaint.color = ContextCompat.getColor(context, R.color.white)
        mPaint.textSize = sp2px(16f).toFloat()
        setPadding(mThumbWidth / 2, 0, mThumbWidth / 2, 0)
    }

    private fun convertToDisplayValue(progressValue: Int): Int {
        return if (progressValue < 0 || progressValue > 200) {
            0
        } else progressValue - 100
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        var progressText = ""
        if (mType == Type.START_CENTER) {
            progressText = convertToDisplayValue(progress).toString()
        } else {
            progressText = "" + progress
        }
        mPaint.getTextBounds(progressText, 0, progressText.length, mProgressTextRect)

        val progressRatio = progress.toFloat() / max
        val thumbOffset =
            (mThumbWidth - mProgressTextRect.width()) / 2 - mThumbWidth * progressRatio
        val thumbX = width * progressRatio + thumbOffset
        val thumbY = height / 2f - mProgressTextRect.height() / 2f - dp2px(8f)
        canvas.drawText(progressText, thumbX, thumbY, mPaint)
    }

    private var mListener: Listener? = null

    interface Listener {
        // [0, 1.0]取值 或者[-1, 1]取值
        fun onProgressChanged(seekBar: SeekBar?, progress: Float, fromUser: Boolean)

        fun onStopTrackingTouch(seekBar: SeekBar?)
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setValue(param: Float) {
        progress = when (mType) {
            Type.START_LEFT -> {
                (param * 100).roundToInt()
            }
            Type.START_CENTER -> {
                convertToProgressValue((param * 100).roundToInt())
            }
        }
    }

    private fun convertToProgressValue(current: Int): Int {
        return if (current < -200 || current > 200) {
            0
        } else (current + 100)
    }

    private fun dp2px(dp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            resources.displayMetrics
        ).toInt()
    }

    private fun sp2px(sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, sp,
            resources.displayMetrics
        ).toInt()
    }
}