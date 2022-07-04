package com.qlive.uiwidghtbeauty

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.qlive.uiwidghtbeauty.BeautyDialogFragment
import sensetime.senseme.com.effects.R
import sensetime.senseme.com.effects.view.BeautyItem
import sensetime.senseme.com.effects.view.BeautyOptionsItem
import sensetime.senseme.com.effects.view.widget.BasicEffectView

class EffectBeautyDialogFragment : BeautyDialogFragment() {
    private var mSenseBeautyView: BasicEffectView? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mSenseBeautyView == null) {
            mSenseBeautyView =
                BasicEffectView(requireContext())
            mSenseBeautyView?.mIbShowOriginal?.setOnTouchListener { view, motionEvent ->

                true
            }
            mSenseBeautyView?.setListener(object :BasicEffectView.Listener{
                override fun onProgressChangedBasicEffect(
                    contentEntity: BeautyItem,
                    progress: Float,
                    fromUser: Boolean
                ) {

                }

                override fun onClickResetBasicEffect(currentTitleData: BeautyOptionsItem) {

                }
            })
        }


        if (mSenseBeautyView?.parent != null) {
            (mSenseBeautyView?.parent as ViewGroup).removeView(mSenseBeautyView)
        }
        val view = inflater.inflate(R.layout.kit_beauty_effect_dialog, container, false)
        view.findViewById<FrameLayout>(R.id.flcontainer).addView(mSenseBeautyView)
        return view
    }
}