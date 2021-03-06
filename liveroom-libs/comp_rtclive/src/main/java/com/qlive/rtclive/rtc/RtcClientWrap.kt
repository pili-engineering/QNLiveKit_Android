package com.qlive.rtclive.rtc

import android.content.Context
import com.qiniu.droid.rtc.*
import com.qlive.liblog.QLiveLogUtil

open class RtcClientWrap(
    val context: Context,
    val setting: QNRTCSetting,
    val mQNRTCClientConfig: QNRTCClientConfig,
    val isAutoSubscribe: Boolean = false
) {

    /**
     * 额外的引擎监听包装为了把让各个模块都能监听rtc事件处理自己的逻辑
     */
    open val mQNRTCEngineEventWrap = QNRTCEngineEventWrap()

    /**
     *  添加你需要的引擎状回调
     */
    fun addExtraQNRTCEngineEventListener(extraQNRTCEngineEventListener: ExtQNClientEventListener) {
        mQNRTCEngineEventWrap.addExtraQNRTCEngineEventListener(extraQNRTCEngineEventListener)
    }

    fun addExtraQNRTCEngineEventListenerToHead(extraQNRTCEngineEventListener: ExtQNClientEventListener) {
        mQNRTCEngineEventWrap.addExtraQNRTCEngineEventListener(extraQNRTCEngineEventListener, true)
    }

    /**
     * 移除额外的监听
     */
    fun removeExtraQNRTCEngineEventListener(extraQNRTCEngineEventListener: ExtQNClientEventListener) {
        mQNRTCEngineEventWrap.removeExtraQNRTCEngineEventListener(extraQNRTCEngineEventListener)
    }

    private val mQNRTCEventListener = QNRTCEventListener { }

    init {
        if(!QLiveLogUtil.isLogAble){
            setting.logLevel =QNLogLevel.NONE
        }else{
            setting.logLevel =QNLogLevel.INFO
        }
        QNRTC.init(context, setting, mQNRTCEventListener) // 初始化
    }

    /**
     *  rtc
     */
    val mClient by lazy {
        QNRTC.createClient(mQNRTCClientConfig, mQNRTCEngineEventWrap).apply {
            setAutoSubscribe(isAutoSubscribe)
        }
    }

}