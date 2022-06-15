package com.qncube.uikitpk

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.nucube.rtclive.QPushTextureView
import com.qbcube.pkservice.QPKService
import com.qbcube.pkservice.QPKMixStreamAdapter
import com.qbcube.pkservice.QPKServiceListener
import com.qbcube.pkservice.QPKSession
import com.qncube.lcommon.CameraMergeOption
import com.qncube.lcommon.MicrophoneMergeOption
import com.qncube.lcommon.QMergeOption
import com.qncube.lcommon.QMixStreamParams
import com.qncube.linkmicservice.QLinkMicService
import com.qncube.liveroomcore.QPlayerClient
import com.qncube.liveroomcore.QClientType
import com.qncube.liveroomcore.QPusherClient
import com.qncube.liveroomcore.been.QExtension
import com.qncube.uikitcore.LinkerUIHelper
import com.qncube.uikitcore.QBaseRoomFrameLayout
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

    override fun initView() {
        val view = if (client?.clientType == QClientType.PLAYER) {
            PKAudiencePreview(context)
        } else {
            PKAnchorPreview(context)
        }
        view.attachKitContext(kitContext!!)
        view.attachLiveClient(client!!)
        addView(view)
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

    private var originPreViewParent: ViewGroup? = null
    private var originIndex = -1;
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
            localRenderView = kitContext?.getPusherRenderViewCall?.invoke()?.getView() ?: return
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
        }

        override fun onStop(pkSession: QPKSession, code: Int, msg: String) {
            flMeContainer.removeView(localRenderView)
            originPreViewParent?.addView(
                localRenderView, originIndex, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
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

}
