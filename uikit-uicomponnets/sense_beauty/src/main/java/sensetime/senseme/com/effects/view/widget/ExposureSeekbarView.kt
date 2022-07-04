package sensetime.senseme.com.effects.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.SeekBar
import sensetime.senseme.com.effects.MSG.MSG_HIDE_VERTICALSEEKBAR

@SuppressLint("AppCompatCustomView")
class ExposureSeekbarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SeekBar(context, attrs) {

    private var mHandler: Handler? = null

    init {
        initView(context)
        progress = 50
        initListener()
    }

    private fun initListener() {
        setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                mListener?.onProgressChanged(p0, p1, p2)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldw, oldh)
    }

    private fun initView(context: Context) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.rotate(-90f)
        canvas.translate(-height.toFloat(), 0f)
        super.onDraw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> mHandler?.removeMessages(MSG_HIDE_VERTICALSEEKBAR)
            MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                var i = 0
                //获取滑动的距离
                i = max - (max * event.y / height).toInt()
                //设置进度
                progress = i
                //每次拖动SeekBar都会调用
                onSizeChanged(width, height, 0, 0)
                if (event.action == MotionEvent.ACTION_UP) {
                    mHandler?.sendEmptyMessageDelayed(MSG_HIDE_VERTICALSEEKBAR, 2000)
                }
            }
            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }

    interface Listener {
        fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean)
    }

    private var mListener: Listener? = null

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun setHandler(mHandler: Handler) {
        this.mHandler = mHandler
    }
}