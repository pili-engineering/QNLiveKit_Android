package com.qlive.uikitcore

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.qlive.avparam.QPlayerRenderView
import com.qlive.rtclive.QPushRenderView
import com.qlive.core.been.QCreateRoomParam
import com.qlive.core.QLiveCallBack

/**
 * uikit UI组件上下文
 * 1在UI组件中能获取平台特性的能力 如activiy 显示弹窗
 */
class QUIKitContext(
    val androidContext: Context,
    val fragmentManager: FragmentManager,
    val currentActivity: Activity,
    val lifecycleOwner: LifecycleOwner,
)

/**
 * uikit 房间里的UI组件上下文
 * 1在UI组件中能获取平台特性的能力 如activiy 显示弹窗
 * 2能获取房间client 主要资源和关键操作
 */
class QLiveUIKitContext(
    val androidContext: Context, //安卓上下文
    val fragmentManager: FragmentManager,     //显示弹窗上下文
    val currentActivity: Activity, //当前activity
    val lifecycleOwner: LifecycleOwner, //页面生命周期
    val leftRoomActionCall: (resultCall: QLiveCallBack<Void>) -> Unit, //离开房间操作
    val createAndJoinRoomActionCall: (param: QCreateRoomParam, resultCall: QLiveCallBack<Void>) -> Unit,//创建并加入操作
    val getPlayerRenderViewCall: () -> QPlayerRenderView?, //获取当前播放器预览窗口
    val getPusherRenderViewCall: () -> QPushRenderView?,  //获取推流预览窗口
)

