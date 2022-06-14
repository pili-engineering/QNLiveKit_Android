package com.qncube.liveuikit

import com.qncube.kitlivepre.LivePreView
import com.qncube.liveuikit.component.BottomFucBarView
import com.qncube.liveuikit.component.InnerCoverView
import com.qncube.liveuikit.component.OuterCoverView
import com.qncube.liveuikit.component.RoomBackGroundView
import com.qncube.uikitcore.QLiveComponent
import com.qncube.liveuikit.component.QLiveFunctionComponent
import com.qncube.liveuikit.component.QLiveView
import com.qncube.liveuikit.hook.KITFunctionInflaterFactory
import com.qncube.uikitdanmaku.DanmakuTrackManagerView
import com.qncube.uikitlinkmic.*
import com.qncube.uikitpk.PKCoverView
import com.qncube.uikitpk.PKPreView
import com.qncube.uikitpk.ShowPKApplyFunctionComponent
import com.qncube.uikitpk.StartPKView
import com.qncube.uikitpublicchat.InputView
import com.qncube.uikitpublicchat.PublicChatView
import com.qncube.uikitpublicchat.RoomNoticeView
import com.qncube.uikituser.*
import java.util.*


/**
 * 槽位表
 */
class RoomPage {

    /**
     * 自定义布局 如果需要替换自定义布局
     */
    var customLayoutID = -1

    /**
     * 房间背景UI组件
     */
    val roomBackGroundView = QLiveView(RoomBackGroundView::class.java)

    /**
     * 左上角房间的房主信息UI组件
     */
    val roomHostView = QLiveView(RoomHostView::class.java)

    /**
     * 开播准备UI组件 -开播预览设置覆盖层
     */
    val livePreView = QLiveView(LivePreView::class.java)

    /**
     * 右上角在线用户列表UI组件
     */
    val onlineUserView = QLiveView(OnlineUserView::class.java)

    /**
     * 右上角房间人数UI组件
     */
    val roomMemberCountView = QLiveView(RoomMemberCountView::class.java)

    /**
     * 右上角房间id text UI组件
     */
    val roomIDView = QLiveView(RoomIdView::class.java)

    /**
     * 右上角房间计时器UI组件
     */
    val roomTimerView = QLiveView(RoomTimerView::class.java)

    /**
     * 弹幕UI组件
     */
    val danmakuTrackView = QLiveView(DanmakuTrackManagerView::class.java)

    /**
     * 公屏消息列表UI组件
     */
    val publicChatView = QLiveView(PublicChatView::class.java)

    /**
     * 公告UI组件
     */
    val roomNoticeView = QLiveView(RoomNoticeView::class.java)

    /**
     * 主播开始pk按钮UI组件
     */
    val startPKView = QLiveView(StartPKView::class.java)

    /**
     * PK覆盖层自定义UI
     * 处理里pk开始就显示 pk结束就隐藏 UI视图内容空实现 用户可以参照替换实现自己的自定义PK视图
     */
    val pkCoverView = QLiveView(PKCoverView::class.java)

    /**
     * pk主播两个主播窗口UI组件
     */
    val pkPreView = QLiveView(PKPreView::class.java)

    /**
     * 连麦中的用户UI组件
     * 麦位UI
     */
    val linkersView = QLiveView(MicLinkersView::class.java)

    /**
     * 房间底部 输入框
     */
    val showInputView = QLiveView(InputView::class.java)

    /**
     * 右下角功能栏目 --连麦弹幕关闭按钮等功能栏
     */
    val bottomFucBar = QLiveView(BottomFucBarView::class.java)

    /**
     *  房间全局外层覆盖自定义UI
     */
    val outerCoverView = QLiveView(OuterCoverView::class.java)

    /**
     *  全局底层覆盖自定义UI
     */
    val mInnerCoverView = QLiveView(InnerCoverView::class.java)

    /**
     * 主播收到pk邀请 展示弹窗 事件监听功能组件
     */
    val showPKApplyComponent = QLiveFunctionComponent(ShowPKApplyFunctionComponent())

    /**
     * 主播收到连麦申请 展示弹窗 事件监听功能组件
     */
    val showLinkMicApplyComponent = QLiveFunctionComponent(ShowLinkMicApplyComponent())

    /**
     * 注册自定义功能组件  事件处理组件无UI
     */
    fun addFucComponent(component: QLiveComponent) {
        KITFunctionInflaterFactory.functionComponents.add(component)
    }

}