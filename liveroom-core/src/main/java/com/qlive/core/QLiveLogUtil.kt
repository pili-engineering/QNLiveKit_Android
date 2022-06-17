package com.qlive.core

import android.util.Log

object QLiveLogUtil {

    var isLogAble = true
    private val tag = "QNLiveRoom"

    fun logD(msg: String) {
        if (isLogAble) {
            Log.d(tag, msg)
        }
    }

    fun d(tag:String,msg: String) {
        if (isLogAble) {
            Log.d(tag, msg)
        }
    }
    fun LogE(msg: String) {
        if (isLogAble) {
            Log.e(tag, msg)
        }
    }

}