package com.qlive.uikitcore

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.qlive.avparam.QPlayerRenderView
import com.qlive.core.been.QCreateRoomParam
import com.qlive.core.QLiveCallBack
import com.qlive.rtclive.QPushRenderView

/**
 * uikit UI组件上下文
 * 1在UI组件中能获取平台特性的能力 如activiy 显示弹窗
 *
 */
class QUIKitContext(
    /**
     * 安卓上下文
     */
    val androidContext: Context,
    /**
     * 安卓FragmentManager 用于显示弹窗
     */
    val fragmentManager: FragmentManager,
    /**
     * 当前所在的Activity
     */
    val currentActivity: Activity,
    /**
     * 当前页面的安卓LifecycleOwner
     */
    val lifecycleOwner: LifecycleOwner,
)

/**
 * uikit 房间里的UI组件上下文
 * 1在UI组件中能获取平台特性的能力 如activiy 显示弹窗
 * 2能获取房间client 主要资源和关键操作
 */
class QLiveUIKitContext(
    /**
     * 安卓上下文
     */
    val androidContext: Context,
    /**
     * 安卓FragmentManager 用于显示弹窗
     */
    val fragmentManager: FragmentManager,
    /**
     * 当前所在的Activity
     */
    val currentActivity: Activity,
    /**
     * 当前页面的安卓LifecycleOwner
     */
    val lifecycleOwner: LifecycleOwner,
    /**
     * 离开房间操作 在任意UI组件中可以操作离开房间
     */
    val leftRoomActionCall: (resultCall: QLiveCallBack<Void>) -> Unit, //离开房间操作
    /**
     * 创建并且加入房间操作 在任意UI组件中可创建并且加入房间
     */
    val createAndJoinRoomActionCall: (param: QCreateRoomParam, resultCall: QLiveCallBack<Void>) -> Unit,//创建并加入操作
    /**
     * 获取当前播放器预览窗口 在任意UI组件中如果要对预览窗口变化可直接获取
     * 比如连麦pk组件需要改变预览窗口
     */
    val getPlayerRenderViewCall: () -> QPlayerRenderView?, //获取当前播放器预览窗口
    /**
     * 获取推流预览窗口  在任意UI组件中如果要对预览窗口变化可直接获取
     * 比如连麦pk组件需要改变预览窗口
     */
    val getPusherRenderViewCall: () -> QPushRenderView?,  //获取推流预览窗口

)

