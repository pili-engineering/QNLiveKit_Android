package com.qlive.uikitpk

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qlive.avparam.*
import com.qlive.rtclive.QPushTextureView
import com.qlive.pkservice.QPKService
import com.qlive.pkservice.QPKMixStreamAdapter
import com.qlive.pkservice.QPKServiceListener
import com.qlive.core.been.QPKSession
import com.qlive.core.QClientType
import com.qlive.playerclient.QPlayerClient
import com.qlive.core.been.QExtension
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikitcore.LinkerUIHelper
import com.qlive.uikitcore.QBaseRoomFrameLayout
import kotlinx.android.synthetic.main.kit_anchor_pk_preview.view.*

/**
 * pk主播
 */
class PKPreView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getLayoutId(): Int {
        return -1
    }

    private var childView: QBaseRoomFrameLayout? = null
    override fun initView() {
        val view = if (client?.clientType == QClientType.PLAYER) {
            PKAudiencePreview(context)
        } else {
            PKAnchorPreview(context)
        }
        view.attachKitContext(kitContext!!)
        view.attachLiveClient(client!!)
        addView(view)
        childView = view
    }


    /**
     * 进入回调
     * @param user 进入房间的用户
     * @param liveId 房间ID
     */
    override fun onEntering(roomId: String, user: QLiveUser) {
        super.onEntering(roomId, user)
        childView?.onEntering(roomId, user)
    }

    /**
     * 加入回调
     * @param roomInfo 房间信息
     */
    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        childView?.onJoined(roomInfo)
    }

    /**
     * 用户离开回调
     */
    override fun onLeft() {
        super.onLeft()
        childView?.onLeft()
    }

    /**
     * 销毁
     */
    override fun onDestroyed() {
        super.onDestroyed()
        childView?.onDestroyed()
    }
}

//观众端pk预览
class PKAudiencePreview : QBaseRoomFrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var isPKingPreview = false
    private var isPKing = false
    private val mQPKServiceListener = object :
        QPKServiceListener {

        override fun onStart(pkSession: QPKSession) {
            isPKing = true
        }

        override fun onStop(pkSession: QPKSession, code: Int, msg: String) {
            isPKing = false
        }
        override fun onStartTimeOut(pkSession: QPKSession) {}
        override fun onPKExtensionUpdate(pkSession: QPKSession, extension: QExtension) {
        }
    }

    private var mQPlayerEventListener = object : QPlayerEventListener {

        override fun onVideoSizeChanged(width: Int, height: Int) {
            if (width < height && isPKingPreview) {
                removeView()
            } else if (isPKing && !isPKingPreview && width > height) {
                addView()
            }
        }
    }

    private var originParent: ViewGroup? = null
    private var originIndex = 0
    private fun addView() {
        isPKingPreview = true
        val player = kitContext?.getPlayerRenderViewCall?.invoke()?.getView() ?: return
        val parent = player.parent as ViewGroup
        originParent = parent
        originIndex = parent.indexOfChild(player)
        parent.removeView(player)
        llPKContainer.addView(
            player,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
        )
    }

    private fun removeView() {
        isPKingPreview = false
        val rendView = kitContext?.getPlayerRenderViewCall?.invoke()
        val player = rendView?.getView() ?: return
        llPKContainer.removeView(player)
        originParent?.addView(
            player,
            originIndex,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        player.requestLayout()
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_anchor_pk_preview
    }

    override fun initView() {
        client!!.getService(QPKService::class.java).addServiceListener(mQPKServiceListener)
        (client as QPlayerClient).addPlayerEventListener(mQPlayerEventListener)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)

    }
}


//主播端预览
class PKAnchorPreview : QBaseRoomFrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    //混流适配
    private val mQPKMixStreamAdapter = object :  QPKMixStreamAdapter {

        override fun onPKLinkerJoin(pkSession: QPKSession): MutableList<QMergeOption> {
            return LinkerUIHelper.getPKMixOp(pkSession, user!!)
        }

        override fun onPKMixStreamStart(pkSession: QPKSession): QMixStreamParams? {
            return QMixStreamParams().apply {
                mixStreamWidth = LinkerUIHelper.pkMixWidth
                mixStringHeight = LinkerUIHelper.pkMixHeight
                mixBitrate = 1500 * 1000
                FPS = 25
            }
        }

        override fun onPKLinkerLeft(): MutableList<QMergeOption> {
            //pk结束
            val ops = ArrayList<QMergeOption>()
            return ops
        }

        /**
         * 当pk结束后如果还有其他普通连麦者 如何混流
         * 如果pk结束后没有其他连麦者 则不会回调
         * @return
         */
        override fun onPKMixStreamStop(): QMixStreamParams? {
            return null
        }
    }

    private var localRenderView: View? = null

    //PK监听
    private val mQPKServiceListener = object :
        QPKServiceListener {

        override fun onStart(pkSession: QPKSession) {
            val peer = if (pkSession.initiator.userId == user?.userId) {
                pkSession.receiver
            } else {
                pkSession.initiator
            }
            changeMeRenderViewToPk()
            flPeerContainer.addView(
                QPushTextureView(context).apply {
                    client?.getService(QPKService::class.java)
                        ?.setPeerAnchorPreView(this)
                },
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }

        override fun onStop(pkSession: QPKSession, code: Int, msg: String) {
            changeMeRenderViewToStopPk()
            flPeerContainer.removeAllViews()
        }

        override fun onStartTimeOut(pkSession: QPKSession) {}
        override fun onPKExtensionUpdate(pkSession: QPKSession, extension: QExtension) {}
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_anchor_pk_preview
    }

    override fun initView() {
        client!!.getService(QPKService::class.java).addServiceListener(mQPKServiceListener)
        client!!.getService(QPKService::class.java).setPKMixStreamAdapter(mQPKMixStreamAdapter)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)

    }

    private var pkScaleX = 0f
    private var pkScaleY = 0f

    private var originPreViewParent: ViewGroup? = null
    private var originIndex = -1;
    private fun changeMeRenderViewToPk() {
        localRenderView = kitContext?.getPusherRenderViewCall?.invoke()?.getView() ?: return

        pkScaleX = (flMeContainer.width / localRenderView!!.width.toFloat())
        pkScaleY = flMeContainer.height / (localRenderView!!.height.toFloat())
        localRenderView!!.pivotX = 0f
        localRenderView!!.pivotY = localRenderView!!.height / 2f

        val scaleX = ObjectAnimator.ofFloat(
            localRenderView!!,
            "scaleX",
            1f, pkScaleX
        )
        scaleX.duration = 500
        scaleX.repeatCount = 0
        val scaleY = ObjectAnimator.ofFloat(
            localRenderView!!,
            "scaleY",
            1f,
            pkScaleY
        )
        scaleY.duration = 500
        scaleY.repeatCount = 0

        val translationY = ObjectAnimator.ofFloat(
            localRenderView!!,
            "translationY",
            0f,
            -(localRenderView!!.height / 2f - (flMeContainer.height / 2f + llPKContainer.y))
        )

        AnimatorSet().apply {
            play(scaleX).with(scaleY).with(translationY)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {}

                override fun onAnimationEnd(p0: Animator?) {
                    originPreViewParent = localRenderView!!.parent as ViewGroup
                    originIndex = originPreViewParent?.indexOfChild(localRenderView) ?: 0
                    originPreViewParent?.removeView(localRenderView)
                    flMeContainer.addView(localRenderView)
                    flPeerContainer.addView(
                        QPushTextureView(context).apply {
                            client?.getService(QPKService::class.java)
                                ?.setPeerAnchorPreView(this)
                        },
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )
                    localRenderView!!.scaleX = 1f
                    localRenderView!!.scaleY = 1f
                    localRenderView!!.translationY = 0f
                }

                override fun onAnimationCancel(p0: Animator?) {

                }

                override fun onAnimationRepeat(p0: Animator?) {}
            })
        }.start()
    }

    private fun changeMeRenderViewToStopPk() {
        localRenderView = kitContext?.getPusherRenderViewCall?.invoke()?.getView() ?: return
        flMeContainer.removeView(localRenderView)
        originPreViewParent?.addView(
            localRenderView, originIndex, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        flPeerContainer.removeAllViews()
    }

}
