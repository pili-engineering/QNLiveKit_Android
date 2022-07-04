package com.qlive.uiwidghtbeauty

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BeautyItemBgLoader {

    fun start(work: () -> Unit, call: () -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val ret = async(Dispatchers.IO) {
                work()
            }
            ret.await()
            call.invoke()
        }
    }
}