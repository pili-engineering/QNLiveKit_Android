package com.qlive.pushclient

import com.qlive.rtm.RtmManager
import com.qlive.rtm.joinChannel
import com.qlive.rtm.leaveChannel
import com.qlive.rtclive.*
import com.qiniu.droid.rtc.*
import com.qlive.avparam.*
import com.qlive.coreimpl.AppCache
import com.qlive.coreimpl.QNLiveRoomContext
import com.qlive.coreimpl.util.backGround
import com.qlive.core.*
import com.qlive.core.QClientType
import com.qlive.core.QLiveService
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.coreimpl.datesource.RoomDataSource
import com.qlive.coreimpl.datesource.UserDataSource
import com.qlive.coreimpl.util.getCode

class QPusherClientImpl : QPusherClient, QRTCProvider {

    companion object {
        fun create(): QPusherClient {
            return QPusherClientImpl()
        }
    }

    private val mRoomSource = RoomDataSource()
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
                mQNLiveRoomContext.enter(roomId, UserDataSource.loginUser)
                //业务接口发布房间
                val roomInfo = mRoomSource.pubRoom(roomId)
                //加群
                if (RtmManager.isInit) {
                    RtmManager.rtmClient.joinChannel(roomInfo.chatID)
                }
                //初始化混流器
                if (!mRtcRoom.mMixStreamManager.isInit) {
                    mRtcRoom.mMixStreamManager.init(
                        roomId,
                        roomInfo.pushURL,
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
                //加入rtc房间
                mRtcRoom.joinRtc(roomInfo.roomToken, "")
                //开始发布本地轨道
                mRtcRoom.publishLocal()
                //开始单路转推
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
                mRoomSource.unPubRoom(mQNLiveRoomContext.roomInfo?.liveID ?: "")
                if (RtmManager.isInit) {
                    RtmManager.rtmClient.leaveChannel(mQNLiveRoomContext.roomInfo?.chatID ?: "")
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