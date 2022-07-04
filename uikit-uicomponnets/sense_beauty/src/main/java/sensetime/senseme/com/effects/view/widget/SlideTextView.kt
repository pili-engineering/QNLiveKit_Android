package sensetime.senseme.com.effects.view.widget

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_slide_text.view.*
import sensetime.senseme.com.effects.R

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 6/11/21 12:43 PM
 */
class SlideTextView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mDistance = 0f
    private var mLastViewId = 0
    private val duration: Long = 200
    private var mListener: Listener? = null

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_slide_text, this, true)
        setOpenVar()
        mLastViewId = tv_video.id

        tv_take.post {
            onClickTake(tv_take)
        }

        tv_video.setOnClickListener {
            onClickVideo(it as TextView)
        }

        tv_take.setOnClickListener {
            onClickTake(it as TextView)
        }

        tv_style.setOnClickListener {
            if (mLastViewId == it.id) return@setOnClickListener

            val layoutParams: ConstraintLayout.LayoutParams =
                    tv_take.layoutParams as ConstraintLayout.LayoutParams
            mDistance = (layoutParams.leftMargin + it.width).toFloat()

            if (mLastViewId == tv_video.id) mDistance += (layoutParams.leftMargin + it.width).toFloat()

            startSwitchModeAnimation(false)
            mLastViewId = it.id
            setNormal(tv_style)
            mListener?.onClickMenuListener(2)
        }
    }

    lateinit var mTvStyle: TextView
    lateinit var mTvTake: TextView
    private fun setOpenVar() {
        mTvStyle = tv_style
        mTvTake = tv_take
    }

    fun performClickStyle() {
        tv_style.post {
            postDelayed({
                tv_style.performClick()
            }, duration + 1)
        }
    }

    private fun onClickTake(it: TextView) {
        if (mLastViewId == it.id) return

        val layoutParams: ConstraintLayout.LayoutParams =
                tv_take.layoutParams as ConstraintLayout.LayoutParams
        mDistance = (layoutParams.leftMargin + it.width).toFloat()

        if (mLastViewId == tv_style.id)
            startSwitchModeAnimation(true)
        else
            startSwitchModeAnimation(false)
        mLastViewId = it.id
        setNormal(it)
        mListener?.onClickMenuListener(1)
    }

    private fun onClickVideo(it: TextView) {
        if (mLastViewId == it.id) return

        val layoutParams: ConstraintLayout.LayoutParams =
                tv_take.layoutParams as ConstraintLayout.LayoutParams
        mDistance = (layoutParams.leftMargin + it.width).toFloat()
        if (mLastViewId == tv_style.id) mDistance += (layoutParams.leftMargin + it.width).toFloat()

        startSwitchModeAnimation(true)
        mLastViewId = it.id
        setNormal(it)
        mListener?.onClickMenuListener(0)
    }

    private fun setNormal(view: TextView) {
        tv_take.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        tv_video.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
        tv_style.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)

        view.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
    }

    private fun startSwitchModeAnimation(isRight: Boolean) {
        if (isRight) {
            tv_take.animate().translationXBy(mDistance).setDuration(duration).start()
            tv_video.animate().translationXBy(mDistance).setDuration(duration).start()
            tv_style.animate().translationXBy(mDistance).setDuration(duration).start()
        } else {
            tv_take.animate().translationXBy(-mDistance).setDuration(duration).start()
            tv_video.animate().translationXBy(-mDistance).setDuration(duration).start()
            tv_style.animate().translationXBy(-mDistance).setDuration(duration).start()
        }
    }

    interface Listener {
        open fun onClickMenuListener(position: Int)
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun showText() {
        gp_text.visibility = View.VISIBLE
    }

    fun hideText() {
        gp_text.visibility = View.INVISIBLE
    }
}