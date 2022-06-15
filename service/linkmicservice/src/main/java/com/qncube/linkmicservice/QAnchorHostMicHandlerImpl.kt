package com.qncube.linkmicservice

import com.nucube.rtclive.QRTCLiveProvider
import com.nucube.rtclive.QRtcLiveRoom
import com.qncube.lcommon.CameraMergeOption
import com.qncube.lcommon.MicrophoneMergeOption
import com.qncube.lcommon.QMergeOption
import com.qncube.linveroominner.BaseService
import com.qncube.liveroomcore.QLiveClient
import com.qncube.liveroomcore.been.QExtension
import com.qncube.liveroomcore.been.QMicLinker

class QAnchorHostMicHandlerImpl(private val context: MicLinkContext) : QAnchorHostMicHandler,
    BaseService() {

    private var mQMixStreamAdapter: QAnchorHostMicHandler.QMixStreamAdapter? = null
    private val mQLinkMicServiceListener = object :
        QLinkMicServiceListener {
        private val mOps = HashMap<String, QMergeOption>()
        override fun onInitLinkers(linkers: MutableList<QMicLinker>) {}
        override fun onLinkerJoin(micLinker: QMicLinker) {
            if (context.mQRtcLiveRoom.mMixStreamManager.mQNMergeJob == null) {
                // context.mRtcLiveRoom.mMixStreamManager.clear()
                context.mQRtcLiveRoom.mMixStreamManager.startMixStreamJob()
            }
            val ops = mQMixStreamAdapter?.onResetMixParam(context.allLinker, micLinker, true)
            mOps.clear()
            ops?.forEach {
                mOps.put(it.uid, it)
                context.mQRtcLiveRoom.mMixStreamManager.updateUserAudioMergeOptions(
                    it.uid,
                    it.microphoneMergeOption,
                    false
                )
                context.mQRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
                    it.uid,
                    it.cameraMergeOption,
                    false
                )
            }
            context.mQRtcLiveRoom.mMixStreamManager.commitOpt()
        }

        override fun onLinkerLeft(micLinker: QMicLinker) {
            if (context.mQRtcLiveRoom.mMixStreamManager
                    .roomUser == 0
            ) {
                context.mQRtcLiveRoom.mMixStreamManager.startForwardJob()
                return
            }
            val ops = mQMixStreamAdapter?.onResetMixParam(context.allLinker, micLinker, false)
            context.mQRtcLiveRoom.mMixStreamManager.updateUserAudioMergeOptions(
                micLinker.user?.userId ?: "",
                MicrophoneMergeOption(),
                false
            )
            context.mQRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
                micLinker.user?.userId ?: "",
                CameraMergeOption(),
                false
            )
            mOps.clear()
            ops?.forEach {
                mOps.put(it.uid, it)
                context.mQRtcLiveRoom.mMixStreamManager.updateUserAudioMergeOptions(
                    it.uid,
                    it.microphoneMergeOption,
                    false
                )
                context.mQRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
                    it.uid,
                    it.cameraMergeOption,
                    false
                )
            }
            context.mQRtcLiveRoom.mMixStreamManager.commitOpt()
        }

        override fun onLinkerMicrophoneStatusChange(micLinker: QMicLinker) {}
        override fun onLinkerCameraStatusChange(micLinker: QMicLinker) {
            if (micLinker.isOpenCamera) {
                //打开了
                mOps.get(micLinker.user.userId)?.cameraMergeOption?.let {
                    context.mQRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
                        micLinker.user.userId,
                        it,
                        true
                    )
                }
            } else {
                //关闭了摄像头
                context.mQRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
                    micLinker.user.userId,
                    CameraMergeOption(),
                    true
                )
            }
        }

        override fun onLinkerKicked(micLinker: QMicLinker, msg: String) {}
        override fun onLinkerExtensionUpdate(micLinker: QMicLinker, extension: QExtension) {}
    }

    /**
     * 设置混流适配器
     * @param QMixStreamAdapter
     */
    override fun setMixStreamAdapter(QMixStreamAdapter: QAnchorHostMicHandler.QMixStreamAdapter?) {
        mQMixStreamAdapter = QMixStreamAdapter
    }

    override fun attachRoomClient(client: QLiveClient) {
        val room: QRtcLiveRoom = rtcRoomGetter
        context.mQLinkMicServiceListeners.addFirst(mQLinkMicServiceListener)
        context.mQRtcLiveRoom = room
        context.mQRtcLiveRoom.addExtraQNRTCEngineEventListener(context.mExtQNClientEventListener)
        super.attachRoomClient(client)
    }

    override fun onDestroyed() {
        super.onDestroyed()
    }

    /**
     * 获得rtc对象
     */
    private val rtcRoomGetter by lazy {
        (client as QRTCLiveProvider).rtcRoomGetter.invoke()
    }
}