package com.qlive.sdk;

import android.content.Context;

import androidx.annotation.NonNull;

import com.qncube.linveroominner.QLiveDelegate;
import com.qncube.linveroominner.QRoomImpl;
import com.qncube.linveroominner.QTokenGetter;
import com.qncube.linveroominner.UserDataSource;
import com.qncube.liveroomcore.QLiveCallBack;
import com.qncube.liveroomcore.QPlayerClient;
import com.qncube.liveroomcore.QPusherClient;
import com.qncube.liveroomcore.QRooms;
import com.qncube.liveuikit.QLiveUIKit;
import com.qncube.playerclient.QPlayerClientImpl;
import com.qncube.pushclient.QPusherClientImpl;

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
