package com.qlive.uikitlinkmic

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.qlive.linkmicservice.QAnchorHostMicHandler
import com.qlive.linkmicservice.QAudienceMicHandler
import com.qlive.linkmicservice.QLinkMicService
import com.qlive.linkmicservice.QLinkMicServiceListener
import com.qlive.core.been.QMicLinker
import com.qlive.core.*
import com.qlive.core.QClientType
import com.qlive.core.been.QExtension
import com.qlive.pkservice.QPKService
import com.qlive.rtclive.QPushTextureView
import com.qlive.uikitcore.*
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

    //麦位列表适配器 如果需要修改UI 请替换适配器
    private var mLinkerAdapter: BaseQuickAdapter<QMicLinker, BaseViewHolder> =
        LinkerAdapter().apply {
            isOnMic = {
                isMyOnMic()
            }
            setOnItemChildClickListener { _, view, position ->
                //麦位点击事件
            }
        }

    init {
        //连麦混流参数  拉流端看到混流的位置 和上麦后麦位位置如果需要大致匹配 需要经过屏幕尺寸的换算
        //demo实现的混流方式是 混流到右上角
        /**
         * 混流每个麦位宽大小
         */
        LinkerUIHelper.mixMicWidth = 184

        /**
         * 混流每个麦位高
         */
        LinkerUIHelper.mixMicHeight = 184

        /**
         * 混流第一个麦位上间距
         */
        LinkerUIHelper.mixTopMargin = 174
        /**
         * 混流参数 每个麦位间距
         */
        LinkerUIHelper.micBottomMixMargin = 15
        /**
         * 混流参数 每个麦位右间距
         */
        LinkerUIHelper.micRightMixMargin = 30 * 3
    }

    private fun init() {
        //绑定屏幕尺寸开始换算
        LinkerUIHelper.attachUIWidth(flLinkContent.width, flLinkContent.height)

        //麦位覆盖
        val rcLp: FrameLayout.LayoutParams =
            recyLinker.layoutParams as FrameLayout.LayoutParams
        rcLp.topMargin = LinkerUIHelper.uiTopMargin

        //麦位预览窗口列表
        val rcSurfaceLp = mMicSeatView.layoutParams as FrameLayout.LayoutParams
        rcSurfaceLp.topMargin = LinkerUIHelper.uiTopMargin

        mLinkerAdapter.bindToRecyclerView(recyLinker)
    }

    //麦位监听
    private val mQLinkMicServiceListener = object :
        QLinkMicServiceListener {

        override fun onLinkerJoin(micLinker: QMicLinker) {
            Log.d("LinkerSlot", " onUserJoinLink 有人上麦 ${micLinker.user.nick}")
            mLinkerAdapter.addData(micLinker)
            if (isMyOnMic()) {
                //我是麦上用户则 添加预览窗口
                addLinkerPreview(micLinker)
            }
        }

        override fun onLinkerLeft(micLinker: QMicLinker) {
            Log.d("LinkerSlot", " onUserLeft 有人下麦 ${micLinker.user.nick}")
            //移除麦位
            val index = mLinkerAdapter.indexOf(micLinker)
            removePreview(index, micLinker)
            mLinkerAdapter.remove(index)
        }

        override fun onLinkerMicrophoneStatusChange(micLinker: QMicLinker) {
            val index = mLinkerAdapter.indexOf(micLinker)
            mLinkerAdapter.notifyItemChanged(index)
            mMicSeatView.convertItem(index, micLinker)
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
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
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
        @SuppressLint("NotifyDataSetChanged")
        override fun onRoleChange(isLinker: Boolean) {
            Log.d("LinkerSlot", " lonLocalRoleChange 本地角色变化 ${isLinker}")
            if (isLinker) {
                //我上麦了 切换连麦模式
                client?.getService(QLinkMicService::class.java)?.allLinker?.forEach {
                    Log.d("LinkerSlot", " zb 添加窗口 ${it.user.nick}")
                    addLinkerPreview(it)
                }
            } else {
                //我下麦了 切换拉流模式
                client?.getService(QLinkMicService::class.java)?.allLinker?.forEach {
                    Log.d("LinkerSlot", "  zb移除窗口 ${it.user.nick}")
                    removePreview(mLinkerAdapter.indexOf(it), it)
                }
            }
            mLinkerAdapter.notifyDataSetChanged()
        }
    }

    //连麦混流适配器
    private val mQMixStreamAdapter =
        QAnchorHostMicHandler.QMixStreamAdapter { micLinkers, target, isJoin ->
            LinkerUIHelper.getLinkersMixOp(micLinkers, roomInfo!!)
        }

    override fun getLayoutId(): Int {
        return R.layout.kit_view_linkers
    }

    override fun initView() {
        if (client!!.clientType == QClientType.PUSHER) {
            //我是主播
            client!!.getService(QLinkMicService::class.java).anchorHostMicHandler.setMixStreamAdapter(
                mQMixStreamAdapter
            )
        } else {
            //我是观众
            client!!.getService(QLinkMicService::class.java).audienceMicHandler.addLinkMicListener(
                mQAudienceMicHandler
            )
        }
        //添加连麦麦位监听
        client!!.getService(QLinkMicService::class.java)
            .addMicLinkerListener(mQLinkMicServiceListener)

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
                QPushTextureView(context).apply {
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onLeft() {
        super.onLeft()
        mLinkerAdapter.data.clear()
        mLinkerAdapter.notifyDataSetChanged()
        mMicSeatView.clear();
    }

}