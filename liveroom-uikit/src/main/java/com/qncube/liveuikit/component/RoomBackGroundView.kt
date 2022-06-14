package com.qncube.liveuikit.component

import android.content.Context
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.qbcube.pkservice.QNPKService
import com.qbcube.pkservice.QNPKSession
import com.qncube.liveroomcore.Extension
import com.qncube.liveroomcore.mode.QLiveRoomInfo
import com.qncube.uikitcore.*
import kotlinx.android.synthetic.main.kit_img_bg.view.*


/**
 * 房间背景图片组件
 */
class RoomBackGroundView : QBaseRoomFrameLayout {

    companion object {
        //默认背景图片
        var defaultBackGroundImg = R.drawable.kit_dafault_room_bg
    }
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val mPKServiceListener = object : QNPKService.PKServiceListener {
        override fun onInitPKer(pkSession: QNPKSession) {
            ivBg.setImageResource(defaultBackGroundImg)
        }

        override fun onStart(pkSession: QNPKSession) {
            ivBg.setImageResource(defaultBackGroundImg)
        }

        override fun onStop(pkSession: QNPKSession, code: Int, msg: String) {
            Glide.with(context!!).load(roomInfo?.coverUrl)
                .into(ivBg)
        }

        override fun onWaitPeerTimeOut(pkSession: QNPKSession) {
        }

        override fun onPKExtensionUpdate(pkSession: QNPKSession, extension: Extension) {}
    }


    override fun getLayoutId(): Int {
        return R.layout.kit_img_bg
    }

    override fun initView() {
        client?.getService(QNPKService::class.java)?.addPKServiceListener(mPKServiceListener)
        ivBg.setImageResource(defaultBackGroundImg)
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        Glide.with(context!!).load(roomInfo.coverUrl)
            .into(ivBg)
    }

}