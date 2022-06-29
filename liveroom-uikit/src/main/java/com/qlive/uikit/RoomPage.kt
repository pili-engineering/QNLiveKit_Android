package com.qlive.uikit

import android.content.Context
import com.qlive.uikit.component.LivePreView
import com.qlive.core.QLiveCallBack
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.sdk.QLive
import com.qlive.sdk.QPage
import com.qlive.uikit.component.*
import com.qlive.uikitcore.QLiveComponent
import com.qlive.uikit.hook.KITFunctionInflaterFactory
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
 * 房间页面
 */
class RoomPage : QPage {

    /**
     * 主播收到pk邀请 展示弹窗 事件监听功能组件
     */
    val showPKApplyComponent = QLiveFunctionComponent(ShowPKApplyFunctionComponent())

    /**
     * 主播收到连麦申请 展示弹窗 事件监听功能组件
     */
    val showLinkMicApplyComponent = QLiveFunctionComponent(ShowLinkMicApplyComponent())

    /**
     * 用户收到主播连麦邀请 展示弹窗
     */
    val playerShowBeInvitedComponent = QLiveFunctionComponent(PlayerShowBeInvitedComponent())

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