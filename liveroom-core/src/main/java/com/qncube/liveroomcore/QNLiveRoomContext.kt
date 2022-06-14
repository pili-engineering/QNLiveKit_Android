package com.qncube.liveroomcore

import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.liveroomcore.mode.QNLiveUser

class QNLiveRoomContext(private val mClient: QNLiveClient) {

    private val serviceMap = HashMap<Class<*>, Any>()
    private val mLifeCycleListener = ArrayList<QClientLifeCycleListener>()
    val mRoomScheduler = RoomScheduler()
    var roomInfo: QLiveRoomInfo? = null

    init {
        mLifeCycleListener.add(mRoomScheduler)
    }

    fun <T : QNLiveService> registerService(serviceClass: Class<T>) {

        val classStr = serviceClass.name + "Impl"
        val classImpl = Class.forName(classStr)
        val constructor = classImpl.getConstructor()
        val obj = constructor.newInstance() as QNLiveService
        serviceMap[serviceClass] = obj
        mLifeCycleListener.add(obj)
        obj.attachRoomClient(mClient)

    }

    fun <T : QNLiveService> getService(serviceClass: Class<T>): T? {
        return serviceMap[serviceClass] as T?
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


    fun enter(liveId: String, user: QNLiveUser) {
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

    fun close() {
        mLifeCycleListener.forEach {
            it.onDestroyed()
        }
        mLifeCycleListener.clear()
        serviceMap.clear()
    }

}