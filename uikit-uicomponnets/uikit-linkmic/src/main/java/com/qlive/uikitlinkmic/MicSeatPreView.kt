package com.qlive.uikitlinkmic

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.qlive.core.been.QExtension
import com.qlive.linkmicservice.QLinkMicService
import com.qlive.core.been.QMicLinker
import com.qlive.linkmicservice.QLinkMicServiceListener
import com.qlive.uikitcore.LinkerUIHelper
import com.qlive.uikitcore.ext.ViewUtil
import kotlinx.android.synthetic.main.kit_item_linker.view.*
import kotlinx.android.synthetic.main.kit_item_linker_surface.view.*
import kotlinx.android.synthetic.main.kit_item_linker_surface.view.flSurfaceContainer
import kotlinx.android.synthetic.main.kit_item_linker_surface.view.ivMicStatus
import kotlinx.android.synthetic.main.kit_view_linkers.view.*

//麦位预览
class MicSeatPreView : LinearLayout, QLinkMicServiceListener {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var isLinker = false
    var linkService: QLinkMicService? = null

    fun setRole(isLinker: Boolean) {
        this.isLinker = isLinker
        for (i in 0 until childCount) {
            (getChildAt(i) as MicItemPreView).setRole(isLinker)
        }
    }

    fun clear(){
        removeAllViews()
    }

    override fun onLinkerJoin(micLinker: QMicLinker) {
        val itemView = MicItemPreView(context)
        itemView.linkService = linkService
        itemView.onLinkerJoin(micLinker)
        itemView.setRole(isLinker)
        addView(itemView)
    }


    override fun onLinkerLeft(micLinker: QMicLinker) {
        var target: MicItemPreView? = null
        for (i in 0 until childCount) {
            val item = getChildAt(i) as MicItemPreView
            if (item.mMicLinker?.user?.userId == micLinker.user.userId) {
                target = item
            }
        }
        target?.let {
            it.onLinkerLeft(micLinker)
            this@MicSeatPreView.removeView(it)
        }
    }

    override fun onLinkerMicrophoneStatusChange(micLinker: QMicLinker) {
        var target: MicItemPreView? = null
        for (i in 0 until childCount) {
            val item = getChildAt(i) as MicItemPreView
            if (item.mMicLinker?.user?.userId == micLinker.user.userId) {
                target = item
            }
        }
        target?.onLinkerMicrophoneStatusChange(micLinker)
    }

    override fun onLinkerCameraStatusChange(micLinker: QMicLinker) {
        var target: MicItemPreView? = null
        for (i in 0 until childCount) {
            val item = getChildAt(i) as MicItemPreView
            if (item.mMicLinker?.user?.userId == micLinker.user.userId) {
                target = item
            }
        }
        target?.onLinkerCameraStatusChange(micLinker)
    }

    override fun onLinkerKicked(micLinker: QMicLinker, msg: String?) {
    }

    override fun onLinkerExtensionUpdate(micLinker: QMicLinker, QExtension: QExtension?) {
    }
}

class MicItemPreView : FrameLayout, QLinkMicServiceListener {

    var mMicLinker: QMicLinker? = null
    private var isLinker = false
    var linkService: QLinkMicService? = null
    fun setRole(isLinker: Boolean) {
        this.isLinker = isLinker
        if (isLinker) {
            addSurface()
        } else {
            removeSurface()
        }
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val itemView = LayoutInflater.from(context).inflate(R.layout.kit_item_linker, this, false)
        val flVideoMicLp = itemView.flVideoMic.layoutParams
        flVideoMicLp.width = LinkerUIHelper.uiMicWidth
        flVideoMicLp.height = LinkerUIHelper.uiMicHeight + LinkerUIHelper.micBottomUIMargin
        itemView.flVideoMic.layoutParams = flVideoMicLp

        val tempMixLp = itemView.tempMix.layoutParams as FrameLayout.LayoutParams
        tempMixLp.width = LinkerUIHelper.uiMicWidth
        tempMixLp.height = LinkerUIHelper.uiMicHeight + LinkerUIHelper.micBottomUIMargin
        tempMixLp.marginEnd = LinkerUIHelper.micRightUIMargin
        itemView.tempMix.layoutParams = tempMixLp

        addView(itemView)
    }


    override fun onLinkerJoin(micLinker: QMicLinker) {
        mMicLinker = micLinker
        if (micLinker.isOpenCamera) {
            flVideoMic.visibility = VISIBLE
            flAudioMic.visibility = View.GONE
        } else {
            flVideoMic.visibility = GONE
            flAudioMic.visibility = View.VISIBLE
        }
        Glide.with(context).load(micLinker.user.avatar)
            .into(ivAvatarInner)
        ivMicStatusInner.isSelected = micLinker.isOpenMicrophone
        ivOutMicStatus.isSelected = micLinker.isOpenMicrophone
    }

    override fun onLinkerLeft(micLinker: QMicLinker) {
        mMicLinker = null
    }

    override fun onLinkerMicrophoneStatusChange(micLinker: QMicLinker) {
        ivMicStatusInner.isSelected = micLinker.isOpenMicrophone
        ivOutMicStatus.isSelected = micLinker.isOpenMicrophone
    }

    override fun onLinkerCameraStatusChange(micLinker: QMicLinker) {
        if (micLinker.isOpenCamera) {
            flVideoMic.visibility = VISIBLE
            flAudioMic.visibility = View.GONE
        } else {
            flVideoMic.visibility = GONE
            flAudioMic.visibility = View.VISIBLE
        }
    }

    override fun onLinkerKicked(micLinker: QMicLinker, msg: String?) {
    }

    override fun onLinkerExtensionUpdate(micLinker: QMicLinker, QExtension: QExtension?) {
    }

    private fun addSurface() {
        val container = flSurfaceContainer as FrameLayout
        val size = ViewUtil.dip2px(96f)
        container.addView(
            RoundTextureView(context).apply {
                linkService?.setUserPreview(mMicLinker?.user?.userId ?: "", this)
                setRadius((size / 2).toFloat())
            },
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
            )
        )
    }

    private fun removeSurface() {
        val container = flSurfaceContainer as FrameLayout
        container.removeAllViews()
    }
}