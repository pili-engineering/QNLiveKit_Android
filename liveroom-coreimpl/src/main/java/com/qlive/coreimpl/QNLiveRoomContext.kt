package com.qlive.coreimpl

import com.qlive.core.QClientLifeCycleListener
import com.qlive.core.QClientType
import com.qlive.core.QLiveClient
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser

import com.qlive.core.QLiveService
import com.qlive.coreimpl.datesource.UserDataSource

class QNLiveRoomContext(private val mClient: QLiveClient) {

    private val serviceMap = HashMap<String, Any>()
    private val mLifeCycleListener = ArrayList<QClientLifeCycleListener>()
    val mRoomScheduler = com.qlive.coreimpl.RoomScheduler()
    var roomInfo: QLiveRoomInfo? = null
        private set
    private var liveId = ""

    private fun registerService(serviceClass: String) {
        try {
            val classStr = serviceClass + "Impl"
            val classImpl = Class.forName(classStr)
            val constructor = classImpl.getConstructor()
            val obj = constructor.newInstance() as BaseService
            serviceMap[serviceClass] = obj
            mLifeCycleListener.add(obj)
            obj.attachRoomClient(mClient)
            if (liveId.isNotEmpty()) {
                obj.onEntering(roomInfo!!.liveID, UserDataSource.loginUser)
            }
            if (roomInfo != null) {
                obj.onJoined(roomInfo!!)
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    fun <T : QLiveService> getService(serviceClass: Class<T>): T? {
        val serviceObj = serviceMap[serviceClass.name] as T?
        if (serviceObj == null) {
            registerService(serviceClass.name)
            return serviceMap[serviceClass.name] as T?
        }
        return serviceObj
    }

    private var isInit = false
    fun checkInit() {
        if (isInit) {
            return
        }
        mLifeCycleListener.add(mRoomScheduler)
        isInit = true
    }

    fun enter(liveId: String, user: QLiveUser) {
        this.liveId = liveId
        mLifeCycleListener.forEach {
            it.onEntering(liveId, user)
        }
    }

    fun leaveRoom() {
        mLifeCycleListener.forEach {
            it.onLeft()
        }
        this.roomInfo = null
    }

    fun joinedRoom(roomInfo: QLiveRoomInfo) {
        this.roomInfo = roomInfo
        mLifeCycleListener.forEach {
            it.onJoined(roomInfo)
        }
    }

    fun destroy() {
        mLifeCycleListener.forEach {
            it.onDestroyed()
        }
        mLifeCycleListener.clear()
        serviceMap.clear()
    }
}