package com.qlive.uikitpk

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.qlive.pkservice.QPKService
import com.qlive.pkservice.QPKServiceListener
import com.qlive.core.been.QPKSession
import com.qlive.core.been.QExtension

import com.qlive.uikitcore.QBaseRoomFrameLayout

class PKCoverView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val mQPKServiceListener = object :
        QPKServiceListener {


        override fun onStart(pkSession: QPKSession) {
            visibility = View.VISIBLE
        }

        override fun onStop(pkSession: QPKSession, code: Int, msg: String) {
            visibility = View.GONE
        }

        override fun onStartTimeOut(pkSession: QPKSession) {
        }

        override fun onPKExtensionUpdate(pkSession: QPKSession, extension: QExtension) {
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_pk_cover_view
    }

    override fun initView() {
        client!!.getService(QPKService::class.java).addServiceListener(mQPKServiceListener)
    }


}