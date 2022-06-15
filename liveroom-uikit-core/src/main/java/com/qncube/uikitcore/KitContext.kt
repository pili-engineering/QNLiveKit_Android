package com.qncube.uikitcore

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.qncube.liveroomcore.QCreateRoomParam
import com.qncube.liveroomcore.QLiveCallBack

interface KitContext {
    var androidContext: Context
    var fm: FragmentManager
    var currentActivity: Activity
    var lifecycleOwner: LifecycleOwner
}

interface LiveKitContext : KitContext {
    var leftRoomActionCall: (resultCall: QLiveCallBack<Void>) -> Unit
    var createAndJoinRoomActionCall: (param: QCreateRoomParam, resultCall: QLiveCallBack<Void>) -> Unit
}

