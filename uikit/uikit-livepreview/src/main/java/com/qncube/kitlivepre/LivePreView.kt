package com.qncube.kitlivepre

import android.content.Context
import android.util.AttributeSet
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.mode.QNCreateRoomParam
import com.qncube.liveroomcore.QPusherClient
import com.qncube.lcommon.RtcException
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.uikitcore.QBaseRoomFrameLayout
import com.qncube.uikitcore.dialog.LoadingDialog
import com.qncube.uikitcore.ext.bg
import kotlinx.android.synthetic.main.kit_live_preview.view.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 开播预览槽位
 */
class LivePreView : QBaseRoomFrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun getLayoutId(): Int {
        return R.layout.kit_live_preview
    }

    suspend fun createSuspend(p: QNCreateRoomParam) = suspendCoroutine<QLiveRoomInfo> { ct ->
        QNLiveRoomEngine.createRoom(p, object :
            QLiveCallBack<QLiveRoomInfo> {
            override fun onError(code: Int, msg: String) {
                ct.resumeWithException(RtcException(code, msg))
            }

            override fun onSuccess(data: QLiveRoomInfo) {
                ct.resume(data)
            }

        })
    }

    private suspend fun suspendJoinRoom(roomId: String) = suspendCoroutine<QLiveRoomInfo> { cont ->
        client!!.joinRoom(roomId, object :
            QLiveCallBack<QLiveRoomInfo> {
            override fun onError(code: Int, msg: String?) {
                cont.resumeWithException(RtcException(code, msg ?: ""))
            }

            override fun onSuccess(data: QLiveRoomInfo) {
                cont.resume(data)
            }
        })
    }

    private fun create(p: QNCreateRoomParam) {
        kitContext?.lifecycleOwner?.bg {
            LoadingDialog.showLoading(kitContext!!.fm)
            doWork {
                val info = createSuspend(p)
                suspendJoinRoom(info.liveId)
            }
            catchError {
                it.message?.asToast()
            }
            onFinally {
                LoadingDialog.cancelLoadingDialog()
            }
        }
    }

    override fun initView() {

        ivClose.setOnClickListener {
            kitContext?.currentActivity?.finish()
        }
        tvStart.setOnClickListener {
            val titleStr = etTitle.text.toString()
            if (titleStr.isEmpty()) {
                context!!.resources.toast(R.string.preview_tip_input_title)
                return@setOnClickListener
            }
            val noticeStr = etNotice.text.toString() ?: ""
            create(QNCreateRoomParam().apply {
                title = titleStr
                notice = noticeStr
                coverURL =UserDataSource.loginUser?.avatar
            })
        }

        llBeauty.setOnClickListener {
        }

        llSwitch.setOnClickListener {
            (client as QPusherClient)
                .switchCamera()
        }
    }
}