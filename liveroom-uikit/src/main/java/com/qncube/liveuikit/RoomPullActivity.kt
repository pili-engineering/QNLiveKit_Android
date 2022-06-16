package com.qncube.liveuikit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.view.LayoutInflaterCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.qncube.lcommon.PreviewMode
import com.qncube.liveroomcore.*
import com.qncube.liveuikit.hook.KITLiveInflaterFactory
import com.qncube.lcommon.RtcException
import com.qncube.linveroominner.QLiveDelegate
import com.qncube.linveroominner.asToast
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.roomservice.QRoomService
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.activity.BaseFrameActivity
import com.qncube.uikitcore.ext.bg
import kotlinx.android.synthetic.main.activity_room_pull.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RoomPullActivity : BaseFrameActivity() {

    companion object {
        var replaceLayoutId = -1
        var startCallBack: QLiveCallBack<QLiveRoomInfo>? = null
        fun start(context: Context, roomId: String, callBack: QLiveCallBack<QLiveRoomInfo>?) {
            startCallBack = callBack
            val i = Intent(context, RoomPullActivity::class.java)
            i.putExtra("roomId", roomId)
            context.startActivity(i)
        }
    }

    private var mRoomId = ""
    private val mRoomClient by lazy {
        QLiveDelegate.qLiveSdk.createPlayerClientCall()
    }

    private val mKitContext by lazy {
        object : KitContext {
            override var androidContext: Context = this@RoomPullActivity
            override var fm: FragmentManager = supportFragmentManager
            override var currentActivity: Activity = this@RoomPullActivity
            override var lifecycleOwner: LifecycleOwner = this@RoomPullActivity
            override var leftRoomActionCall: (resultCall: QLiveCallBack<Void>) -> Unit = {
                mRoomClient.leaveRoom(object : QLiveCallBack<Void> {
                    override fun onError(code: Int, msg: String?) {
                        it.onError(code, msg)
                    }

                    override fun onSuccess(data: Void?) {
                        it.onSuccess(data)
                    }
                })
            }
            override var createAndJoinRoomActionCall: (param: QCreateRoomParam, resultCall: QLiveCallBack<Void>) -> Unit =
                { p, c ->
                    "player activity can not create".asToast()
                }
        }
    }

    private suspend fun suspendJoinRoom(roomId: String) = suspendCoroutine<QLiveRoomInfo> { cont ->
        mRoomClient.joinRoom(roomId, object :
            QLiveCallBack<QLiveRoomInfo> {
            override fun onError(code: Int, msg: String) {
                cont.resumeWithException(RtcException(code, msg))
            }

            override fun onSuccess(data: QLiveRoomInfo) {
                cont.resume(data)
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//设置透明导航栏
        }
        LayoutInflaterCompat.setFactory2(
            LayoutInflater.from(this),
            KITLiveInflaterFactory(delegate, mRoomClient, mKitContext)
        )
        super.onCreate(savedInstanceState)
    }

    override fun init() {
        mRoomId = intent.getStringExtra("roomId") ?: ""
        container.post {
            bg {
                showLoading(true)
                doWork {
                    val room = suspendJoinRoom(mRoomId)
                    startCallBack?.onSuccess(room)
                    mRoomClient.play(playerRenderView)
                }
                catchError {
                    it.message?.asToast()
                    startCallBack?.onError(it.getCode(), it.message)
                    finish()
                }
                onFinally {
                    startCallBack = null
                    showLoading(false)
                }
            }
        }
        playerRenderView.setDisplayAspectRatio(PreviewMode.ASPECT_RATIO_PAVED_PARENT)
    }

    //安卓重写返回键事件
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK
            && mRoomClient.getService(QRoomService::class.java).roomInfo != null
        ) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        mRoomClient.destroy()
        startCallBack?.onError(-1, "")
        startCallBack = null
    }

    override fun isToolBarEnable(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        if (replaceLayoutId > 0) {
            return replaceLayoutId
        }
        return R.layout.activity_room_pull
    }

}