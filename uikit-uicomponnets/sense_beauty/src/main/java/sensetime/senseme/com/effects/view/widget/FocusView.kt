package sensetime.senseme.com.effects.view.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_focus.view.*
import sensetime.senseme.com.effects.R

class FocusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_focus, this, true)
        val layoutParams = FrameLayout.LayoutParams(80, 80)
        iv_fucos.layoutParams = layoutParams
    }

    fun perFormSetMeteringArea(touchX: Float, touchY: Float) {
        val params = iv_fucos.layoutParams as FrameLayout.LayoutParams
        params.setMargins(touchX.toInt() - 50, touchY.toInt() - 50, 0, 0)
        iv_fucos.layoutParams = params
        iv_fucos.visibility = View.VISIBLE
        startAnim()
    }

    private fun startAnim() {
        val animatorSet = AnimatorSet()
        val animX = ObjectAnimator.ofFloat(iv_fucos, "scaleX", 1.5f, 1.2f)
        val animY = ObjectAnimator.ofFloat(iv_fucos, "scaleY", 1.5f, 1.2f)
        animatorSet.duration = 500
        animatorSet.play(animX).with(animY)
        animatorSet.start()
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                iv_fucos.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }
}