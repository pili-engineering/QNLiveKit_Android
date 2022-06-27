package com.qlive.uikit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.LayoutInflaterCompat
import com.qlive.uikit.hook.KITInflaterFactory
import com.qlive.uikitcore.QUIKitContext
import com.qlive.uikitcore.activity.BaseFrameActivity

/**
 * 房间列表activity
 */
class RoomListActivity : BaseFrameActivity() {

    companion object {
        var replaceLayoutID = -1
        fun start(context: Context) {
            context.startActivity(Intent(context, RoomListActivity::class.java))
        }
    }

    private val mQUIKitContext by lazy {
        QUIKitContext(
            this@RoomListActivity,
            supportFragmentManager,
            this@RoomListActivity,
            this@RoomListActivity
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(
            LayoutInflater.from(this),
            KITInflaterFactory(delegate, mQUIKitContext)
        )
        super.onCreate(savedInstanceState)
    }

    override fun init() {
    }

    override fun getLayoutId(): Int {
        if(replaceLayoutID>0){
            return  replaceLayoutID
        }
        return R.layout.kit_activity_room_list
    }
}