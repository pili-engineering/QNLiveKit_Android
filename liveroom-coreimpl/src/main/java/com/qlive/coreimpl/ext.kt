package com.qlive.coreimpl
import android.content.res.Resources
import android.widget.Toast


fun String.asToast() {
    if (this.isEmpty()) {
        return
    }
    Toast.makeText(AppCache.appContext, this, Toast.LENGTH_SHORT).show()
}

fun Resources.toast(res: Int) {
    getString(res).asToast()
}

