package com.qlive.uikit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.LayoutInflaterCompat
import com.qlive.uikit.hook.KITInflaterFactory
import com.qlive.uikitcore.QUIKitContext
import com.qlive.uikitcore.activity.BaseFrameActivity

class RoomListActivity : BaseFrameActivity() {

    companion object {
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

    override fun isToolBarEnable(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_activity_room_list
    }
}