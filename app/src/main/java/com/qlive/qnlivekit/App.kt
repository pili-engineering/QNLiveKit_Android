package com.qlive.qnlivekit

import android.app.Application
import com.qlive.core.QLiveCallBack
import com.qlive.core.QSdkConfig
import com.qlive.coreimpl.http.OKHttpService
import com.qlive.jsonutil.JsonUtils
import com.qlive.sdk.QLive

import okhttp3.Request

class App : Application() {
    companion object {
        val demo_url = "https://niucube-api.qiniu.com"
        //  val demo_url="http://10.200.20.28:5080"
        var user: com.qlive.qnlivekit.BZUser? = null
    }

    override fun onCreate() {
        super.onCreate()
        QLive.init(
            this, QSdkConfig()
        ) { callback ->
            //业务方获取token
            getLoginToken(callback)
        }
    }

    //demo获取token
    private fun getLoginToken(callBack: QLiveCallBack<String>) {
        Thread {
            try {
                val requestToken = Request.Builder()
                    .url("${demo_url}/v1/live/auth_token?userID=${user!!.data.accountId}&deviceID=adjajdasod")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + user!!.data.loginToken)
                    .get()
                    .build();
                val callToken = OKHttpService.okHttp.newCall(requestToken);
                val repToken = callToken.execute()
                val tkjson = repToken.body?.string()
                val tkobj = JsonUtils.parseObject(tkjson, com.qlive.qnlivekit.BZkIToken::class.java)

                callBack.onSuccess(tkobj?.data?.accessToken ?: "")

            } catch (e: Exception) {
                e.printStackTrace()
                callBack.onError(-1, "")
            }
        }.start()
    }
}