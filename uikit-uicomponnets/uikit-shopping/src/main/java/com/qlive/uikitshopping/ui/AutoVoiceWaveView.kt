package com.qlive.uikitshopping.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner


class AutoVoiceWaveView : VoiceWaveView, LifecycleEventObserver {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun attach(lifecycleOwner: LifecycleOwner) {
        lineWidth = 5f
        lineColor = Color.WHITE
        addBody(17)
        addBody(40)
        addBody(38)
        addBody(8)
        lifecycleOwner.lifecycle.addObserver(this)
    }

    private var isAutoPlay = false

    fun setAutoPlay(autoPlay: Boolean) {
        isAutoPlay = autoPlay
        if (isAutoPlay) {
            start()
        } else {
            stop()
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_PAUSE) {
            if(isAutoPlay){
                stop()
            }
        }
        if (event == Lifecycle.Event.ON_RESUME) {
            if(isAutoPlay){
                start()
            }
        }
    }
}