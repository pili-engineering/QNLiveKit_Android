package com.qlive.uikit.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.qlive.avparam.QBeautySetting
import com.qlive.pushclient.QPusherClient
import com.qlive.uikit.R
import com.qlive.uikit.hook.KITFunctionInflaterFactory
import com.qlive.uikitcore.QKitImageView
import com.qlive.uikitcore.ShowDialogAble
import com.qlive.uikitcore.ext.setDoubleCheckClickListener

class ShowBeautyView : QKitImageView {

    private var component: ShowDialogAble? = null
    private var isOpen = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        component =
            KITFunctionInflaterFactory.findLiveComponentByName("com.qlive.uikitbeauty.QSenseBeautyComponent") as ShowDialogAble?
        if (component != null) {
            setDoubleCheckClickListener {
                component?.showDialog(0, Unit)
            }
        } else {
            setDoubleCheckClickListener {
                if (!isOpen) {
                    isOpen = true
                    (client as QPusherClient).setDefaultBeauty(
                        QBeautySetting(
                            0.6f,
                            0.8f,
                            0.6f
                        ).apply { setEnable(true) })
                } else {
                    isOpen = false
                    (client as QPusherClient).setDefaultBeauty(
                        QBeautySetting(
                            0f,
                            0f,
                            0f
                        ).apply { setEnable(false) })
                }
            }
        }
    }

}