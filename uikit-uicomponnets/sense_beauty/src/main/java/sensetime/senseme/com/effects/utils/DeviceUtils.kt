package sensetime.senseme.com.effects.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

object DeviceUtils {

    fun getAppVersionName(): String {
        var versionName = ""
        try {
            val pm: PackageManager = getContext().packageManager
            val pi: PackageInfo = pm.getPackageInfo(getContext().packageName, 0)
            versionName = pi.versionName
            if (versionName == null || versionName.isEmpty()) {
                return ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return versionName
    }

    private fun getContext(): Context {
        return ContextHolder.getContext()
    }
}