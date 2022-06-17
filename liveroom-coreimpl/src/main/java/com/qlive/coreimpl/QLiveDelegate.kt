package com.qlive.coreimpl

import android.content.Context
import com.qlive.core.*
import com.qlive.coreimpl.http.OKHttpService
import com.qlive.core.been.QLiveUser
import com.qlive.coreimpl.datesource.UserDataSource

object QLiveDelegate {

    var qRooms: QRooms = QRoomImpl.instance;

    fun init(context: Context, tokenGetter: QTokenGetter, callBack: QLiveCallBack<Void>?) {
        AppCache.appContext = context
        OKHttpService.tokenGetter = tokenGetter
        UserDataSource().loginUser(context, object : QLiveCallBack<QLiveUser> {
            override fun onError(code: Int, msg: String?) {
                callBack?.onError(code, msg)
            }

            override fun onSuccess(data: QLiveUser) {
                callBack?.onSuccess(null)
            }
        })
    }

    private var uikitObj:Any?=null
    fun <T> getUIKIT(): T {
        if(uikitObj!=null){
            return uikitObj as T
        }
        val classStr = "com.qlive.uikit.QLiveUIKitImpl"
        val classImpl = Class.forName(classStr)
        val constructor = classImpl.getConstructor()
        val uikitObj = constructor.newInstance() as T
        return uikitObj
    }
}