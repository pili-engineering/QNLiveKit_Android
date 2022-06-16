package com.qncube.linveroominner

import com.qncube.linveroominner.http.PageData
import com.qncube.liveroomcore.QCreateRoomParam
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.QRooms
import com.qncube.liveroomcore.been.QLiveRoomInfo

class QRoomImpl private constructor() : QRooms {

    companion object{
        val instance = QRoomImpl()
    }
    override fun createRoom(param: QCreateRoomParam, callBack: QLiveCallBack<QLiveRoomInfo>?) {
        RoomDataSource().createRoom(param, callBack)
    }

    override fun deleteRoom(roomID: String, callBack: QLiveCallBack<Void>?) {
        RoomDataSource().deleteRoom(roomID,callBack)
    }

    override fun listRoom(
        pageNumber: Int,
        pageSize: Int,
        callBack: QLiveCallBack<List<QLiveRoomInfo>>?
    ) {
        RoomDataSource().listRoom(pageNumber, pageSize, object :  QLiveCallBack<PageData<QLiveRoomInfo>> {
            override fun onError(code: Int, msg: String?) {
                callBack?.onError(code, msg)
            }

            override fun onSuccess(data: PageData<QLiveRoomInfo>?) {
               callBack?.onSuccess(data?.list?:ArrayList<QLiveRoomInfo>())
            }

        })
    }

    override fun getRoomInfo(roomID: String, callBack: QLiveCallBack<QLiveRoomInfo>?) {
        RoomDataSource().refreshRoomInfo(roomID,callBack)
    }


}