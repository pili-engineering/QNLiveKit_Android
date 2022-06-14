package com.qncube.liveroomcore;

import android.content.Context;

import com.nucube.http.OKHttpService;
import com.nucube.http.PageData;
import com.qncube.liveroomcore.datasource.RoomDataSource;
import com.qncube.liveroomcore.datasource.UserDataSource;
import com.qncube.liveroomcore.mode.QNCreateRoomParam;

import com.qncube.liveroomcore.mode.QLiveRoomInfo;
import com.qncube.liveroomcore.mode.QNLiveUser;

import java.util.HashMap;

/**
 * 房间业务管理
 */
public class QNLiveRoomEngine {

    private static QNLiveUser mUser = new QNLiveUser();
    private static UserDataSource mUserDataSource = new UserDataSource();
    private static RoomDataSource mRoomDataSource = new RoomDataSource();

    /**
     * 初始化
     *
     * @param context
     * @param token
     * @param callBack
     */
    public static void init(Context context, String token, QLiveCallBack<Void> callBack) {
        OKHttpService.INSTANCE.setToken(token);
        AppCache.appContext = context;
        mUserDataSource.loginUser(context, new QLiveCallBack<QNLiveUser>() {

            @Override
            public void onError(int code, String msg) {
                callBack.onError(code, msg);
            }

            @Override
            public void onSuccess(QNLiveUser data) {
                mUser = data;
                callBack.onSuccess(null);
            }
        });
    }

    /**
     * 绑定自己用户信息
     *
     * @param avatar
     * @param nickName
     * @param extensions
     * @param callBack
     */
    public static void updateUserInfo(String avatar, String nickName, HashMap<String, String> extensions, QLiveCallBack<QNLiveUser> callBack) {
        mUserDataSource.updateUser(avatar, nickName, extensions, new QLiveCallBack<Void>() {
            @Override
            public void onError(int code, String msg) {
                callBack.onError(code, msg);
            }

            @Override
            public void onSuccess(Void data) {
                mUser.nick = nickName;
                mUser.extensions = extensions;
                mUser.avatar = avatar;
                callBack.onSuccess(mUser);
            }
        });
    }

    /**
     * 创建房间
     *
     * @param param
     * @param callBack
     */
    public static void createRoom(QNCreateRoomParam param, QLiveCallBack<QLiveRoomInfo> callBack) {
        mRoomDataSource.createRoom(param, callBack);
    }

    /**
     * 删除房间
     *
     * @param callBack
     */
    public static void deleteRoom(String liveId, QLiveCallBack<Void> callBack) {
        mRoomDataSource.deleteRoom(liveId, callBack);
    }

    /**
     * 房间列表
     *
     * @param pageNumber
     * @param pageSize
     * @param callBack
     */
    public static void listRoom(int pageNumber, int pageSize, QLiveCallBack<PageData<QLiveRoomInfo>> callBack) {
        mRoomDataSource.listRoom(pageNumber, pageSize, callBack);
    }

    /**
     * 查询房间
     *
     * @param liveId
     * @param callBack
     */
    public static void getRoomInfo(String liveId, QLiveCallBack<QLiveRoomInfo> callBack) {
        mRoomDataSource.refreshRoomInfo(liveId, callBack);
    }

    public static QNLiveUser getCurrentUserInfo() {
        return mUser;
    }
}