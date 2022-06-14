package com.qncube.uikitcore

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

interface KitContext {
    var androidContext: Context
    var fm: FragmentManager
    var currentActivity: Activity
    var lifecycleOwner: LifecycleOwner
}