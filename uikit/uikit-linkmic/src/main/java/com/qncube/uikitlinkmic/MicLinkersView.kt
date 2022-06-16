package com.qncube.uikitlinkmic

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qiniu.droid.rtc.QNConnectionState
import com.qiniu.droid.rtc.QNTextureView
import com.qncube.linkmicservice.QAnchorHostMicHandler
import com.qncube.linkmicservice.QAudienceMicHandler
import com.qncube.linkmicservice.QLinkMicService
import com.qncube.linkmicservice.QLinkMicServiceListener
import com.qncube.linveroominner.asToast
import com.qncube.liveroomcore.been.QMicLinker
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.QClientType
import com.qncube.liveroomcore.been.QExtension
import com.qncube.uikitcore.*
import kotlinx.android.synthetic.main.kit_view_linkers.view.*

/**
 * 连麦麦位列表
 */
class MicLinkersView : QBaseRoomFrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val linkService get() = client!!.getService(QLinkMicService::class.java)!!

    //麦位列表适配器
    private var mLinkerAdapter: BaseQuickAdapter<QMicLinker, BaseViewHolder> =
        LinkerAdapter().apply {
            setOnItemChildClickListener { _, view, position ->
                //麦位点击事件
            }
        }

    //麦位监听
    private val mQLinkMicServiceListener = object :
        QLinkMicServiceListener {
        override fun onInitLinkers(linkers: MutableList<QMicLinker>) {
            Log.d("LinkerSlot", " 同步麦位${linkers.size}")
            val lcs = linkers.filter {
                it.user.userId != roomInfo?.anchor?.userId
            }
            mLinkerAdapter.setNewData(lcs)

        }

        override fun onLinkerJoin(micLinker: QMicLinker) {
            Log.d("LinkerSlot", " onUserJoinLink 有人上麦 ${micLinker.user.nick}")
            mLinkerAdapter.addData(micLinker)
            if (isMyOnMic()) {
                addLinkerPreview(micLinker)
            }
        }

        override fun onLinkerLeft(micLinker: QMicLinker) {
            Log.d("LinkerSlot", " onUserLeft 有人下麦 ${micLinker.user.nick}")
            val index = mLinkerAdapter.indexOf(micLinker)
            removePreview(index, micLinker)
            mLinkerAdapter.remove(index)
        }

        override fun onLinkerMicrophoneStatusChange(micLinker: QMicLinker) {
            val index = mLinkerAdapter.indexOf(micLinker)
            mLinkerAdapter.notifyItemChanged(index)
        }

        override fun onLinkerCameraStatusChange(micLinker: QMicLinker) {
            val index = mLinkerAdapter.indexOf(micLinker)
            mLinkerAdapter.notifyItemChanged(index)
            mMicSeatView.convertItem(index, micLinker)
        }

        override fun onLinkerKicked(micLinker: QMicLinker, msg: String) {
            if (micLinker.user.userId == user?.userId) {
                linkService.audienceMicHandler.stopLink(object :
                    QLiveCallBack<Void> {
                    override fun onError(code: Int, msg: String?) {
                        msg?.asToast()
                    }

                    override fun onSuccess(data: Void?) {}
                })
            }
        }

        override fun onLinkerExtensionUpdate(micLinker: QMicLinker, extension: QExtension) {}
    }

    //观众端连麦监听
    private val mQAudienceMicHandler = object : QAudienceMicHandler.QLinkMicListener {

        /**
         * 本地角色变化
         */
        override fun onRoleChange(isLinker: Boolean) {
            Log.d("LinkerSlot", " lonLocalRoleChange 本地角色变化 ${isLinker}")
            if (isLinker) {
                client?.getService(QLinkMicService::class.java)?.allLinker?.forEach {
                    Log.d("LinkerSlot", " zb 添加窗口 ${it.user.nick}")
                    addLinkerPreview(it)
                }
            } else {
                client?.getService(QLinkMicService::class.java)?.allLinker?.forEach {
                    Log.d("LinkerSlot", "  zb移除窗口 ${it.user.nick}")
                    removePreview(mLinkerAdapter.indexOf(it), it)
                }
            }
        }
    }

    //连麦混流适配器
    private val mQMixStreamAdapter =
        QAnchorHostMicHandler.QMixStreamAdapter { micLinkers, target, isJoin ->
            LinkerUIHelper.getLinkers(micLinkers.map { it.user }, roomInfo!!)
        }

    override fun getLayoutId(): Int {
        return R.layout.kit_view_linkers
    }

    override fun initView() {
        if (client!!.clientType == QClientType.PUSHER) {
            client!!.getService(QLinkMicService::class.java).anchorHostMicHandler.setMixStreamAdapter(
                mQMixStreamAdapter
            )
        } else {
            client!!.getService(QLinkMicService::class.java).audienceMicHandler.addLinkMicListener(
                mQAudienceMicHandler
            )
        }
        client!!.getService(QLinkMicService::class.java).addMicLinkerListener(mQLinkMicServiceListener)

        recyLinker.layoutManager = LinearLayoutManager(context)
        flLinkContent.post {
            init()
        }
    }

    private fun isMyOnMic(): Boolean {
        if (client?.clientType == QClientType.PUSHER) {
            return true
        }
        linkService.allLinker.forEach {
            if (it.user.userId == user?.userId) {
                return true
            }
        }
        return false
    }

    private fun addLinkerPreview(micLinker: QMicLinker) {
        if (micLinker.user.userId != roomInfo?.anchor?.userId) {
            Log.d("LinkerSlot", "  添加窗口用户")
            mMicSeatView.addItemView(micLinker, linkService)

        } else {
            Log.d("LinkerSlot", "  添加窗口房主")
            flAnchorSurfaceCotiner.visibility = View.VISIBLE
            flAnchorSurfaceCotiner.addView(
                QNTextureView(context).apply {
                    linkService.setUserPreview(micLinker.user?.userId ?: "", this)
                },
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    private fun removePreview(index: Int, micLinker: QMicLinker) {
        if (micLinker.user.userId != roomInfo?.anchor?.userId) {
            Log.d("LinkerSlot", "  移除窗口${micLinker.user.nick}")
            mMicSeatView.removeItem(index)
        } else {
            Log.d("LinkerSlot", "  移除窗口房主")
            flAnchorSurfaceCotiner.removeAllViews()
            flAnchorSurfaceCotiner.visibility = View.INVISIBLE
        }
    }

    private fun BaseQuickAdapter<QMicLinker, BaseViewHolder>.indexOf(linker: QMicLinker): Int {
        data.forEachIndexed { index, qnMicLinker ->
            if (qnMicLinker.user?.userId == linker.user?.userId) {
                return index
            }
        }
        return -1
    }

    private fun init() {
        LinkerUIHelper.attachUIWidth(flLinkContent.width, flLinkContent.height)
        val rcLp: FrameLayout.LayoutParams =
            recyLinker.layoutParams as FrameLayout.LayoutParams
        rcLp.topMargin = LinkerUIHelper.uiTopMargin
        val rcSurfaceLp = mMicSeatView.layoutParams as FrameLayout.LayoutParams
        rcSurfaceLp.topMargin = LinkerUIHelper.uiTopMargin
        mLinkerAdapter.bindToRecyclerView(recyLinker)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        super.onStateChanged(source, event)
        if (event == Lifecycle.Event.ON_DESTROY) {
            if (client?.clientType == QClientType.PUSHER) {
                client?.getService(QLinkMicService::class.java)?.anchorHostMicHandler?.setMixStreamAdapter(
                    null
                )
            } else {
                client?.getService(QLinkMicService::class.java)?.audienceMicHandler?.removeLinkMicListener(
                    mQAudienceMicHandler
                )
            }
            client?.getService(QLinkMicService::class.java)
                ?.removeMicLinkerListener(mQLinkMicServiceListener)
        }
    }

}