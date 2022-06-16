package com.qlive.core

import com.qlive.core.been.QCreateRoomParam
import com.qlive.core.been.QLiveRoomInfo




interface QRooms {

    fun createRoom(param: QCreateRoomParam, callBack: QLiveCallBack<QLiveRoomInfo>?) //创建房间

    fun deleteRoom(roomID: String, callBack: QLiveCallBack<Void>?) //删除房间

    fun listRoom(
        pageNumber: Int,
        pageSize: Int,
        callBack: QLiveCallBack<List<QLiveRoomInfo>>?
    ) //房间列表

    fun getRoomInfo(roomID: String, callBack: QLiveCallBack<QLiveRoomInfo>?) //根据ID搜索房间信息

}