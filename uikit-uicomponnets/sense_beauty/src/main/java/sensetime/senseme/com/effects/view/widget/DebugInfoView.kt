package sensetime.senseme.com.effects.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.sensetime.stmobile.STMobileHumanActionNative
import kotlinx.android.synthetic.main.view_debug.view.*
import sensetime.senseme.com.effects.R

/**
 * @Description
 * @Author Lu Guoqiang
 * @Time 2021/7/15 8:59 下午
 */
class DebugInfoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.view_debug, this, true)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun resetFaceExpression() {
        iv_face_expression_head_normal.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_head_normal)
        )
        iv_face_expression_side_face_left.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_side_face_left)
        )
        iv_face_expression_side_face_right.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_side_face_right)
        )
        iv_face_expression_tilted_face_left.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_tilted_face_left)
        )
        iv_face_expression_tilted_face_right.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_tilted_face_right)
        )
        iv_face_expression_head_rise.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_head_rise)
        )
        iv_face_expression_head_lower.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_head_lower)
        )
        iv_face_expression_two_eye_open.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_two_eye_open)
        )
        iv_face_expression_two_eye_close.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_two_eye_close)
        )
        iv_face_expression_lefteye_close_righteye_open.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_lefteye_close_righteye_open)
        )
        iv_face_expression_lefteye_open_righteye_close.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_lefteye_open_righteye_close)
        )
        iv_face_expression_mouth_open.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_mouth_open)
        )
        iv_face_expression_mouth_close.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_mouth_close)
        )
        iv_face_expression_face_lips_pouted.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_face_lips_pouted)
        )
        iv_face_expression_face_lips_upward.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_face_lips_upward)
        )
        iv_face_expression_lips_curl_left.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_lips_curl_left)
        )
        iv_face_expression_lips_curl_right.setImageDrawable(
            resources.getDrawable(R.drawable.face_expression_lips_curl_right)
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showFaceExpressionInfo(faceExpressionInfo: BooleanArray?) {
        resetFaceExpression()
        if (faceExpressionInfo != null) {
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_HEAD_NORMAL.expressionCode]) {
                iv_face_expression_head_normal.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_head_normal_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_SIDE_FACE_LEFT.expressionCode]) {
                iv_face_expression_side_face_left.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_side_face_left_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_SIDE_FACE_RIGHT.expressionCode]) {
                iv_face_expression_side_face_right.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_side_face_right_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_TILTED_FACE_LEFT.expressionCode]) {
                iv_face_expression_tilted_face_left.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_tilted_face_left_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_TILTED_FACE_RIGHT.expressionCode]) {
                iv_face_expression_tilted_face_right.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_tilted_face_right_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_HEAD_RISE.expressionCode]) {
                iv_face_expression_head_rise.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_head_rise_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_HEAD_LOWER.expressionCode]) {
                iv_face_expression_head_lower.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_head_lower_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_TWO_EYE_OPEN.expressionCode]) {
                iv_face_expression_two_eye_open.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_two_eye_open_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_TWO_EYE_CLOSE.expressionCode]) {
                iv_face_expression_two_eye_close.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_two_eye_close_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_LEFTEYE_CLOSE_RIGHTEYE_OPEN.expressionCode]) {
                iv_face_expression_lefteye_close_righteye_open.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_lefteye_close_righteye_open_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_LEFTEYE_OPEN_RIGHTEYE_CLOSE.expressionCode]) {
                iv_face_expression_lefteye_open_righteye_close.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_lefteye_open_righteye_close_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_MOUTH_OPEN.expressionCode]) {
                iv_face_expression_mouth_open.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_mouth_open_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_MOUTH_CLOSE.expressionCode]) {
                iv_face_expression_mouth_close.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_mouth_close_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_FACE_LIPS_POUTED.expressionCode]) {
                iv_face_expression_face_lips_pouted.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_face_lips_pouted_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_FACE_LIPS_UPWARD.expressionCode]) {
                iv_face_expression_face_lips_upward.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_face_lips_upward_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_FACE_LIPS_CURL_LEFT.expressionCode]) {
                iv_face_expression_lips_curl_left.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_lips_curl_left_selected)
                )
            }
            if (faceExpressionInfo[STMobileHumanActionNative.STMobileExpression.ST_MOBILE_EXPRESSION_FACE_LIPS_CURL_RIGHT.expressionCode]) {
                iv_face_expression_lips_curl_right.setImageDrawable(
                    resources.getDrawable(R.drawable.face_expression_lips_curl_right_selected)
                )
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun resetHandActionInfo() {
        iv_palm.setBackgroundColor(Color.parseColor("#00000000"))
        iv_palm.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_palm)
        )
        iv_thumb.setBackgroundColor(Color.parseColor("#00000000"))
        iv_thumb.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_thumb)
        )
        iv_ok.setBackgroundColor(Color.parseColor("#00000000"))
        iv_ok.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_ok)
        )
        iv_pistol.setBackgroundColor(Color.parseColor("#00000000"))
        iv_pistol.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_pistol)
        )
        iv_one_finger.setBackgroundColor(Color.parseColor("#00000000"))
        iv_one_finger.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_one_finger)
        )
        iv_finger_heart.setBackgroundColor(Color.parseColor("#00000000"))
        iv_finger_heart.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_finger_heart)
        )
        iv_heart_hand.setBackgroundColor(Color.parseColor("#00000000"))
        iv_heart_hand.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_heart_hand)
        )
        iv_scissor.setBackgroundColor(Color.parseColor("#00000000"))
        iv_scissor.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_scissor)
        )
        iv_congratulate.setBackgroundColor(Color.parseColor("#00000000"))
        iv_congratulate.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_congratulate)
        )
        iv_palm_up.setBackgroundColor(Color.parseColor("#00000000"))
        iv_palm_up.setImageDrawable(
            resources.getDrawable(R.drawable.ic_trigger_palm_up)
        )
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showHandActionInfo(action: Long) {
        val mColorBlue = Color.parseColor("#0a8dff")
        resetHandActionInfo()
        if (action and STMobileHumanActionNative.ST_MOBILE_HAND_PALM > 0) {
            findViewById<View>(R.id.iv_palm).setBackgroundColor(mColorBlue)
            (findViewById<View>(R.id.iv_palm) as ImageView).setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_palm_selected)
            )
        } else if (action and STMobileHumanActionNative.ST_MOBILE_HAND_GOOD > 0) {
            findViewById<View>(R.id.iv_thumb).setBackgroundColor(mColorBlue)
            (findViewById<View>(R.id.iv_thumb) as ImageView).setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_thumb_selected)
            )
        } else if (action and STMobileHumanActionNative.ST_MOBILE_HAND_OK > 0) {
            findViewById<View>(R.id.iv_ok).setBackgroundColor(mColorBlue)
            (findViewById<View>(R.id.iv_ok) as ImageView).setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_ok_selected)
            )
        } else if (action and STMobileHumanActionNative.ST_MOBILE_HAND_PISTOL > 0) {
            findViewById<View>(R.id.iv_pistol).setBackgroundColor(mColorBlue)
            (findViewById<View>(R.id.iv_pistol) as ImageView).setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_pistol_selected)
            )
        } else if (action and STMobileHumanActionNative.ST_MOBILE_HAND_FINGER_INDEX > 0) {
            findViewById<View>(R.id.iv_one_finger).setBackgroundColor(mColorBlue)
            (findViewById<View>(R.id.iv_one_finger) as ImageView).setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_one_finger_selected)
            )
        } else if (action and STMobileHumanActionNative.ST_MOBILE_HAND_FINGER_HEART > 0) {
            findViewById<View>(R.id.iv_finger_heart).setBackgroundColor(mColorBlue)
            (findViewById<View>(R.id.iv_finger_heart) as ImageView).setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_finger_heart_selected)
            )
        } else if (action and STMobileHumanActionNative.ST_MOBILE_HAND_LOVE > 0) {
            findViewById<View>(R.id.iv_heart_hand).setBackgroundColor(mColorBlue)
            (findViewById<View>(R.id.iv_heart_hand) as ImageView).setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_heart_hand_selected)
            )
        } else if (action and STMobileHumanActionNative.ST_MOBILE_HAND_SCISSOR > 0) {
            findViewById<View>(R.id.iv_scissor).setBackgroundColor(mColorBlue)
            (findViewById<View>(R.id.iv_scissor) as ImageView).setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_scissor_selected)
            )
        } else if (action and STMobileHumanActionNative.ST_MOBILE_HAND_CONGRATULATE > 0) {
            findViewById<View>(R.id.iv_congratulate).setBackgroundColor(mColorBlue)
            (findViewById<View>(R.id.iv_congratulate) as ImageView).setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_congratulate_selected)
            )
        } else if (action and STMobileHumanActionNative.ST_MOBILE_HAND_HOLDUP > 0) {
            findViewById<View>(R.id.iv_palm_up).setBackgroundColor(mColorBlue)
            (findViewById<View>(R.id.iv_palm_up) as ImageView).setImageDrawable(
                resources.getDrawable(R.drawable.ic_trigger_palm_up_selected)
            )
        }
    }

    fun showDebug(show: Boolean) {
        rl_test_layout.visibility = View.VISIBLE
        ll_face_expression.visibility = View.VISIBLE
        ll_hand_action_info.visibility = View.VISIBLE
        testSwitch0.visibility = VISIBLE
        testSwitch1.visibility = VISIBLE
        testSwitch2.visibility = VISIBLE
    }
}