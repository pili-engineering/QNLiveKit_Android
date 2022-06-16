package com.qncube.linveroominner

import android.content.Context
import com.qncube.linveroominner.http.OKHttpService
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.liveroomcore.QPlayerClient
import com.qncube.liveroomcore.QPusherClient
import com.qncube.liveroomcore.QRooms
import com.qncube.liveroomcore.been.QLiveUser

object QLiveDelegate {

    interface QLiveSdk {
        fun createPusherClientCall(): QPusherClient //创建主播端
        fun createPlayerClientCall(): QPlayerClient //创建观众端
    }

    lateinit var qLiveSdk: QLiveSdk
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
}