package sensetime.senseme.com.effects.utils

import org.greenrobot.eventbus.EventBus

object EventBusUtils {

    private fun isEventBusRegistered(subscribe: Any?): Boolean {
        return EventBus.getDefault().isRegistered(subscribe)
    }

    fun registerEventBus(subscriber: Any?) {
        if (!isEventBusRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber)
        }
    }

    fun unregisterEventBus(subscriber: Any?) {
        if (isEventBusRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber)
        }
    }

    fun post(event: Any?) {
        EventBus.getDefault().post(event)
    }

    fun postSticky(event: Any?) {
        EventBus.getDefault().postSticky(event)
    }
}