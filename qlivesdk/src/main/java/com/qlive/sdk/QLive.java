package com.qlive.sdk;

import android.content.Context;

import com.qlive.core.QSdkConfig;
import com.qlive.core.been.QLiveUser;
import com.qlive.coreimpl.QLiveDelegate;
import com.qlive.core.QTokenGetter;
import com.qlive.coreimpl.datesource.UserDataSource;
import com.qlive.core.QLiveCallBack;
import com.qlive.playerclient.QPlayerClient;
import com.qlive.pushclient.QPusherClient;
import com.qlive.core.QRooms;

import com.qlive.playerclient.QPlayerClientImpl;
import com.qlive.pushclient.QPusherClientImpl;

public class QLive {

    /**
     * 初始化
     *
     * @param context
     * @param tokenGetter
     * @param callBack
     * @return
     */
    public static void init(Context context, QSdkConfig config, QTokenGetter tokenGetter, QLiveCallBack<Void> callBack) {
        QLiveDelegate.INSTANCE.init(context,config, tokenGetter, callBack);
    }

    /**
     * 更新用户信息
     *
     * @param userInfo
     * @param callBack
     */
    public static void setUser(QUserInfo userInfo, QLiveCallBack<Void> callBack) {
        new UserDataSource().updateUser(userInfo.avatar, userInfo.nick, userInfo.extension, callBack);
    }

    public static QLiveUser getLoginUser() {
        return UserDataSource.loginUser;
    }

    public static QPusherClient createPusherClient() {
        return QPusherClientImpl.Companion.create();
    }

    public static QPlayerClient createPlayerClient() {
        return QPlayerClientImpl.Companion.create();
    }

    public static QRooms getRooms() {
        return QLiveDelegate.INSTANCE.getQRooms();
    }

    public static QLiveUIKit getLiveUIKit() {
        return QLiveDelegate.INSTANCE.getUIKIT();
    }
}
