package com.qlive.uikit

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.LayoutInflaterCompat
import com.qlive.avparam.PreviewMode
import com.qlive.core.*
import com.qlive.uikit.hook.KITLiveInflaterFactory
import com.qlive.avparam.RtcException
import com.qlive.core.been.QCreateRoomParam
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.uikit.hook.KITFunctionInflaterFactory
import com.qlive.roomservice.QRoomService
import com.qlive.uikitcore.QLiveUIKitContext
import com.qlive.uikitcore.activity.BaseFrameActivity
import com.qlive.uikitcore.ext.bg
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import com.qlive.sdk.QLive
import com.qlive.uikitcore.getCode
import kotlinx.android.synthetic.main.kit_activity_room_player.*

/**
 * 观众activity
 * UI插件底座 请勿在activity 做过多UI逻辑代码
 */
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

    //拉流客户端
    private val mRoomClient by lazy {
        QLive.createPlayerClient()
    }

    //离开房间函数
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

    //创建并且加入房间函数
    private val createAndJoinRoomActionCall: (param: QCreateRoomParam, resultCall: QLiveCallBack<Void>) -> Unit =
        { p, c ->
            Toast.makeText(this, "player activity can not create", Toast.LENGTH_SHORT).show()
        }

    //UI组件上下文
    private val mQUIKitContext by lazy {
        QLiveUIKitContext(
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

    //加入房间
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

    //UI组件装载器
    /**
     * UI组件以插件的形式加载进来
     * 装载器完成替换删除 功能分发操作
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

    override fun init() {
        mRoomId = intent.getStringExtra("roomId") ?: ""

        KITFunctionInflaterFactory.attachKitContext(mQUIKitContext)
        KITFunctionInflaterFactory.attachLiveClient(mRoomClient)
        mInflaterFactory.onEntering(mRoomId, QLive.getLoginUser())
        KITFunctionInflaterFactory.onEntering(mRoomId, QLive.getLoginUser())

        container.post {
            bg {
                showLoading(true)
                doWork {
                    //加入房间
                    val room = suspendJoinRoom(mRoomId)
                    startCallBack?.onSuccess(room)
                    //开始播放
                    mRoomClient.play(playerRenderView)
                    //分发状态到各个UI组件
                    mInflaterFactory.onJoined(room)
                    KITFunctionInflaterFactory.onJoined(room)
                }
                catchError {
                    Toast.makeText(this@RoomPullActivity, it.message, Toast.LENGTH_SHORT).show()
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

    override fun getLayoutId(): Int {
        if (replaceLayoutId > 0) {
            return replaceLayoutId
        }
        return R.layout.kit_activity_room_player
    }

    override fun onResume() {
        super.onResume()
        mRoomClient.resume()
    }

    override fun onPause() {
        mRoomClient.pause()
        super.onPause()
    }

}