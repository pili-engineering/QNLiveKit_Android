package com.qncube.liveroomcore.datasource

import com.alibaba.fastjson.util.ParameterizedTypeImpl
import com.nucube.http.OKHttpService
import com.nucube.http.PageData
import com.qiniu.jsonutil.JsonUtils
import com.qncube.liveroomcore.Extension
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.backGround
import com.qncube.liveroomcore.getCode
import com.qncube.liveroomcore.innermodel.HearBeatResp
import com.qncube.liveroomcore.mode.QNCreateRoomParam
import com.qncube.liveroomcore.mode.QLiveRoomInfo
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

    suspend fun createRoom(param: QNCreateRoomParam): QLiveRoomInfo {
        return OKHttpService.post(
            "/client/live/room/instance",
            JsonUtils.toJson(param),
            QLiveRoomInfo::class.java
        )
    }

    fun createRoom(param: QNCreateRoomParam, callBack: QLiveCallBack<QLiveRoomInfo>?) {
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
    suspend fun updateRoomExtension(liveId: String, extension: Extension) {

        val json = JSONObject()
        json.put("live_id", liveId)
        json.put("extends", extension)

        OKHttpService.put(
            "/client/live/room/extends",
            json.toString(),
            Any::class.java
        )
    }

    suspend fun heartbeat(liveId: String) :HearBeatResp{
        return OKHttpService.get("/client/live/room/heartbeat/${liveId}", null, HearBeatResp::class.java)
    }
}