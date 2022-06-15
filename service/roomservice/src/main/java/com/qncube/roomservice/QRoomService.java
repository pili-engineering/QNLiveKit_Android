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

    public void addRoomServiceListener(QRoomServiceListener listener);

    public void removeRoomServiceListener(QRoomServiceListener listener);

    /**
     * 获取当前房间
     *
     * @return
     */
    public QLiveRoomInfo getRoomInfo();

    /**
     * 刷新房间信息
     */
    public void getRoomInfo(QLiveCallBack<QLiveRoomInfo> callBack);

    /**
     * 跟新直播扩展信息
     *
     * @param extension
     */
    public void updateExtension(QExtension extension, QLiveCallBack<Void> callBack);


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
