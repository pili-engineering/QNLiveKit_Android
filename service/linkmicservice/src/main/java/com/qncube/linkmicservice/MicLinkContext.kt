package com.qncube.linkmicservice

import com.nucube.rtclive.DefaultExtQNClientEventListener
import com.nucube.rtclive.RtcLiveRoom
import com.qiniu.jsonutil.JsonUtils
import com.qncube.liveroomcore.been.QMicLinker
import java.util.*

class MicLinkContext {

    val allLinker = LinkedList<QMicLinker>()
    val mQLinkMicServiceListeners = LinkedList<QLinkMicServiceListener>()
    fun removeLinker(uid: String): QMicLinker?{
        getMicLinker(uid)?.let {
            allLinker.remove(it)
            return it
        }
        return null
    }

    fun addLinker(linker: QMicLinker): Boolean {
        val it = getMicLinker(linker.user.userId)
        if (it == null) {
            allLinker.add(linker)
            return true
        } else {
            it.user = linker.user
            it.userRoomID = linker.userRoomID
            it.isOpenMicrophone = linker.isOpenMicrophone
            it.isOpenCamera = linker.isOpenCamera
            it.extension = linker.extension
            return false
        }
    }

    fun getMicLinker(uid: String): QMicLinker? {
        allLinker.forEach {
            if (it.user?.userId == uid) {
                return it
            }
        }
        return null
    }

    val mExtQNClientEventListener = object : DefaultExtQNClientEventListener {
        //
        override fun onUserJoined(p0: String, p1: String?) {
            val micLinker = JsonUtils.parseObject(p1, QMicLinker::class.java) ?: return
            val it = getMicLinker(p0)
            addLinker(micLinker)
            if (it == null) {
                mQLinkMicServiceListeners.forEach {
                    it.onLinkerJoin(micLinker)
                }
            }
        }

        override fun onUserLeft(p0: String) {
            val mic = getMicLinker(p0)
            if (mic != null) {
                removeLinker(p0)
                mQLinkMicServiceListeners.forEach {
                    it.onLinkerLeft(mic)
                }
            }
        }
    }
    lateinit var mRtcLiveRoom: RtcLiveRoom

}