package com.qlive.uiwidghtbeauty

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.qlive.uiwidghtbeauty.BeautyDialogFragment

class EffectBeautyDialogFragment : BeautyDialogFragment() {
    private var mSenseBeautyView: QSenseBeautyView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mSenseBeautyView == null) {
            mSenseBeautyView =
                QSenseBeautyView(requireContext())
        }
        if (mSenseBeautyView?.parent != null) {
            (mSenseBeautyView?.parent as ViewGroup).removeView(mSenseBeautyView)
        }
        val view = inflater.inflate(R.layout.kit_beauty_effect_dialog, container, false)
        view.findViewById<FrameLayout>(R.id.flcontainer).addView(mSenseBeautyView)
        return view
    }
}