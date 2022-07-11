package com.qlive.uikit;

import android.content.Context;

import com.qlive.core.QLiveCallBack;
import com.qlive.core.been.QLiveRoomInfo;
import com.qlive.sdk.QLive;
import com.qlive.sdk.QPage;
import com.qlive.uikit.component.AnchorOfflineMonitorComponent;
import com.qlive.uikit.component.QLiveFunctionComponent;
import com.qlive.uikit.hook.KITFunctionInflaterFactory;
import com.qlive.uikitcore.QLiveComponent;
import com.qlive.uikitlinkmic.PlayerShowBeInvitedComponent;
import com.qlive.uikitlinkmic.ShowLinkMicApplyComponent;
import com.qlive.uikitpk.ShowPKApplyFunctionComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.jvm.internal.Intrinsics;

public class RoomPage implements QPage {

    /**
     * 主播收到pk邀请 展示弹窗 事件监听功能组件
     */
    public QLiveFunctionComponent showPKApplyComponent = new QLiveFunctionComponent((new ShowPKApplyFunctionComponent()));
    /**
     * 主播收到连麦申请 展示弹窗 事件监听功能组件
     */
    public QLiveFunctionComponent showLinkMicApplyComponent = new QLiveFunctionComponent((QLiveComponent) (new ShowLinkMicApplyComponent()));
    /**
     * 用户收到主播连麦邀请 展示弹窗
     */
    public QLiveFunctionComponent playerShowBeInvitedComponent = new QLiveFunctionComponent((QLiveComponent) (new PlayerShowBeInvitedComponent()));
    /**
     * 房主离线事件处理
     */
    public QLiveFunctionComponent anchorOfflineMonitorComponent = new QLiveFunctionComponent((QLiveComponent) (new AnchorOfflineMonitorComponent()));


    private int anchorCustomLayoutID = -1;

    private int playerCustomLayoutID = -1;

    public int getAnchorCustomLayoutID() {
        return anchorCustomLayoutID;
    }

    /**
     * 自定义布局 如果需要替换自定义布局
     * 自定义主播端布局 如果需要替换自定义布局
     *
     * @param anchorCustomLayoutID 自定义布局ID
     */
    public void setAnchorCustomLayoutID(int anchorCustomLayoutID) {
        this.anchorCustomLayoutID = anchorCustomLayoutID;
    }

    public int getPlayerCustomLayoutID() {
        return playerCustomLayoutID;
    }

    /**
     * 自定义布局 如果需要替换自定义布局
     * 自定义主播端布局 如果需要替换自定义布局
     *
     * @param playerCustomLayoutID 自定义布局ID
     */
    public void setPlayerCustomLayoutID(int playerCustomLayoutID) {
        this.playerCustomLayoutID = playerCustomLayoutID;
    }


    /**
     * 添加 功能组件
     * 功能组件
     * @param component 功能组件
     */
    public final void addFunctionComponent(@NotNull QLiveComponent component) {
        Intrinsics.checkNotNullParameter(component, "component");
        KITFunctionInflaterFactory.INSTANCE.getFunctionComponents().add(component);
    }

    /**
     * 根据房间信息自动跳转主播页直播间或观众直播间
     *
     * @param context 安卓上下文
     * @param roomInfo 房间信息
     * @param callBack 回调
     */
    public final void startRoomActivity(@NotNull Context context, @NotNull QLiveRoomInfo roomInfo, @Nullable QLiveCallBack<QLiveRoomInfo> callBack) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(roomInfo, "roomInfo");
        String var10002;
        if (Intrinsics.areEqual(roomInfo.anchor.userId, QLive.getLoginUser().userId)) {
            var10002 = roomInfo.liveID;
            Intrinsics.checkNotNullExpressionValue(var10002, "roomInfo.liveID");
            RoomPushActivity.Companion.start(context, var10002, callBack);
        } else {
            var10002 = roomInfo.liveID;
            Intrinsics.checkNotNullExpressionValue(var10002, "roomInfo.liveID");
            RoomPullActivity.Companion.start(context, var10002, callBack);
        }

    }

    /**
     * 跳转观众直播间
     *
     * @param context    安卓上下文
     * @param liveRoomId 房间ID
     * @param callBack   回调
     */
    public final void startPlayerRoomActivity(@NotNull Context context, @NotNull String liveRoomId, @Nullable QLiveCallBack<QLiveRoomInfo> callBack) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(liveRoomId, "liveRoomId");
        RoomPullActivity.Companion.start(context, liveRoomId, callBack);
    }

    /**
     * 跳转已经存在的主播直播间
     * @param context 安卓上下文
     * @param liveRoomId 直播间ID
     * @param callBack 回调
     */
    public final void startAnchorRoomActivity(@NotNull Context context, @NotNull String liveRoomId, @Nullable QLiveCallBack<QLiveRoomInfo> callBack) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(liveRoomId, "liveRoomId");
        RoomPushActivity.Companion.start(context, liveRoomId, callBack);
    }

    /**
     * 跳转到创建直播间开播页面
     * @param context 安卓上下文
     * @param callBack 回调
     */
    public final void startAnchorRoomWithPreview(@NotNull Context context, @Nullable QLiveCallBack<QLiveRoomInfo> callBack) {
        Intrinsics.checkNotNullParameter(context, "context");
        RoomPushActivity.Companion.start(context, callBack);
    }


}
