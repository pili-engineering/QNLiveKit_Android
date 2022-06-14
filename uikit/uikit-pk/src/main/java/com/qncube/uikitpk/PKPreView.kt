package com.qncube.uikitpk

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.nucube.rtclive.CameraMergeOption
import com.nucube.rtclive.MicrophoneMergeOption
import com.nucube.rtclive.MixStreamParams
import com.nucube.rtclive.QNMergeOption
import com.qbcube.pkservice.QNPKService
import com.qbcube.pkservice.QNPKSession
import com.qiniu.droid.rtc.QNTextureView
import com.qncube.linkmicservice.QNLinkMicService
import com.qncube.playerclient.QNLivePullClient
import com.qncube.liveroomcore.ClientType
import com.qncube.liveroomcore.Extension
import com.qncube.pushclient.QNLivePushClient
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
        val view = if (client?.clientType == ClientType.PLAYER) {
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

    private val mPKServiceListener = object : QNPKService.PKServiceListener {
        override fun onInitPKer(pkSession: QNPKSession) {
            addView()
        }

        override fun onStart(pkSession: QNPKSession) {
            addView()
        }

        override fun onStop(pkSession: QNPKSession, code: Int, msg: String) {
            removeView()
        }

        override fun onWaitPeerTimeOut(pkSession: QNPKSession) {}


        override fun onPKExtensionUpdate(pkSession: QNPKSession, extension: Extension) {
        }
    }

    private var originParent: ViewGroup? = null
    private var originIndex = 0
    private fun addView() {
        val player = (client as QNLivePullClient).pullPreview.view
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
        val player = (client as QNLivePullClient).pullPreview.view
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
        client!!.getService(QNPKService::class.java).addPKServiceListener(mPKServiceListener)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
        if (event == Lifecycle.Event.ON_DESTROY) {
            client?.getService(QNPKService::class.java)?.removePKServiceListener(mPKServiceListener)
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
    private val mPKMixStreamAdapter = object : QNPKService.PKMixStreamAdapter {

        override fun onPKLinkerJoin(pkSession: QNPKSession): MutableList<QNMergeOption> {
            val ops = ArrayList<QNMergeOption>()
            val peer = if (pkSession.initiator.userId == user?.userId) {
                pkSession.receiver
            } else {
                pkSession.initiator
            }
            ops.add(QNMergeOption().apply {
                uid = user!!.userId
                cameraMergeOption = CameraMergeOption().apply {
                    isNeed = true
                    mX = 0
                    mY = 0
                    mZ = 0
                    mWidth = PKUIHelper.mixWidth / 2
                    mHeight = PKUIHelper.mixHeight
                    // mStretchMode=QNRenderMode.
                }
                microphoneMergeOption = MicrophoneMergeOption().apply {
                    isNeed = true
                }
            })
            ops.add(QNMergeOption().apply {
                uid = peer.userId
                cameraMergeOption = CameraMergeOption().apply {
                    isNeed = true
                    mX = PKUIHelper.mixWidth / 2
                    mY = 0
                    mZ = 0
                    mWidth = PKUIHelper.mixWidth / 2
                    mHeight = PKUIHelper.mixHeight
                    // mStretchMode=QNRenderMode.
                }
                microphoneMergeOption = MicrophoneMergeOption().apply {
                    isNeed = true
                }
            })
            return ops
        }

        override fun onPKMixStreamStart(pkSession: QNPKSession): MixStreamParams {
            return MixStreamParams().apply {
                mixStreamWidth = PKUIHelper.mixWidth
                mixStringHeight = PKUIHelper.mixHeight
                mixBitrate = 1500 * 1000
                fps = 25
            }
        }

        override fun onPKLinkerLeft(): MutableList<QNMergeOption> {
            val ops = ArrayList<QNMergeOption>()
            client?.getService(QNLinkMicService::class.java)
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
    private val mPKServiceListener = object : QNPKService.PKServiceListener {

        override fun onInitPKer(pkSession: QNPKSession) {}

        override fun onStart(pkSession: QNPKSession) {
            val peer = if (pkSession.initiator.userId == user?.userId) {
                pkSession.receiver
            } else {
                pkSession.initiator
            }
            localRenderView = (client as QNLivePushClient).localPreView as View
            originPreViewParent = localRenderView!!.parent as ViewGroup
            originIndex = originPreViewParent?.indexOfChild(localRenderView) ?: 0
            originPreViewParent?.removeView(localRenderView)
            flMeContainer.addView(localRenderView)
            flPeerContainer.addView(
                QNTextureView(context).apply {
                    client?.getService(QNPKService::class.java)
                        ?.setPeerAnchorPreView(peer.userId, this)
                },
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }

        override fun onStop(pkSession: QNPKSession, code: Int, msg: String) {
            flMeContainer.removeView(localRenderView)
            originPreViewParent?.addView(
                localRenderView, originIndex, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            flPeerContainer.removeAllViews()
        }

        override fun onWaitPeerTimeOut(pkSession: QNPKSession) {}
        override fun onPKExtensionUpdate(pkSession: QNPKSession, extension: Extension) {}
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_anchor_pk_preview
    }

    override fun initView() {
        client!!.getService(QNPKService::class.java).addPKServiceListener(mPKServiceListener)
        client!!.getService(QNPKService::class.java).setPKMixStreamAdapter(mPKMixStreamAdapter)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
        if (event == Lifecycle.Event.ON_DESTROY) {
            client?.getService(QNPKService::class.java)?.removePKServiceListener(mPKServiceListener)
            client?.getService(QNPKService::class.java)?.setPKMixStreamAdapter(null)
        }
    }

}
