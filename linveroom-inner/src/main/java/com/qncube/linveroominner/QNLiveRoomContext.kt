package com.qncube.linveroominner

import com.qncube.liveroomcore.QLiveClient
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser

import com.qncube.liveroomcore.QLiveService

class QNLiveRoomContext(private val mClient: QLiveClient) {

    private val serviceMap = HashMap<Class<*>, Any>()
    private val mLifeCycleListener = ArrayList<QClientLifeCycleListener>()
    val mRoomScheduler = com.qncube.linveroominner.RoomScheduler()
    var roomInfo: QLiveRoomInfo? = null

    init {
        mLifeCycleListener.add(mRoomScheduler)
    }

    private fun <T : QLiveService> registerService(serviceClass: Class<T>) {
        try {
            val classStr = serviceClass.name + "Impl"
            val classImpl = Class.forName(classStr)
            val constructor = classImpl.getConstructor()
            val obj = constructor.newInstance() as BaseService
            serviceMap[serviceClass] = obj
            mLifeCycleListener.add(obj)
            obj.attachRoomClient(mClient)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun <T : QLiveService> getService(serviceClass: Class<T>): T? {
        val serviceObj = serviceMap[serviceClass] as T?
        if (serviceObj == null) {
            registerService(serviceClass)
            return serviceMap[serviceClass] as T?
        }
        return serviceObj
    }

    /**
     * 添加房间生命周期状态监听
     *
     * @param lifeCycleListener
     */
    fun addRoomLifeCycleListener(lifeCycleListener: QClientLifeCycleListener) {
        mLifeCycleListener.add(lifeCycleListener)
    }

    //移除房间生命周期状态监听
    fun removeRoomLifeCycleListener(lifeCycleListener: QClientLifeCycleListener) {
        mLifeCycleListener.remove(lifeCycleListener)
    }

    fun enter(liveId: String, user: QLiveUser) {
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