package com.qlive.uikit

import android.content.Context
import com.qlive.kitlivepre.LivePreView
import com.qlive.core.QLiveCallBack
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.sdk.QLive
import com.qlive.sdk.QPage
import com.qlive.uikit.component.*
import com.qlive.uikitcore.QLiveComponent
import com.qlive.uikit.hook.KITFunctionInflaterFactory
import com.qlive.uikitcore.getCode
import com.qlive.uikitdanmaku.DanmakuTrackManagerView
import com.qlive.uikitlinkmic.*
import com.qlive.uikitpk.PKCoverView
import com.qlive.uikitpk.PKPreView
import com.qlive.uikitpk.ShowPKApplyFunctionComponent
import com.qlive.uikitpk.StartPKView
import com.qlive.uikitpublicchat.InputView
import com.qlive.uikitpublicchat.PublicChatView
import com.qlive.uikitpublicchat.RoomNoticeView
import com.qlive.uikituser.*

/**
 * 槽位表
 */
class RoomPage : QPage {

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
    val innerCoverView = QLiveView(InnerCoverView::class.java)

    /**
     * 主播收到pk邀请 展示弹窗 事件监听功能组件
     */
    val showPKApplyComponent = QLiveFunctionComponent(ShowPKApplyFunctionComponent())

    /**
     * 主播收到连麦申请 展示弹窗 事件监听功能组件
     */
    val showLinkMicApplyComponent = QLiveFunctionComponent(ShowLinkMicApplyComponent())

    /**
     * 房主离线事件处理
     */
    val anchorOfflineMonitorComponent = QLiveFunctionComponent(AnchorOfflineMonitorComponent())

    /**
     * 注册自定义功能组件  事件处理组件无UI
     */
    fun addFunctionComponent(component: QLiveComponent) {
        KITFunctionInflaterFactory.functionComponents.add(component)
    }


    /**
     * 根据QLiveRoomInfo自动判断跳转主播页面还是观众页面
     */
    fun startRoomActivity(
        context: Context,
        roomInfo: QLiveRoomInfo,
        callBack: QLiveCallBack<QLiveRoomInfo>?
    ) {
        if (roomInfo.anchor.userId == QLive.getLoginUser().userId) {
            RoomPushActivity.start(context, roomInfo.liveID, callBack)
        } else {
            RoomPullActivity.start(context, roomInfo.liveID, callBack)
        }
    }

    /**
     * 跳转观众直播间
     */
    fun startPlayerRoomActivity(
        context: Context,
        liveRoomId: String,
        callBack: QLiveCallBack<QLiveRoomInfo>?
    ) {
        RoomPullActivity.start(context, liveRoomId, callBack)
    }

    /**
     * 跳转已经存在的主播直播间
     */
    fun startAnchorRoomActivity(
        context: Context,
        liveRoomId: String,
        callBack: QLiveCallBack<QLiveRoomInfo>?
    ) {
        RoomPushActivity.start(context, liveRoomId, callBack)
    }

    /**
     * 跳转主播开播预览页面
     */
    fun startAnchorRoomWithPreview(context: Context, callBack: QLiveCallBack<QLiveRoomInfo>?) {
        RoomPushActivity.start(context, callBack)
    }

    private val mRoomCoverViewPage = QLiveView(RoomCoverViewPage::class.java)

    /**
     * 自定义布局 如果需要替换自定义布局
     * 自定义主播端布局 如果需要替换自定义布局
     */
    var anchorCustomLayoutID = -1
        set(value) {
            field = value
            RoomPushActivity.replaceLayoutId = value
        }

    /**
     *  自定义观众端布局 如果需要替换自定义布局
     */
    var playerCustomLayoutID = -1
        set(value) {
            field = value
            RoomPullActivity.replaceLayoutId = value
        }

}