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
import com.qlive.uikitlinkmic.*
import com.qlive.uikitpk.ShowPKApplyFunctionComponent

/**
 * Room page
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
     * Add function component
     *
     * @param component
     */
    fun addFunctionComponent(component: QLiveComponent) {
        KITFunctionInflaterFactory.functionComponents.add(component)
    }


    /**
     * Start room activity
     *
     * @param context
     * @param roomInfo
     * @param callBack
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
     * Start player room activity
     *
     * @param context
     * @param liveRoomId
     * @param callBack
     */
    fun startPlayerRoomActivity(
        context: Context,
        liveRoomId: String,
        callBack: QLiveCallBack<QLiveRoomInfo>?
    ) {
        RoomPullActivity.start(context, liveRoomId, callBack)
    }

    /**
     * Start anchor room activity
     *
     * @param context
     * @param liveRoomId
     * @param callBack
     */
    fun startAnchorRoomActivity(
        context: Context,
        liveRoomId: String,
        callBack: QLiveCallBack<QLiveRoomInfo>?
    ) {
        RoomPushActivity.start(context, liveRoomId, callBack)
    }

    /**
     * Start anchor room with preview
     *
     * @param context
     * @param callBack
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