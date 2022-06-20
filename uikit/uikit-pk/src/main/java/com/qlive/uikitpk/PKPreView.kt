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
import com.qlive.rtclive.QPushTextureView
import com.qlive.pkservice.QPKService
import com.qlive.pkservice.QPKMixStreamAdapter
import com.qlive.pkservice.QPKServiceListener
import com.qlive.core.been.QPKSession
import com.qlive.avparam.CameraMergeOption
import com.qlive.avparam.MicrophoneMergeOption
import com.qlive.avparam.QMergeOption
import com.qlive.avparam.QMixStreamParams
import com.qlive.linkmicservice.QLinkMicService
import com.qlive.core.QClientType
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

    private var childView: QBaseRoomFrameLayout?=null
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
    override fun onEntering(roomId: String, user: QLiveUser){
        super.onEntering(roomId, user)
        childView?.onEntering(roomId,user)
    }

    /**
     * 加入回调
     * @param roomInfo 房间信息
     */
    override fun onJoined(roomInfo: QLiveRoomInfo){
        super.onJoined(roomInfo)
        childView?.onJoined(roomInfo)
    }

    /**
     * 用户离开回调
     */
    override fun onLeft(){
        super.onLeft()
        childView?.onLeft()
    }

    /**
     * 销毁
     */
    override fun onDestroyed(){
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

    private val mQPKServiceListener = object :
        QPKServiceListener {

        override fun onStart(pkSession: QPKSession) {
            addView()
        }

        override fun onStop(pkSession: QPKSession, code: Int, msg: String) {
            removeView()
        }

        override fun onStartTimeOut(pkSession: QPKSession) {}


        override fun onPKExtensionUpdate(pkSession: QPKSession, extension: QExtension) {
        }
    }

    private var originParent: ViewGroup? = null
    private var originIndex = 0
    private fun addView() {
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
        player.requestLayout()
    }

    private fun removeView() {
        val player = kitContext?.getPlayerRenderViewCall?.invoke()?.getView() ?: return
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
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
        if (event == Lifecycle.Event.ON_DESTROY) {
            client?.getService(QPKService::class.java)?.removeServiceListener(mQPKServiceListener)
        }
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
    private val mQPKMixStreamAdapter = object :
        QPKMixStreamAdapter {

        override fun onPKLinkerJoin(pkSession: QPKSession): MutableList<QMergeOption> {
            val ops = ArrayList<QMergeOption>()
            val peer = if (pkSession.initiator.userId == user?.userId) {
                pkSession.receiver
            } else {
                pkSession.initiator
            }
            ops.add(QMergeOption().apply {
                uid = user!!.userId
                cameraMergeOption = CameraMergeOption().apply {
                    isNeed = true
                    x = 0
                    y = 0
                    z = 0
                    width = PKUIHelper.mixWidth / 2
                    height = PKUIHelper.mixHeight
                    // mStretchMode=QNRenderMode.
                }
                microphoneMergeOption = MicrophoneMergeOption().apply {
                    isNeed = true
                }
            })
            ops.add(QMergeOption().apply {
                uid = peer.userId
                cameraMergeOption = CameraMergeOption().apply {
                    isNeed = true
                    x = PKUIHelper.mixWidth / 2
                    y = 0
                    z = 0
                    width = PKUIHelper.mixWidth / 2
                    height = PKUIHelper.mixHeight
                    // mStretchMode=QNRenderMode.
                }
                microphoneMergeOption = MicrophoneMergeOption().apply {
                    isNeed = true
                }
            })
            return ops
        }

        override fun onPKMixStreamStart(pkSession: QPKSession): QMixStreamParams {
            return QMixStreamParams().apply {
                mixStreamWidth = PKUIHelper.mixWidth
                mixStringHeight = PKUIHelper.mixHeight
                mixBitrate = 1500 * 1000
                FPS = 25
            }
        }

        override fun onPKLinkerLeft(): MutableList<QMergeOption> {
            val ops = ArrayList<QMergeOption>()
            client?.getService(QLinkMicService::class.java)
                ?.allLinker?.let {
                    ops.addAll(LinkerUIHelper.getLinkers(it.map { it.user }, roomInfo!!))
                }
            return ops
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
        if (event == Lifecycle.Event.ON_DESTROY) {
            client?.getService(QPKService::class.java)?.removeServiceListener(mQPKServiceListener)
            client?.getService(QPKService::class.java)?.setPKMixStreamAdapter(null)
        }
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
        localRenderView!!.pivotY = localRenderView!!.height/2f

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
           - (localRenderView!!.height / 2f - (flMeContainer.height / 2f + llPKContainer.y))
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
