package com.qncube.pushclient

import com.niucube.rtm.RtmManager
import com.niucube.rtm.joinChannel
import com.niucube.rtm.leaveChannel
import com.nucube.rtclive.*
import com.qiniu.droid.rtc.*
import com.qncube.lcommon.*
import com.qncube.linveroominner.AppCache
import com.qncube.linveroominner.QNLiveRoomContext
import com.qncube.linveroominner.backGround
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.QClientType
import com.qncube.liveroomcore.QLiveService
import com.qncube.liveroomcore.been.QLiveRoomInfo

class QPusherClientImpl : QPusherClient, QRTCLiveProvider {

    companion object {
        fun create(): QPusherClient {
            return QPusherClientImpl()
        }
    }

    private val mRoomSource = com.qncube.linveroominner.RoomDataSource()
    private var mLiveStatusListener: QLiveStatusListener? = null
    private var mLocalPreView: QPushRenderView? = null
    private var mCameraParams: QCameraParam =
        QCameraParam()
    private var mQMicrophoneParam: QMicrophoneParam =
        QMicrophoneParam()
    private var mConnectionStatusLister: QConnectionStatusLister? = null
    private val mRtcRoom by lazy {
        QRtcLiveRoom(AppCache.appContext).apply {
            addExtraQNRTCEngineEventListener(
                object : DefaultExtQNClientEventListener {
                    override fun onConnectionStateChanged(
                        p0: QNConnectionState,
                        p1: QNConnectionDisconnectedInfo?
                    ) {
                        super.onConnectionStateChanged(p0, p1)
                        mConnectionStatusLister?.onConnectionStatusChanged(p0.toQConnectionState())
                    }
                }
            )
        }
    }
    private val mQNLiveRoomContext by lazy {
        QNLiveRoomContext(this).apply {
            mRoomScheduler.roomStatusChange = {
                mLiveStatusListener?.onLiveStatusChanged(it)
            }
        }
    }

    /**
     * 获取服务实例
     *
     * @param serviceClass
     * @param <T>
     * @return
    </T> */
    override fun <T : QLiveService> getService(serviceClass: Class<T>): T? {
        return mQNLiveRoomContext.getService(serviceClass)
    }

    override fun setLiveStatusListener(liveStatusListener: QLiveStatusListener?) {
        mLiveStatusListener = liveStatusListener
    }

    /**
     * 加入房间
     * @param roomId
     * @param callBack
     */
    override fun joinRoom(roomId: String, callBack: QLiveCallBack<QLiveRoomInfo>?) {
        backGround {
            doWork {
                mQNLiveRoomContext.enter(roomId, com.qncube.linveroominner.UserDataSource.loginUser)
                val roomInfo = mRoomSource.pubRoom(roomId)
                if (RtmManager.isInit) {
                    RtmManager.rtmClient.joinChannel(roomInfo.chatId)
                }
                if (!mRtcRoom.mMixStreamManager.isInit) {
                    mRtcRoom.mMixStreamManager.init(
                        roomId,
                        roomInfo.pushUrl,
                        QMixStreamParams().apply {
                            this.mixStreamWidth = mCameraParams.width
                            this.mixStringHeight = mCameraParams.height
                            this.mixBitrate = mCameraParams.bitrate
                            this.FPS = mCameraParams.FPS
                        })
                    mRtcRoom.mMixStreamManager.setTrack(
                        mRtcRoom.localVideoTrack,
                        mRtcRoom.localAudioTrack
                    )
                }
                mRtcRoom.joinRtc(roomInfo.roomToken, "")
                mRtcRoom.publishLocal()
                mRtcRoom.mMixStreamManager.startForwardJob()
                mQNLiveRoomContext.joinedRoom(roomInfo)

                callBack?.onSuccess(roomInfo)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }

    }

    override fun closeRoom(callBack: QLiveCallBack<Void>?) {
        backGround {
            doWork {
                mRoomSource.unPubRoom(mQNLiveRoomContext.roomInfo?.liveId ?: "")
                if (RtmManager.isInit) {
                    RtmManager.rtmClient.leaveChannel(mQNLiveRoomContext.roomInfo?.chatId ?: "")
                }
                mRtcRoom.leave()
                mQNLiveRoomContext.leaveRoom()
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun destroy() {
        mRtcRoom.close()
        mQNLiveRoomContext.destroy()
    }

    override fun setConnectionStatusLister(connectionStatusLister: QConnectionStatusLister?) {
        mConnectionStatusLister = connectionStatusLister
    }

    override fun enableCamera(cameraParam: QCameraParam?, renderView: QPushRenderView) {
        cameraParam?.let { mCameraParams = it }
        this.mLocalPreView = renderView
        mRtcRoom.enableCamera(cameraParam ?: QCameraParam())
        mRtcRoom.setLocalPreView(mLocalPreView as QNRenderView)
    }

    override fun enableMicrophone(microphoneParams: QMicrophoneParam?) {
        microphoneParams?.let { mQMicrophoneParam = it }
        mRtcRoom.enableMicrophone(microphoneParams ?: QMicrophoneParam())
    }

    override fun switchCamera(callBack: QLiveCallBack<QCameraFace>?) {
        mRtcRoom.switchCamera()
    }

    override fun muteCamera(muted: Boolean, callBack: QLiveCallBack<Boolean>?) {
        if (mRtcRoom.muteLocalCamera(muted)) {
            callBack?.onSuccess(true)
        } else {
            callBack?.onSuccess(false)
        }
    }

    override fun muteMicrophone(muted: Boolean, callBack: QLiveCallBack<Boolean>?) {
        if (mRtcRoom.muteLocalCamera(muted)) {
            callBack?.onSuccess(true)
        } else {
            callBack?.onSuccess(false)
        }
    }

    override fun setVideoFrameListener(frameListener: QVideoFrameListener?) {
        mRtcRoom.setVideoFrameListener(QVideoFrameListenerWrap(frameListener))
    }

    override fun setAudioFrameListener(frameListener: QAudioFrameListener?) {
        mRtcRoom.setAudioFrameListener(QAudioFrameListenerWrap(frameListener))
    }

    override fun getPushRenderView(): QPushRenderView? {
        return mLocalPreView
    }

    override fun getClientType(): QClientType {
        return QClientType.PUSHER
    }

    /**
     * 获得rtc对象
     */
    override var rtcRoomGetter: (() -> QRtcLiveRoom) = {
        mRtcRoom
    }
}