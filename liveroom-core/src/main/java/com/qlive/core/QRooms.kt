package com.qlive.core

import com.qlive.core.been.QCreateRoomParam
import com.qlive.core.been.QLiveRoomInfo

/**
 * 房间管理接口
 * @constructor Create empty Q rooms
 */
interface QRooms {

    /**
     * 创建房间
     *
     * @param param 创建房间参数
     * @param callBack
     */
    fun createRoom(param: QCreateRoomParam, callBack: QLiveCallBack<QLiveRoomInfo>?) //创建房间

    /**
     * 删除房间
     *
     * @param roomID
     * @param callBack
     */
    fun deleteRoom(roomID: String, callBack: QLiveCallBack<Void>?) //删除房间

    /**
     * 房间列表
     *
     * @param pageNumber
     * @param pageSize
     * @param callBack
     */
    fun listRoom(
        pageNumber: Int,
        pageSize: Int,
        callBack: QLiveCallBack<List<QLiveRoomInfo>>?
    ) //房间列表

    /**
     * 根据ID获取房间信息
     *
     * @param roomID 房间ID
     * @param callBack
     */
    fun getRoomInfo(roomID: String, callBack: QLiveCallBack<QLiveRoomInfo>?) //根据ID搜索房间信息

}