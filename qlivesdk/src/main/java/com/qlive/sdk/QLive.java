package com.qlive.sdk;

import android.content.Context;
import android.content.Intent;

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

/**
 * 低代码直播客户端
 */
public class QLive {

    /**
     * 初始化
     * @param context 安卓上下文
     * @param config  sdk配置
     * @param tokenGetter token获取
     */
    public static void init(Context context, QSdkConfig config, QTokenGetter tokenGetter) {
        QLiveDelegate.INSTANCE.init(context, config, tokenGetter);
    }

    /**
     * 登陆 认证成功后才能使用qlive的功能
     * @apiNote 认证成功后才能使用qlive的功能
     * @param callBack 操作回调
     */
    public static void auth(@NotNull QLiveCallBack<Void> callBack) {
        QLiveDelegate.INSTANCE.login(callBack);
    }

    /**
     * 跟新用户信息
     * @param userInfo 用户参数
     * @param callBack 回调函数
     */
    public static void setUser(@NotNull QUserInfo userInfo, @NotNull QLiveCallBack<Void> callBack) {
        QLiveUser user = new QLiveUser();
        user.avatar = userInfo.avatar;
        user.nick = userInfo.nick;
        user.extensions = userInfo.extension;
        QLiveDelegate.INSTANCE.setUser(user, callBack);
    }

    /**
     * 获取当前登陆用户资料
     * @return QLiveUser
     */
    public static QLiveUser getLoginUser() {
        return UserDataSource.loginUser;
    }

    /**
     * 创建推流客户端
     * @return QPusherClient
     */
    public static QPusherClient createPusherClient() {
        return QPusherClientImpl.Companion.create();
    }

    /**
     * 创建拉流客户端
     * @return QPlayerClient
     */
    public static QPlayerClient createPlayerClient() {
        return QPlayerClientImpl.Companion.create();
    }

    /**
     * 获取房间管理接口
     * @return  QRooms
     */
    public static QRooms getRooms() {
        return QLiveDelegate.INSTANCE.getQRooms();
    }

    /**
     * 获得UIkit
     * @return QLiveUIKit
     */
    public static QLiveUIKit getLiveUIKit() {
        return QLiveDelegate.INSTANCE.getUIKIT();
    }
}
