package com.qncube.linkmicservice

import com.nucube.rtclive.CameraMergeOption
import com.nucube.rtclive.MicrophoneMergeOption
import com.nucube.rtclive.QMergeOption
import com.nucube.rtclive.RtcLiveRoom
import com.qncube.linveroominner.getRtc
import com.qncube.liveroomcore.service.BaseService
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
            if (context.mRtcLiveRoom.mMixStreamManager.mQNMergeJob == null) {
                // context.mRtcLiveRoom.mMixStreamManager.clear()
                context.mRtcLiveRoom.mMixStreamManager.startMixStreamJob()
            }
            val ops = mQMixStreamAdapter?.onResetMixParam(context.allLinker, micLinker, true)
            mOps.clear()
            ops?.forEach {
                mOps.put(it.uid, it)
                context.mRtcLiveRoom.mMixStreamManager.updateUserAudioMergeOptions(
                    it.uid,
                    it.microphoneMergeOption,
                    false
                )
                context.mRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
                    it.uid,
                    it.cameraMergeOption,
                    false
                )
            }
            context.mRtcLiveRoom.mMixStreamManager.commitOpt()
        }

        override fun onLinkerLeft(micLinker: QMicLinker) {
            if (context.mRtcLiveRoom.mMixStreamManager
                    .roomUser == 0
            ) {
                context.mRtcLiveRoom.mMixStreamManager.startForwardJob()
                return
            }
            val ops = mQMixStreamAdapter?.onResetMixParam(context.allLinker, micLinker, false)
            context.mRtcLiveRoom.mMixStreamManager.updateUserAudioMergeOptions(
                micLinker.user?.userId ?: "",
                MicrophoneMergeOption(),
                false
            )
            context.mRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
                micLinker.user?.userId ?: "",
                CameraMergeOption(),
                false
            )
            mOps.clear()
            ops?.forEach {
                mOps.put(it.uid, it)
                context.mRtcLiveRoom.mMixStreamManager.updateUserAudioMergeOptions(
                    it.uid,
                    it.microphoneMergeOption,
                    false
                )
                context.mRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
                    it.uid,
                    it.cameraMergeOption,
                    false
                )
            }
            context.mRtcLiveRoom.mMixStreamManager.commitOpt()
        }
        override fun onLinkerMicrophoneStatusChange(micLinker: QMicLinker) {}
        override fun onLinkerCameraStatusChange(micLinker: QMicLinker) {
            if (micLinker.isOpenCamera) {
                //打开了
                mOps.get(micLinker.user.userId)?.cameraMergeOption?.let {
                    context.mRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
                        micLinker.user.userId,
                        it,
                        true
                    )
                }
            } else {
                //关闭了摄像头
                context.mRtcLiveRoom.mMixStreamManager.updateUserVideoMergeOptions(
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
        val field = client.getRtc()
        val room: RtcLiveRoom = field.get(client) as RtcLiveRoom
        context.mQLinkMicServiceListeners.addFirst(mQLinkMicServiceListener)
        context.mRtcLiveRoom = room
        context.mRtcLiveRoom.addExtraQNRTCEngineEventListener(context.mExtQNClientEventListener)
        super.attachRoomClient(client)
    }
    override fun onDestroyed() {
        super.onDestroyed()
    }
}