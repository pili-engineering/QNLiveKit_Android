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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QLive {

    /**
     * 初始化
     *
     * @param context
     * @param tokenGetter
     * @return
     */
    public static void init(Context context, QSdkConfig config, QTokenGetter tokenGetter) {
        QLiveDelegate.INSTANCE.init(context, config, tokenGetter);
    }

    /**
     * 登陆
     * @param callBack
     */
    public static void auth(@NotNull QLiveCallBack<Void> callBack) {
        QLiveDelegate.INSTANCE.login(callBack);
    }

    /**
     * 跟新用户信息
     *
     * @param userInfo
     * @param callBack
     */
    public static void setUser(@NotNull QUserInfo userInfo, @NotNull QLiveCallBack<Void> callBack) {
        QLiveUser user = new QLiveUser();
        user.avatar = userInfo.avatar;
        user.nick = userInfo.nick;
        user.extensions = userInfo.extension;
        QLiveDelegate.INSTANCE.setUser(user, callBack);
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
