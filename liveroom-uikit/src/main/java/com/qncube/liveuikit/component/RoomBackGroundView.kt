package com.qncube.liveuikit.component

import android.content.Context
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.qbcube.pkservice.QPKService
import com.qbcube.pkservice.QPKServiceListener
import com.qbcube.pkservice.QPKSession
import com.qncube.liveroomcore.been.QExtension
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.uikitcore.*
import kotlinx.android.synthetic.main.kit_img_bg.view.*


/**
 * 房间背景图片组件
 */
class RoomBackGroundView : QBaseRoomFrameLayout {

    companion object {
        //默认背景图片
        var defaultBackGroundImg = com.qncube.liveuikit.R.drawable.kit_dafault_room_bg
    }
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
            ivBg.setImageResource(defaultBackGroundImg)
        }

        override fun onStop(pkSession: QPKSession, code: Int, msg: String) {
            Glide.with(context!!).load(roomInfo?.coverUrl)
                .into(ivBg)
        }

        override fun onStartTimeOut(pkSession: QPKSession) {}
        override fun onPKExtensionUpdate(pkSession: QPKSession, extension: QExtension) {}
    }


    override fun getLayoutId(): Int {

        return com.qncube.liveuikit.R.layout.kit_img_bg
    }

    override fun initView() {
        client?.getService(QPKService::class.java)?.addServiceListener(mQPKServiceListener)
        ivBg.setImageResource(defaultBackGroundImg)
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        Glide.with(context!!).load(roomInfo.coverUrl)
            .into(ivBg)
    }

}