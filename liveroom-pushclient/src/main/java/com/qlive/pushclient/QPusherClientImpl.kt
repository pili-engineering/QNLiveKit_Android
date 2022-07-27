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
import com.qlive.core.QLiveService
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.coreimpl.datesource.RoomDataSource
import com.qlive.coreimpl.datesource.UserDataSource
import com.qlive.coreimpl.util.getCode
import com.qlive.liblog.QLiveLogUtil

class QPusherClientImpl : QPusherClient, QRTCProvider {

    companion object {
        fun create(): QPusherClient {
            val client = QPusherClientImpl()
            client.init()
            return client
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
    private val mLiveContext by lazy {
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
        return mLiveContext.getService(serviceClass)
    }

    override fun setLiveStatusListener(liveStatusListener: QLiveStatusListener?) {
        mLiveStatusListener = liveStatusListener
    }

    private fun init() {
        mLiveContext.checkInit()
    }

    /**
     * 加入房间
     * @param roomID
     * @param callBack
     */
    override fun joinRoom(roomID: String, callBack: QLiveCallBack<QLiveRoomInfo>?) {
        backGround {
            doWork {
                mLiveContext.enter(roomID, UserDataSource.loginUser)
                //业务接口发布房间
                val roomInfo = mRoomSource.pubRoom(roomID)
                //加群
                if (RtmManager.isInit) {
                    RtmManager.rtmClient.joinChannel(roomInfo.chatID)
                }
                //初始化混流器
                if (!mRtcRoom.mMixStreamManager.isInit) {
                    mRtcRoom.mMixStreamManager.init(
                        roomID,
                        roomInfo.pushURL,
                        QMixStreaming.MixStreamParams().apply {
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
                val startTime = System.currentTimeMillis()
                //加入rtc房间
                mRtcRoom.joinRtc(roomInfo.roomToken, "")
                QLiveLogUtil.d(
                    "mRtcRoom.joinRtc",
                    "join duration ${(System.currentTimeMillis() - startTime)}"
                )
                //开始发布本地轨道
                mRtcRoom.publishLocal()
                //开始单路转推
                mRtcRoom.mMixStreamManager.startForwardJob()
                mLiveContext.joinedRoom(roomInfo)

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
                mLiveContext.beforeLeaveRoom()
                mRoomSource.unPubRoom(mLiveContext.roomInfo?.liveID ?: "")
                if (RtmManager.isInit) {
                    RtmManager.rtmClient.leaveChannel(mLiveContext.roomInfo?.chatID ?: "")
                }
                mRtcRoom.leave()
                mLiveContext.leaveRoom()
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun destroy() {
        mLiveStatusListener = null
        mRtcRoom.close()
        mLiveContext.destroy()
    }

    override fun setConnectionStatusLister(connectionStatusLister: QConnectionStatusLister?) {
        mConnectionStatusLister = connectionStatusLister
    }

    override fun enableCamera(cameraParam: QCameraParam?, renderView: QPushRenderView) {
        cameraParam?.let { mCameraParams = it }
        this.mLocalPreView = renderView
        mRtcRoom.enableCamera(cameraParam ?: QCameraParam())
        mRtcRoom.setLocalPreView(mLocalPreView!!.getQNRender())
    }

    override fun enableMicrophone(microphoneParams: QMicrophoneParam?) {
        microphoneParams?.let { mQMicrophoneParam = it }
        mRtcRoom.enableMicrophone(microphoneParams ?: QMicrophoneParam())
    }

    override fun switchCamera(callBack: QLiveCallBack<QCameraFace>?) {
        mRtcRoom.switchCamera(){it,msg->
            if(it!==null){
                callBack?.onSuccess(if(it){QCameraFace.FRONT}else{QCameraFace.BACK})
            }else{
                callBack?.onError(-1,msg)
            }
        }
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

    override fun pause() {
        mRtcRoom.localVideoTrack?.stopCapture()
        mRtcRoom.localAudioTrack?.setVolume(0.0)
    }

    override fun resume() {
        mRtcRoom.localVideoTrack?.startCapture()
        mRtcRoom.localAudioTrack?.setVolume(1.0)
    }

    override fun setDefaultBeauty(beautySetting: QBeautySetting) {
        mRtcRoom.localVideoTrack?.setBeauty(beautySetting.toQNBeautySetting())
    }

//    override fun setVideoWaterMark(waterMark: QNVideoWaterMark) {
//        mRtcRoom.localVideoTrack?.setWaterMark(waterMark)
//    }

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