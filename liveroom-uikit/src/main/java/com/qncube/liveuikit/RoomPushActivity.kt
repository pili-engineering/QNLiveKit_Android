package com.qncube.liveuikit

import android.Manifest
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
import com.nucube.rtclive.QNCameraParams
import com.nucube.rtclive.QNMicrophoneParams
import com.qbcube.pkservice.QNPKService
import com.qiniu.droid.rtc.QNConnectionState
import com.qncube.danmakuservice.QNDanmakuService
import com.qncube.linkmicservice.QNLinkMicService
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.liveroomcore.mode.QNLiveUser
import com.qncube.liveuikit.hook.KITLayoutInflaterFactory
import com.qncube.publicchatservice.QNPublicChatService
import com.qncube.pushclient.QNLivePushClient
import com.qncube.pushclient.QNPushClientListener
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.activity.BaseFrameActivity
import com.qncube.uikitcore.ext.permission.PermissionAnywhere
import com.qncube.uikitcore.view.CommonPagerAdapter
import com.qncube.uikitcore.view.EmptyFragment
import kotlinx.android.synthetic.main.activity_room_push.*

class RoomPushActivity : BaseFrameActivity() {

    private var roomId = ""

    companion object {
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
        QNLivePushClient.createLivePushClient().apply {
            registerService(
                com.qncube.chatservice.QClientService::class.java,
            )
            registerService(
                QNPKService::class.java,
            )
            registerService(
                QNLinkMicService::class.java,
            )
            registerService(
                QNDanmakuService::class.java,
            )
            registerService(
                QNPublicChatService::class.java,
            )
            registerService(
                com.qncube.roomservice.QClientService::class.java
            )
            setPushClientListener(object : QNPushClientListener {
                override fun onConnectionStateChanged(state: QNConnectionState?, msg: String?) {

                }

                override fun onRoomStatusChange(liveRoomStatus: QLiveStatus, msg: String?) {
                    QNLiveLogUtil.LogE("房间状态变更  ${liveRoomStatus}")
                }

                override fun onCameraStatusChange(isOpen: Boolean) {

                }

                override fun onMicrophoneStatusChange(isOpen: Boolean) {

                }
            })
        }
    }

    private val fragments by lazy {
        listOf(EmptyFragment(), CoverFragment().apply {
            mClient = mRoomClient
        })
    }

    private val mKitContext by lazy {
        object : KitContext {
            override var androidContext: Context = this@RoomPushActivity
            override var fm: FragmentManager = supportFragmentManager
            override var currentActivity: Activity = this@RoomPushActivity
            override var lifecycleOwner: LifecycleOwner = this@RoomPushActivity
        }
    }

    private val mQClientLifeCycleListener = object :
        QClientLifeCycleListener {
        override fun onEntering(roomId: String, user: QNLiveUser) {}

        override fun onJoined(roomInfo: QLiveRoomInfo) {
            startCallBack?.onSuccess(roomInfo)
            startCallBack = null
            prevContainer.visibility = View.GONE
            vpCover.visibility = View.VISIBLE
        }

        override fun onLeft() {}

        override fun onDestroyed() {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//设置透明导航栏
        }
        LayoutInflaterCompat.setFactory2(
            LayoutInflater.from(this),
            KITLayoutInflaterFactory(delegate, mRoomClient, mKitContext)
        )
        super.onCreate(savedInstanceState)
    }

    private fun start() {
        roomId = intent.getStringExtra("roomId") ?: ""

        mRoomClient.enableCamera(QNCameraParams())
        mRoomClient.enableMicrophone(QNMicrophoneParams())
        mRoomClient.localPreView = preTextureView
        vpCover.visibility = View.INVISIBLE
        vpCover.adapter = CommonPagerAdapter(fragments, supportFragmentManager)
        vpCover.currentItem = 1
        mRoomClient.addRoomLifeCycleListener(mQClientLifeCycleListener)

        if (roomId.isEmpty()) {
//            prevContainer.attach(
//                QNLiveRoomUIKit.mViewSlotTable.mLivePreViewSlot,
//                this, mKitContext, mRoomClient
//            )
        } else {
            mRoomClient!!.joinRoom(roomId, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mRoomClient.closeRoom()
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
            && mRoomClient.getService(com.qncube.roomservice.QClientService::class.java).currentRoomInfo != null
        ) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun isToolBarEnable(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_room_push
    }

}