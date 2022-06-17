package com.qlive.qnlivekit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.qlive.jsonutil.JsonUtils
import com.qlive.sdk.QLive
import com.qlive.sdk.QUserInfo
import com.qlive.coreimpl.http.OKHttpService
import com.qlive.core.QLiveCallBack
import com.qlive.uikit.RoomPage
import com.qlive.uikitcore.dialog.LoadingDialog
import com.qlive.uikitcore.ext.bg
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.FormBody
import okhttp3.Request
import okio.Buffer
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine





class MainActivity : AppCompatActivity() {

    private var user: com.qlive.qnlivekit.BZUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loginBtn.setOnClickListener {

            bg {
                LoadingDialog.showLoading(supportFragmentManager)
                doWork {
                    Log.d("livekit", "ss")
                    suspendInit()
                    suspendSetUser()
                    QLive.getLiveUIKit().launch(this@MainActivity)
                }
                catchError {
                    Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                }
                onFinally {
                    LoadingDialog.cancelLoadingDialog()
                }
            }
        }
    }

    suspend fun suspendInit() =
        suspendCoroutine<Unit> { coroutine ->
            QLive.init(application,
                { callback ->
                    getLoginToken(callback)
                }, object : QLiveCallBack<Void> {
                    override fun onError(code: Int, msg: String?) {
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                        coroutine.resumeWithException(Exception("getTokenError"))
                    }

                    override fun onSuccess(data: Void?) {
                        coroutine.resume(Unit)
                    }
                })
        }

    suspend fun suspendSetUser() =
        suspendCoroutine<Unit> { coroutine ->
            QLive.setUser(QUserInfo().apply {
                avatar = user!!.data.avatar
                nick = user!!.data.nickname
                extension = HashMap<String, String>().apply {
                    put("phone", user!!.data.phone)
                    put("customFiled", "i am customFile")
                }
            }, object : QLiveCallBack<Void> {
                override fun onError(code: Int, msg: String?) {
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    coroutine.resumeWithException(Exception("getTokenError"))
                }

                override fun onSuccess(data: Void?) {
                    coroutine.resume(Unit)
                }
            })
        }

    private fun getLoginToken(callBack: QLiveCallBack<String>) {
        Thread {
            try {
                val body = FormBody.Builder()
                    .add("phone", "13141616035")
                    .add("smsCode", "8888")
                    .build()
                val buffer = Buffer()
                body.writeTo(buffer)

                val request = Request.Builder()
                    .url("http://10.200.20.28:5080/v1/signUpOrIn")
                    //  .addHeader(headerKey, headerValue)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .post(body)
                    .build();
                val call = OKHttpService.okHttp.newCall(request);
                val resp = call.execute()

                val code = resp.code
                val userJson = resp.body?.string()
                user = JsonUtils.parseObject(userJson, com.qlive.qnlivekit.BZUser::class.java)


                val requestToken = Request.Builder()
                    .url("http://10.200.20.28:5080/v1/live/auth_token?userID=${user!!.data.accountId}&deviceID=adjajdasod")
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