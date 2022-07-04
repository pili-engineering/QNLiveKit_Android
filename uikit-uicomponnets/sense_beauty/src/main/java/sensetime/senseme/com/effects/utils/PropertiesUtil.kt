package sensetime.senseme.com.effects.utils

import java.util.*

object PropertiesUtil {

    fun getProperties(): Properties {
        val props = Properties()
        try {
            val inputStream = ContextHolder.getContext().assets.open("appConfig.properties")
            props.load(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return props
    }

    fun getBooleanValue(key: String): Boolean {
        val value = getProperties().getProperty(key)
        if ("true".equals(value, true))
            return true
        return false
    }
}