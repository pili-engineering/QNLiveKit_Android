package com.qncube.roomservice;

import com.qncube.liveroomcore.been.QExtension;
import com.qncube.liveroomcore.QLiveCallBack;
import com.qncube.liveroomcore.been.QLiveRoomInfo;
import com.qncube.liveroomcore.been.QLiveUser;
import com.qncube.liveroomcore.service.QLiveService;

import java.util.List;

/**
 * 房间服务
 */
public interface QRoomService extends QLiveService {

    public interface RoomServiceListener {
        /**
         * 直播间某个属性变化
         *
         * @param extension
         */
        void onRoomExtensions(Exception extension);
    }

    public void addRoomServiceListener(RoomServiceListener listener);

    public void removeRoomServiceListener(RoomServiceListener listener);

    /**
     * 获取当前房间
     *
     * @return
     */
    public QLiveRoomInfo getCurrentRoomInfo();

    /**
     * 刷新房间信息
     */
    public void refreshRoomInfo(QLiveCallBack<QLiveRoomInfo> callBack);

    /**
     * 跟新直播扩展信息
     *
     * @param QExtension
     */
    public void updateRoomExtension(QExtension QExtension, QLiveCallBack<Void> callBack);


    /**
     * 当前房间在线用户
     *
     * @param callBack
     */
    public void getOnlineUser(int pageNum, int pageSize, QLiveCallBack<List<QLiveUser>> callBack);

    /**
     * 某个房间在线用户
     *
     * @param callBack
     */
    public void getOnlineUser(int pageNum, int pageSize, String roomId, QLiveCallBack<List<QLiveUser>> callBack);

    /**
     * 使用用户ID搜索房间用户
     *
     * @param uid
     * @param callBack
     */
    public void searchUserByUserId(String uid, QLiveCallBack<QLiveUser> callBack);

    /**
     * 使用用户im uid 搜索用户
     *
     * @param imUid
     * @param callBack
     */
    public void searchUserByIMUid(String imUid, QLiveCallBack<QLiveUser> callBack);


}
