package com.qiniu.qnlivekit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.qiniu.jsonutil.JsonUtils
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.uikitcore.dialog.LoadingDialog
import com.qncube.uikitcore.ext.bg
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.FormBody
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginBtn.setOnClickListener {
            bg {
                LoadingDialog.showLoading(supportFragmentManager)

                doWork {
                    Log.d("livekit", "ss")
                    val resp = postFormUserExtraClient()
                    val code = resp.code
                    val userJson = resp.body?.string()
                    val user = JsonUtils.parseObject(userJson, BZUser::class.java)
                    val token = postJsonUserExtraClient(
                        user!!.data.accountId,
                        "asdasdas",
                        user!!.data!!.loginToken
                    )
                    suspendInit(application, token.accessToken)
                    suspendUpdateUserInfo(user.data.avatar, user.data.nickname, null)

                    RoomListActivity.start(this@MainActivity)

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

    suspend fun suspendInit(context: Context, token: String) =
        suspendCoroutine<Unit> { ct ->
            QNLiveRoomEngine.init(context, token, object :
                QLiveCallBack<Void> {
                override fun onError(code: Int, msg: String?) {
                    ct.resumeWithException(Exception(msg ?: ""))
                }

                override fun onSuccess(data: Void?) {
                    ct.resume(Unit)
                }
            })
        }

    suspend fun suspendUpdateUserInfo(
        avatar: String,
        nickName: String,
        extensions: HashMap<String, String>?
    ) = suspendCoroutine<QNLiveUser>
    { ct ->

        QNLiveRoomEngine.updateUserInfo(
            avatar,
            nickName,
            extensions,
            object : QLiveCallBack<QNLiveUser> {
                override fun onError(code: Int, msg: String?) {
                    ct.resumeWithException(Exception(msg ?: ""))
                }

                override fun onSuccess(data: QNLiveUser) {
                    ct.resume(data)
                }
            })
    }

    suspend fun postFormUserExtraClient() = suspendCoroutine<Response> { ct ->
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
                val rep = call.execute()

                val kitTokenJson =
                    ct.resume(rep)
            } catch (e: Exception) {
                ct.resumeWithException(e)
            }
        }.start()

    }

    suspend fun postJsonUserExtraClient(u: String, d: String, token: String) =
        suspendCoroutine<BZkIToken.TokenDao> { ct ->
            Thread {
                try {
                    val request = Request.Builder()
                        .url("http://10.200.20.28:5080/v1/live/auth_token?userID=$u&deviceID=$d")
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer " + token)
                        .get()
                        .build();
                    val call = OKHttpService.okHttp.newCall(request);
                    val rep = call.execute()
                    val tkjson = rep.body?.string()
                    val tkobj = JsonUtils.parseObject(tkjson, BZkIToken::class.java)
                    ct.resume(tkobj!!.data)
                } catch (e: Exception) {
                    ct.resumeWithException(e)
                }
            }.start()
        }

}