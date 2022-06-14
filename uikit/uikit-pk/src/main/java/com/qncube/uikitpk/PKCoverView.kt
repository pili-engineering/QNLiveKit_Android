package com.qncube.uikitpk

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qbcube.pkservice.QNPKService
import com.qbcube.pkservice.QNPKSession
import com.qncube.liveroomcore.Extension
import com.qncube.uikitcore.QBaseRoomFrameLayout

class PKCoverView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val mPKServiceListener = object : QNPKService.PKServiceListener {
        override fun onInitPKer(pkSession: QNPKSession) {

        }

        override fun onStart(pkSession: QNPKSession) {
            visibility = View.VISIBLE
        }

        override fun onStop(pkSession: QNPKSession, code: Int, msg: String) {
            visibility = View.GONE
        }

        override fun onWaitPeerTimeOut(pkSession: QNPKSession) {}


        override fun onPKExtensionUpdate(pkSession: QNPKSession, extension: Extension) {
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.kit_pk_cover_view
    }

    override fun initView() {
        client!!.getService(QNPKService::class.java).addPKServiceListener(mPKServiceListener)
    }


}