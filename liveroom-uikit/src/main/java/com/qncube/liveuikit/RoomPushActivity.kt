package com.qncube.liveuikit

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.view.LayoutInflaterCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.qncube.lcommon.QCameraParam
import com.qncube.lcommon.QMicrophoneParam
import com.qncube.lcommon.RtcException
import com.qncube.linveroominner.QLiveDelegate
import com.qncube.linveroominner.UserDataSource
import com.qncube.linveroominner.asToast
import com.qncube.liveroomcore.*
import com.qncube.liveuikit.hook.KITLiveInflaterFactory
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveuikit.hook.KITFunctionInflaterFactory
import com.qncube.roomservice.QRoomService
import com.qncube.uikitcore.QLiveKitUIContext
import com.qncube.uikitcore.QUIKitContext
import com.qncube.uikitcore.activity.BaseFrameActivity
import com.qncube.uikitcore.dialog.LoadingDialog
import com.qncube.uikitcore.ext.bg
import com.qncube.uikitcore.ext.permission.PermissionAnywhere
import kotlinx.android.synthetic.main.activity_room_push.*
import java.util.function.Function
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class RoomPushActivity : BaseFrameActivity() {
    private var roomId = ""

    companion object {
        var replaceLayoutId = -1
        private var startCallBack: QLiveCallBack<QLiveRoomInfo>? = null

        fun start(
            context: Context,
            callBack: QLiveCallBack<QLiveRoomInfo>?
        ) {
            startCallBack = callBack
            val i = Intent(context, RoomPushActivity::class.java)
            context.startActivity(i)
        }

        fun start(
            context: Context,
            roomId: String,
            callBack: QLiveCallBack<QLiveRoomInfo>?
        ) {
            startCallBack = callBack
            val i = Intent(context, RoomPushActivity::class.java)
            i.putExtra("roomId", roomId)
            context.startActivity(i)
        }
    }

    private val mRoomClient by lazy {
        QLiveDelegate.qLiveSdk.createPusherClientCall()
    }
    private val mQUIKitContext by lazy {
        QLiveKitUIContext(
            this@RoomPushActivity,
            supportFragmentManager,
            this@RoomPushActivity,
            this@RoomPushActivity,
            leftRoomActionCall,
            createAndJoinRoomActionCall,
            { null }, { preTextureView }
        )
    }

    private val leftRoomActionCall: (resultCall: QLiveCallBack<Void>) -> Unit = {
        mRoomClient.closeRoom(object : QLiveCallBack<Void> {
            override fun onError(code: Int, msg: String?) {
                it.onError(code, msg)
            }
            override fun onSuccess(data: Void?) {
                mInflaterFactory.onLeft()
                KITFunctionInflaterFactory.onLeft()
                it.onSuccess(data)
            }
        })
    }

    private val createAndJoinRoomActionCall: (param: QCreateRoomParam, resultCall: QLiveCallBack<Void>) -> Unit =
        { p, c ->
            bg {
                LoadingDialog.showLoading(supportFragmentManager)
                doWork {
                    val room = createSuspend(p)
                    suspendJoinRoom(room.liveId)
                    startCallBack?.onSuccess(null)
                    startCallBack = null
                    c.onSuccess(null)
                }
                catchError {
                    it.message?.asToast()
                    c.onError(it.getCode(), it.message)
                }
                onFinally {
                    LoadingDialog.cancelLoadingDialog()
                }
            }

        }

    private suspend fun createSuspend(p: QCreateRoomParam) = suspendCoroutine<QLiveRoomInfo> { ct ->
        QLiveDelegate.qRooms.createRoom(p, object :
            QLiveCallBack<QLiveRoomInfo> {
            override fun onError(code: Int, msg: String) {
                ct.resumeWithException(RtcException(code, msg))
            }

            override fun onSuccess(data: QLiveRoomInfo) {
                ct.resume(data)
            }
        })
    }

    private suspend fun suspendJoinRoom(roomId: String) = suspendCoroutine<QLiveRoomInfo> { cont ->
        mInflaterFactory.onEntering(roomId, UserDataSource.loginUser)
        KITFunctionInflaterFactory.onEntering(roomId, UserDataSource.loginUser)
        mRoomClient.joinRoom(roomId, object :
            QLiveCallBack<QLiveRoomInfo> {
            override fun onError(code: Int, msg: String?) {
                cont.resumeWithException(RtcException(code, msg ?: ""))
            }

            override fun onSuccess(data: QLiveRoomInfo) {
                cont.resume(data)
                mInflaterFactory.onJoined(data)
                KITFunctionInflaterFactory.onJoined(data)
            }
        })
    }

    private val mInflaterFactory by lazy {
        KITLiveInflaterFactory(
            delegate,
            mRoomClient,
            mQUIKitContext
        )
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置透明状态栏
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//设置透明导航栏
        LayoutInflaterCompat.setFactory2(
            LayoutInflater.from(this),
            mInflaterFactory
        )
        super.onCreate(savedInstanceState)
    }

    private fun start() {
        roomId = intent.getStringExtra("roomId") ?: ""
        mRoomClient.enableCamera(QCameraParam(), preTextureView)
        mRoomClient.enableMicrophone(QMicrophoneParam())
        KITFunctionInflaterFactory.attachKitContext(mQUIKitContext)
        KITFunctionInflaterFactory.attachLiveClient(mRoomClient)
        if (roomId.isNotEmpty()) {
            mLivePreView?.visibility = View.GONE
            bg {
                LoadingDialog.showLoading(supportFragmentManager)
                doWork {
                    suspendJoinRoom(roomId)
                    startCallBack?.onSuccess(null)
                    startCallBack = null
                }
                catchError {
                    startCallBack?.onError(it.getCode(), "")
                    startCallBack = null
                    it.message?.asToast()
                    finish()
                }
                onFinally {
                    LoadingDialog.cancelLoadingDialog()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mInflaterFactory.onDestroyed()
        KITFunctionInflaterFactory.onDestroyed()
        mRoomClient.destroy()
        startCallBack?.onError(-1, "")
        startCallBack = null
    }

    override fun init() {
        PermissionAnywhere.requestPermission(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        ) { grantedPermissions, _, _ ->
            if (grantedPermissions.size == 2) {
                start()
            } else {
                "请同意必要的权限".asToast()
                startCallBack?.onError(-1, "no permission")
                startCallBack = null
                finish()
            }
        }
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

    override fun isToolBarEnable(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        if (replaceLayoutId > 0) {
            return replaceLayoutId
        }
        return R.layout.activity_room_push
    }
}