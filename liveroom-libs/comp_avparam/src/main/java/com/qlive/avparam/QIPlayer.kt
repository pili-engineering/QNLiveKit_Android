package com.qlive.avparam

interface QIPlayer {
    fun setUp(uir: String, headers: Map<String, String>? = null)
    fun start()

    /**
     * 暂停
     */
    fun pause()

    /**
     * 恢复
     */
    fun resume()
    fun stop()
    fun release()

    fun onLinkStatusChange(isLink: Boolean)
}