package com.qlive.coreimpl.datesource

import com.alibaba.fastjson.util.ParameterizedTypeImpl
import com.qlive.coreimpl.http.OKHttpService
import com.qlive.jsonutil.JsonUtils
import com.qlive.core.been.QExtension
import com.qlive.core.been.QMicLinker
import com.qlive.coreimpl.model.TokenData

import org.json.JSONObject

class LinkDateSource {

    suspend fun getMicList(liveId: String): List<QMicLinker> {
        val p = ParameterizedTypeImpl(
            arrayOf(QMicLinker::class.java),
            List::class.java,
            List::class.java
        )
        val resp: List<QMicLinker> = OKHttpService.get(
            "/client/mic/room/list/${liveId}",
            null, null, p
        )
        return resp
    }

    suspend fun upMic(linker: QMicLinker): TokenData {
        return OKHttpService.post("/client/mic/", JsonUtils.toJson(linker), TokenData::class.java)
    }

    suspend fun downMic(linker: QMicLinker) {
        OKHttpService.delete("/client/mic/", JsonUtils.toJson(linker), Any::class.java)
    }

    suspend fun updateExt(linker: QMicLinker, extension: QExtension) {
        val jsonObj = JSONObject()
        jsonObj.put("live_id", linker.userRoomID)
        jsonObj.put("user_id", linker.user.userId)
        jsonObj.put("extends", extension)
        OKHttpService.put("/client/mic/room/", jsonObj.toString(), Any::class.java)
    }

    suspend fun switch(linker: QMicLinker, isMic: Boolean, isOpen: Boolean) {
        val jsonObj = JSONObject()
        jsonObj.put("live_id", linker.userRoomID)
        jsonObj.put("user_id", linker.user.userId)
        jsonObj.put(
            "type", if (isMic) {
                "mic"
            } else {
                "camera"
            }
        )
        jsonObj.put(
            "status", isOpen
        )
        OKHttpService.put("/client/mic/switch", jsonObj.toString(), Any::class.java)
    }
}