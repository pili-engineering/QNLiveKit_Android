package com.qlive.sdk;

import android.content.Context;

import androidx.annotation.NonNull;

import com.qlive.coreimpl.QLiveDelegate;
import com.qlive.core.QTokenGetter;
import com.qlive.coreimpl.UserDataSource;
import com.qlive.core.QLiveCallBack;
import com.qlive.core.QPlayerClient;
import com.qlive.core.QPusherClient;
import com.qlive.core.QRooms;
import com.qlive.uikit.QLiveUIKit;
import com.qlive.playerclient.QPlayerClientImpl;
import com.qlive.pushclient.QPusherClientImpl;

public class QLive {

    static {
        QLiveDelegate.INSTANCE.setQLiveSdk(new QLiveDelegate.QLiveSdk() {
            @NonNull
            @Override
            public QPlayerClient createPlayerClientCall() {
                return createPlayerClient();
            }

            @NonNull
            @Override
            public QPusherClient createPusherClientCall() {
                return createPusherClient();
            }
        });
    }

    /**
     * 初始化
     *
     * @param context
     * @param tokenGetter
     * @param callBack
     * @return
     */
    public static void init(Context context, QTokenGetter tokenGetter, QLiveCallBack<Void> callBack) {
        QLiveDelegate.INSTANCE.init(context, tokenGetter, callBack);
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

    public static QPusherClient createPusherClient() {
        return QPusherClientImpl.Companion.create();
    }

    public static QPlayerClient createPlayerClient() {
        return QPlayerClientImpl.Companion.create();
    }

    public static QLiveUIKit getLiveUIKit() {
        return QLiveUIKit.Companion.getLiveUIKitInstance();
    }

    public static QRooms getRooms() {
        return QLiveDelegate.INSTANCE.getQRooms();
    }
}
