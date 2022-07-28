package com.qlive.uikitcore

import java.util.*

object QLiveUIEventManager {
    private val mQLiveComponents = LinkedList<QLiveComponent>()
    /**
     * 发送UI事件给所有UI组件
     *
     * @param action 事件名字
     * @param data   数据
     */
    internal fun sendUIEvent(srcComponentClassName:String,action: String, data: String) {
        mQLiveComponents.forEach {
            it.onReceiveUIEvent(srcComponentClassName, action, data)
        }
    }

    /**
     * 发送UI事件给目标组件
     *
     * @param targetComponentClassName 目标组件的类全限定名
     * @param action                   事件名字
     * @param data                     数据
     */
    internal fun sendUIEvent(srcComponentClassName:String,targetComponentClassName: String, action: String, data: String) {
        mQLiveComponents.forEach {
            if(it.javaClass.canonicalName==targetComponentClassName){
                it.onReceiveUIEvent(srcComponentClassName, action, data)
            }
        }
    }

    fun attach(liveComponents:List<QLiveComponent>){
        mQLiveComponents.addAll(liveComponents)
    }

    fun clear(){
        mQLiveComponents.clear()
    }
}