package com.qlive.core;

import com.qlive.core.been.QCreateRoomParam;
import com.qlive.core.been.QLiveRoomInfo;

import java.util.List;

/**
 * 房间管理接口
 */
public interface QRooms {

    /**
     * 创建房间
     *
     * @param param 创建房间参数
     * @param callBack
     */
    void createRoom(QCreateRoomParam param, QLiveCallBack<QLiveRoomInfo> callBack);

    /**
     * 删除房间
     * @param roomID  房间ID
     * @param callBack
     */
    void deleteRoom(String roomID, QLiveCallBack<Void> callBack);

    /**
     * 房间列表
     *
     * @param pageNumber
     * @param pageSize
     * @param callBack
     */
    void listRoom(
            int pageNumber,
            int pageSize,
            QLiveCallBack<List<QLiveRoomInfo>> callBack
    );

    /**
     * 根据ID获取房间信息
     *
     * @param roomID 房间ID
     * @param callBack
     */
    void getRoomInfo(String roomID, QLiveCallBack<QLiveRoomInfo> callBack);
}
