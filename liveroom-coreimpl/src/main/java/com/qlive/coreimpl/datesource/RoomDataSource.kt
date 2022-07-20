package com.qlive.coreimpl.datesource

import com.alibaba.fastjson.util.ParameterizedTypeImpl
import com.qlive.coreimpl.http.OKHttpService
import com.qlive.coreimpl.http.PageData
import com.qlive.jsonutil.JsonUtils
import com.qlive.core.*
import com.qlive.core.been.QCreateRoomParam
import com.qlive.core.been.QExtension
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.coreimpl.model.HearBeatResp
import com.qlive.coreimpl.util.backGround
import com.qlive.coreimpl.util.getCode
import org.json.JSONObject

class RoomDataSource {

    /**
     * 刷新房间信息
     */
    suspend fun refreshRoomInfo(liveId: String): QLiveRoomInfo {
        return OKHttpService.get(
            "/client/live/room/info/${liveId}",
            null,
            QLiveRoomInfo::class.java
        )
    }

    fun refreshRoomInfo(liveId: String, callBack: QLiveCallBack<QLiveRoomInfo>?) {
        backGround {
            doWork {
                val resp = refreshRoomInfo(liveId)
                callBack?.onSuccess(resp)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    suspend fun listRoom(pageNumber: Int, pageSize: Int): PageData<QLiveRoomInfo> {
        val p = ParameterizedTypeImpl(
            arrayOf(QLiveRoomInfo::class.java),
            PageData::class.java,
            PageData::class.java
        )
        val date: PageData<QLiveRoomInfo> =
            OKHttpService.get("/client/live/room/list", HashMap<String, String>().apply {
                put("page_num", pageNumber.toString())
                put("page_size", pageSize.toString())
            }, null, p)
        return date
    }

    fun listRoom(pageNumber: Int, pageSize: Int, call: QLiveCallBack<PageData<QLiveRoomInfo>>?) {
        backGround {
            doWork {

                val resp = listRoom(pageNumber, pageSize)
                call?.onSuccess(resp)
            }
            catchError {
                call?.onError(it.getCode(), it.message)
            }
        }
    }

    suspend fun createRoom(param: QCreateRoomParam): QLiveRoomInfo {
        return OKHttpService.post(
            "/client/live/room/instance",
            JsonUtils.toJson(param),
            QLiveRoomInfo::class.java
        )
    }

    fun createRoom(param: QCreateRoomParam, callBack: QLiveCallBack<QLiveRoomInfo>?) {
        backGround {
            doWork {
                val room = createRoom(param)
                callBack?.onSuccess(room)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    fun deleteRoom(liveId: String, call: QLiveCallBack<Void>?) {
        backGround {
            doWork {
                OKHttpService.delete("/live/room/instance/${liveId}", "{}", Any::class.java)
                call?.onSuccess(null)
            }
            catchError {
                call?.onError(it.getCode(), it.message)
            }
        }
    }

    suspend fun pubRoom(liveId: String): QLiveRoomInfo {
        return OKHttpService.put("/client/live/room/${liveId}", "{}", QLiveRoomInfo::class.java)
    }
    suspend fun unPubRoom(liveId: String): QLiveRoomInfo {
        return OKHttpService.delete("/client/live/room/${liveId}", "{}", QLiveRoomInfo::class.java)
    }

    suspend fun joinRoom(liveId: String): QLiveRoomInfo {
        return OKHttpService.post(
            "/client/live/room/user/${liveId}",
            "{}",
            QLiveRoomInfo::class.java
        )
    }

    suspend fun leaveRoom(liveId: String) {
        OKHttpService.delete("/client/live/room/user/${liveId}", "{}", Any::class.java)
    }

    /**
     * 跟新直播扩展信息
     * @param extension
     */
    suspend fun updateRoomExtension(liveId: String, extension: QExtension) {

        val json = JSONObject()
        json.put("live_id", liveId)
        json.put("extends", extension)

        OKHttpService.put(
            "/client/live/room/extends",
            json.toString(),
            Any::class.java
        )
    }

    suspend fun heartbeat(liveId: String) : HearBeatResp {
        return OKHttpService.get("/client/live/room/heartbeat/${liveId}", null, HearBeatResp::class.java)
    }
}