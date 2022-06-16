package com.qncube.lcommon

import android.net.Uri

interface QIPlayer {
    fun setUp(uir: String, headers: Map<String, String>? = null)
    fun start()

    /**
     * 暂停
     */
    fun pause()

    fun stop()

    /**
     * 恢复
     */
    fun resume()

    fun release()
}