package com.qlive.uikitcore

import java.util.*

object QLiveUIEventManager {

    private val mQLiveComponents = LinkedList<QLiveComponent>()
    private val mActionMap = HashMap<QLiveComponent, HashMap<String, Function1<UIEvent, Unit>>>()
    fun getActionMap(component: QLiveComponent): HashMap<String, Function1<UIEvent, Unit>> {
        var map = mActionMap.get(component)
        if (map == null) {
            map = HashMap<String, Function1<UIEvent, Unit>>()
            mActionMap.put(component, map)
        }
        return map
    }

    /**
     * 发送UI事件给所有UI组件
     *
     * @param action 事件名字
     * @param data   数据
     */
    fun <T : UIEvent?> sendUIEvent(event: T) {
        mQLiveComponents.forEach {
            val function1 = getActionMap(it)[event!!.getAction()]
            function1?.invoke(event)
        }
    }

    fun attach(liveComponents: List<QLiveComponent>) {
        mQLiveComponents.addAll(liveComponents)
    }

    fun clear() {
        mActionMap.clear()
        mQLiveComponents.clear()
    }
}