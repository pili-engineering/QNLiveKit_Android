package com.qlive.uikit

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.core.view.LayoutInflaterCompat
import com.qlive.avparam.QCameraParam
import com.qlive.avparam.QMicrophoneParam
import com.qlive.core.*
import com.qlive.core.been.QCreateRoomParam
import com.qlive.uikit.hook.KITLiveInflaterFactory
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.uikit.hook.KITFunctionInflaterFactory
import com.qlive.roomservice.QRoomService
import com.qlive.sdk.QLive
import com.qlive.uikitcore.KitException
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.activity.BaseFrameActivity
import com.qlive.uikitcore.dialog.LoadingDialog
import com.qlive.uikitcore.ext.bg
import com.qlive.uikitcore.ext.permission.PermissionAnywhere
import kotlin.coroutines.resume
import com.qlive.uikitcore.getCode
import kotlinx.android.synthetic.main.kit_activity_room_pusher.*
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 主播房间activity
 */
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

    /**
     * 主播客户端
     */
    private val mRoomClient by lazy {
        QLive.createPusherClient()
    }

    /**
     * 主播UI上下文
     */
    private val mQUIKitContext by lazy {
        QLiveUIKitContext(
            this@RoomPushActivity,
            supportFragmentManager,
            this@RoomPushActivity,
            this@RoomPushActivity,
            leftRoomActionCall,
            createAndJoinRoomActionCall,
            { null }, { preTextureView }
        )
    }

    //离开房间函数
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

    //创建并且加入函数
    private val createAndJoinRoomActionCall: (param: QCreateRoomParam, resultCall: QLiveCallBack<Void>) -> Unit =
        { p, c ->
            bg {
                LoadingDialog.showLoading(supportFragmentManager)
                doWork {
                    val room = createSuspend(p)
                    suspendJoinRoom(room.liveID)
                    startCallBack?.onSuccess(null)
                    startCallBack = null
                    c.onSuccess(null)
                }
                catchError {
                    Toast.makeText(this@RoomPushActivity, it.message, Toast.LENGTH_SHORT).show()
                    c.onError(it.getCode(), it.message)
                }
                onFinally {
                    LoadingDialog.cancelLoadingDialog()
                }
            }

        }

    //创建房间
    private suspend fun createSuspend(p: QCreateRoomParam) = suspendCoroutine<QLiveRoomInfo> { ct ->
        QLive.getRooms().createRoom(p, object :
            QLiveCallBack<QLiveRoomInfo> {
            override fun onError(code: Int, msg: String) {
                ct.resumeWithException(KitException(code, msg))
            }

            override fun onSuccess(data: QLiveRoomInfo) {
                ct.resume(data)
            }
        })
    }

    //加入房间
    private suspend fun suspendJoinRoom(roomId: String) = suspendCoroutine<QLiveRoomInfo> { cont ->
        mInflaterFactory.onEntering(roomId, QLive.getLoginUser())
        KITFunctionInflaterFactory.onEntering(roomId, QLive.getLoginUser())
        mRoomClient.joinRoom(roomId, object :
            QLiveCallBack<QLiveRoomInfo> {
            override fun onError(code: Int, msg: String?) {
                cont.resumeWithException(KitException(code, msg ?: ""))
            }

            override fun onSuccess(data: QLiveRoomInfo) {
                cont.resume(data)
                mInflaterFactory.onJoined(data)
                KITFunctionInflaterFactory.onJoined(data)
            }
        })
    }

    /**
     * UI组件装载器
     */
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
        //房间ID不为空代表直接加入已经创建过的房间
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
                    Toast.makeText(this@RoomPushActivity, it.message, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this, "请同意必要的权限", Toast.LENGTH_SHORT).show()
                startCallBack?.onError(-1, "no permission")
                startCallBack = null
                finish()
            }
        }
    }

    //安卓重写返回键事件
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK
            && mRoomClient.getService(QRoomService::class.java)?.roomInfo != null
        ) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun getLayoutId(): Int {
        if (replaceLayoutId > 0) {
            return replaceLayoutId
        }
        return R.layout.kit_activity_room_pusher
    }

    override fun onPause() {
        super.onPause()
        mRoomClient.pause()
    }

    override fun onResume() {
        super.onResume()
        mRoomClient.resume()
    }
}