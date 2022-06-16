package com.qlive.uikit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.view.LayoutInflaterCompat
import com.qlive.avparam.PreviewMode
import com.qlive.core.*
import com.qlive.uikit.hook.KITLiveInflaterFactory
import com.qlive.avparam.RtcException
import com.qlive.coreimpl.QLiveDelegate
import com.qlive.coreimpl.UserDataSource
import com.qlive.coreimpl.asToast
import com.qlive.core.been.QCreateRoomParam
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.uikit.hook.KITFunctionInflaterFactory
import com.qlive.roomservice.QRoomService
import com.qlive.uikitcore.QLiveKitUIContext
import com.qlive.uikitcore.activity.BaseFrameActivity
import com.qlive.uikitcore.ext.bg
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

    private val leftRoomActionCall: (resultCall: QLiveCallBack<Void>) -> Unit = {
        mRoomClient.leaveRoom(object : QLiveCallBack<Void> {
            override fun onError(code: Int, msg: String?) {
                it.onError(code, msg)
            }

            override fun onSuccess(data: Void?) {
                it.onSuccess(data)
                mInflaterFactory.onLeft()
                KITFunctionInflaterFactory.onLeft()
            }
        })
    }
    private val createAndJoinRoomActionCall: (param: QCreateRoomParam, resultCall: QLiveCallBack<Void>) -> Unit =
        { p, c ->
            "player activity can not create".asToast()
        }

    private val mQUIKitContext by lazy {
        QLiveKitUIContext(
            this@RoomPullActivity,
            supportFragmentManager,
            this@RoomPullActivity,
            this@RoomPullActivity,
            leftRoomActionCall,
            createAndJoinRoomActionCall,
            { playerRenderView },
            { null }
        )
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

    override fun init() {
        mRoomId = intent.getStringExtra("roomId") ?: ""

        KITFunctionInflaterFactory.attachKitContext(mQUIKitContext)
        KITFunctionInflaterFactory.attachLiveClient(mRoomClient)
        mInflaterFactory.onEntering(mRoomId, UserDataSource.loginUser)
        KITFunctionInflaterFactory.onEntering(mRoomId, UserDataSource.loginUser)

        container.post {
            bg {
                showLoading(true)
                doWork {
                    val room = suspendJoinRoom(mRoomId)
                    startCallBack?.onSuccess(room)
                    mRoomClient.play(playerRenderView)
                    mInflaterFactory.onJoined(room)
                    KITFunctionInflaterFactory.onJoined(room)
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
        mInflaterFactory.onDestroyed()
        KITFunctionInflaterFactory.onDestroyed()

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