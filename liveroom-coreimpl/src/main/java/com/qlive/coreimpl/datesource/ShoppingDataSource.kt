package com.qlive.coreimpl.datesource

import com.alibaba.fastjson.util.ParameterizedTypeImpl
import com.qlive.core.QLiveCallBack
import com.qlive.core.been.QExtension
import com.qlive.core.been.QItem
import com.qlive.coreimpl.http.HttpResp
import com.qlive.coreimpl.http.OKHttpService
import com.qlive.jsonutil.JsonUtils
import org.json.JSONArray
import org.json.JSONObject

class ShoppingDataSource {

    suspend fun addItems(liveId: String, items: List<QItem>) {
        val jsonObj = JSONObject()
        jsonObj.put("live_id", liveId)
        val jsonArray = JSONArray()
        items.forEach {
            jsonArray.put(it)
        }
        jsonObj.put("items", jsonArray)
        OKHttpService.post("/client/item/add", jsonObj.toString(), Any::class.java)
    }

    suspend fun deleteItems(liveId: String, items: List<String>) {
        val jsonObj = JSONObject()
        jsonObj.put("live_id", liveId)
        jsonObj.put("items", items)
        OKHttpService.post("/client/item/delete", jsonObj.toString(), Any::class.java)
    }

    suspend fun updateStatus(liveId: String, items: HashMap<String, Int>) {
        val jsonObj = JSONObject()
        jsonObj.put("live_id", liveId)
        val jsonArray = JSONArray()
        items.forEach {
            val jsonObjItem = JSONObject()
            jsonObjItem.put("item_id", it.key)
            jsonObjItem.put("status", it.value)
            jsonArray.put(jsonObjItem)
        }
        jsonObj.put("items", jsonArray)
        OKHttpService.post("/client/item/status", jsonObj.toString(), Any::class.java)
    }

    //获取直播间所有商品
    suspend fun getItemList(liveId: String): List<QItem> {
        val p = ParameterizedTypeImpl(
            arrayOf(QItem::class.java),
            List::class.java,
            List::class.java
        )
        return OKHttpService.post("/client/items/$liveId", "{}", null, p)
    }

    suspend fun updateItemExtension(liveId:String,ItemID: String, extension: QExtension) {
         OKHttpService.post("/client/item/${liveId}/${ItemID}/extends", JsonUtils.toJson(extension), Any::class.java)
    }

    fun setExplaining(liveId:String,ItemID: String){

    }

//    //取消设置讲解中的商品 并通知房间所有人
//    fun cancelExplaining(callBack: QLiveCallBack<Void?>?)
//
//
//
//    //跟新单个商品顺序
//    fun changeSingleOrder(param: QSingleOrderParam?, callBack: QLiveCallBack<Void?>?)
//
//    fun changeOrder(params: List<QOrderParam?>?, callBack: QLiveCallBack<Void?>?)
//

}