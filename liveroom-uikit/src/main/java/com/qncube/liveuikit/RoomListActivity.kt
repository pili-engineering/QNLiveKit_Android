package com.qncube.liveuikit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.LayoutInflaterCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.qncube.liveuikit.hook.KITLiveInflaterFactory
import com.qncube.uikitcore.KitContext
import com.qncube.uikitcore.activity.BaseFrameActivity

class RoomListActivity : BaseFrameActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, RoomListActivity::class.java))
        }
    }

    private val mKitContext by lazy {
        object : KitContext {
            override var androidContext: Context = this@RoomListActivity
            override var fm: FragmentManager = supportFragmentManager
            override var currentActivity: Activity = this@RoomListActivity
            override var lifecycleOwner: LifecycleOwner = this@RoomListActivity
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(
            LayoutInflater.from(this),
            KITLiveInflaterFactory(delegate, null, mKitContext)
        )
        super.onCreate(savedInstanceState)
    }

    override fun init() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_room_list
    }
}