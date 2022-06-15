package com.qncube.linveroominner
import android.content.res.Resources
import android.widget.Toast
import com.qncube.linveroominner.AppCache


fun String.asToast() {
    if (this.isEmpty()) {
        return
    }
    Toast.makeText(AppCache.appContext, this, Toast.LENGTH_SHORT).show()
}

fun Resources.toast(res: Int) {
    getString(res).asToast()
}

