package com.qncube.liveuikit;

import android.content.Context;

import com.qncube.liveroomcore.QLiveCallBack;
import com.qncube.liveroomcore.mode.QLiveRoomInfo;

/**
 * ui kit
 */
public class QNLiveRoomUIKit {

    /**
     * 槽位表
     */
    public static final RoomPage mViewSlotTable = new RoomPage();

    /**
     * 加入房间
     *
     * @param context
     * @param callBack
     */
    public static void joinRoom(Context context, String liveRoomId, QLiveCallBack<QLiveRoomInfo> callBack) {
        QNLiveRoomUIKitInner.INSTANCE.joinRoom(context, liveRoomId, callBack);
    }

    /**
     * 主播开播
     *
     * @param context
     * @param callBack
     */
    public static void createAndJoinRoom(Context context, QLiveCallBack<QLiveRoomInfo> callBack) {
        QNLiveRoomUIKitInner.INSTANCE.createAndJoinRoom(context, callBack);
    }
}