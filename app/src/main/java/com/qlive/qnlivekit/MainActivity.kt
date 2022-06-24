package com.qlive.qnlivekit

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.qlive.jsonutil.JsonUtils
import com.qlive.sdk.QLive
import com.qlive.sdk.QUserInfo
import com.qlive.coreimpl.http.OKHttpService
import com.qlive.core.QLiveCallBack
import com.qlive.core.QSdkConfig
import com.qlive.uikit.QLiveUIKitImpl
import com.qlive.uikit.RoomListPage
import com.qlive.uikit.RoomPage
import com.qlive.uikitcore.dialog.LoadingDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.Request
import okio.Buffer
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {

    private var user: com.qlive.qnlivekit.BZUser? = null

     val demo_url = "https://niucube-api.qiniu.com"
    //  val demo_url="http://10.200.20.28:5080"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //登陆按钮
        bt_login_login.setOnClickListener {

            val phone = et_login_phone.text.toString() ?: ""
            val code = et_login_verification_code.text.toString() ?: ""
            if (phone.isEmpty()) {
                Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (code.isEmpty()) {
                Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!cbAgreement.isSelected) {
                Toast.makeText(this, "请同意 七牛云服务用户协议 和 隐私权政策", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                LoadingDialog.showLoading(supportFragmentManager)
                try {
                    //demo登陆
                    login(phone, code)

                    //初始化sdk
                    suspendInit()
                    //绑定用户信息
                    suspendSetUser()
                    //启动跳转到直播列表
                    QLive.getLiveUIKit().launch(this@MainActivity)

                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    LoadingDialog.cancelLoadingDialog()
                }
            }
        }
        initOtherView()
    }


    /**
     * 初始化sdk
     */
    suspend fun suspendInit() =
        suspendCoroutine<Unit> { coroutine ->
            QLive.init(application, QSdkConfig(),
                { callback ->
                    //业务方获取token
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

    /**
     *  //绑定用户信息 绑定后房间在线用户能返回绑定设置的字段
     */
    suspend fun suspendSetUser() =
        suspendCoroutine<Unit> { coroutine ->
            //绑定用户信息 绑定后房间在线用户能返回绑定设置的字段
            QLive.setUser(QUserInfo().apply {
                avatar = user!!.data.avatar //设置当前用户头像
                nick = user!!.data.nickname //设置当前用户昵称
                extension = HashMap<String, String>().apply {
                    put("phone", "13141616037")
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

    //demo自己的登陆
    suspend fun login(phoneNumber: String, smsCode: String) = suspendCoroutine<Unit> { ct ->
        Thread {
            try {
                val body = FormBody.Builder()
                    .add("phone", phoneNumber)
                    .add("smsCode", smsCode)
                    .build()
                val buffer = Buffer()
                body.writeTo(buffer)

                val request = Request.Builder()
                    .url("${demo_url}/v1/signUpOrIn")
                    //  .addHeader(headerKey, headerValue)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .post(body)
                    .build();
                val call = OKHttpService.okHttp.newCall(request);
                val resp = call.execute()

                val code = resp.code
                val userJson = resp.body?.string()
                user = JsonUtils.parseObject(userJson, com.qlive.qnlivekit.BZUser::class.java)
                ct.resume(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                ct.resumeWithException(Exception(e.message))
            }
        }.start()
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


    private fun timeJob() {
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                tvSmsTime.isClickable = false
                repeat(60) {
                    tvSmsTime.text = (60 - it).toString()
                    delay(1000)
                }
                tvSmsTime.text = "获取验证码"
                tvSmsTime.isClickable = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initOtherView() {
        tvSmsTime.setOnClickListener {
            val phone = et_login_phone.text.toString() ?: ""
            if (phone.isEmpty()) {
                return@setOnClickListener
            }
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val body = FormBody.Builder()
                        .add("phone", phone)
                        .build()
                    val buffer = Buffer()
                    body.writeTo(buffer)
                    val request = Request.Builder()
                        .url("${demo_url}/v1/getSmsCode")
                        //  .addHeader(headerKey, headerValue)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .post(body)
                        .build();
                    val call = OKHttpService.okHttp.newCall(request);
                    val resp = call.execute()
                    timeJob()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


        cbAgreement.setOnClickListener {
            cbAgreement.isSelected = !cbAgreement.isSelected
        }
        val tips = "我已阅读并同意 七牛云服务用户协议 和 隐私权政策"
        val spannableString = SpannableString(tips)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                WebActivity.start("https://www.qiniu.com/privacy-right", this@MainActivity)
            }
        }, tips.length - 5, tips.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                WebActivity.start("https://www.qiniu.com/user-agreement", this@MainActivity)
            }
        }, tips.length - 18, tips.length - 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            tips.length - 5,
            tips.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            tips.length - 18,
            tips.length - 7,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#007AFF")),
            tips.length - 5,
            tips.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#007AFF")),
            tips.length - 18,
            tips.length - 7,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        cbAgreement.setMovementMethod(LinkMovementMethod.getInstance());//设置可点击状态
        cbAgreement.text = spannableString
    }
}