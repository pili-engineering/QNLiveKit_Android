package com.qlive.rtclive

import android.util.Log
import android.widget.Toast
import com.qlive.rtclive.rtc.SimpleQNRTCListener
import com.qiniu.droid.rtc.*
import com.qlive.avparam.*

enum class MixType(var isStart: Boolean) {
    mix(false), forward(false), pk(false)
}

class MixStreamManager(val mQRtcLiveRoom: QRtcLiveRoom) {
    val lastUserMergeOp = HashMap<String, QMixStreaming.MergeOption>()
    private var localVideoTrack: QNLocalVideoTrack? = null
    private var localAudioTrack: QNLocalAudioTrack? = null
    var mQMixStreamParams: QMixStreaming.MixStreamParams? = null
        private set
    private val tracksMap = HashMap<String, QMixStreaming.TrackMergeOption>()
    private val toDoAudioMergeOptionsMap = HashMap<String, QMixStreaming.TrackMergeOption>()
    private val toDoVideoMergeOptionsMap = HashMap<String, QMixStreaming.TrackMergeOption>()
    private var streamId = ""
    private var pushUrl = ""
    private var serialnum = 1
    private val mEngine by lazy { mQRtcLiveRoom.mClient }

    //混流任务
    var mQNMergeJob: QNTranscodingLiveStreamingConfig? = null
        private set

    //单路转推任务
    var mQNForwardJob: QNDirectLiveStreamingConfig? = null
        private set

    var mPKMergeJob: QNTranscodingLiveStreamingConfig? = null
        private set

    var mMixType = MixType.forward
        private set

    //房间人数
    var roomUser = 0

    var isInit = false
    fun setTrack(videoTrack: QNLocalVideoTrack?, audioTrack: QNLocalAudioTrack?) {
        this.localVideoTrack = videoTrack
        this.localAudioTrack = audioTrack
    }

    fun init(
        streamId: String,
        pushUrl: String,
        QMixStreamParams: QMixStreaming.MixStreamParams
    ) {
        isInit = true
        this.mQMixStreamParams = QMixStreamParams
        this.pushUrl = pushUrl
        this.streamId = streamId
        mEngine.setLiveStreamingListener(object : QNLiveStreamingListener {
            override fun onStarted(streamID: String) {
                Log.d("MixStreamHelperImp", "MixStreamHelperImp onStarted ${mMixType.name}")
                // 转推任务创建成功时触发此回调

                mMixType.isStart = true
                if (mMixType == MixType.forward) {
                    stopPKMixStreamJob()
                    stopMixStreamJob()
                }

                if (mMixType == MixType.mix) {
                    stopForwardJob()
                    stopPKMixStreamJob()
                    commitOpt()
                }

                if (mMixType == MixType.pk) {
                    stopForwardJob()
                    stopMixStreamJob()
                    commitOpt()
                }
            }

            override fun onStopped(streamID: String) {
                // 转推任务成功停止时触发此回调
            }

            override fun onTranscodingTracksUpdated(streamID: String) {
                // 合流布局更新成功时触发此回调
            }

            override fun onError(streamID: String, errorInfo: QNLiveStreamingErrorInfo) {
                // 转推任务出错时触发此回调
                Log.d(
                    "MixStreamHelperImp",
                    "MixStreamHelperImp onError  ${mMixType.name}" + errorInfo.message + "  " + errorInfo.code
                )
            }
        })
        mQRtcLiveRoom.addExtraQNRTCEngineEventListenerToHead(object : SimpleQNRTCListener {
            override fun onUserPublished(p0: String, p1: MutableList<QNRemoteTrack>) {
                super.onUserPublished(p0, p1)
                onPublished(p0, p1)
            }

            override fun onLocalPublished(var1: String, var2: List<QNLocalTrack>) {
                super.onLocalPublished(var1, var2)
                onPublished(var1, var2)
            }

            override fun onUserUnpublished(p0: String, p1: MutableList<QNRemoteTrack>) {
                super.onUserUnpublished(p0, p1)
                p1.forEach {
                    tracksMap.remove(it.trackID)
                }
            }

            override fun onLocalUnpublished(var1: String, var2: List<QNLocalTrack>) {
                super.onLocalUnpublished(var1, var2)
                var2.forEach {
                    tracksMap.remove(it.trackID)
                }
            }

            private fun onPublished(p0: String, p1: List<QNTrack>) {
                p1.forEach { track ->
                    when (track.tag) {
                        QRtcLiveRoom.TAG_AUDIO -> {
                            toDoAudioMergeOptionsMap[p0]?.let {
                                tracksMap[track.trackID] = it
                                commitOpt()
                            }
                            toDoAudioMergeOptionsMap.remove(p0)
                        }

                        QRtcLiveRoom.TAG_CAMERA -> {
                            toDoVideoMergeOptionsMap[p0]?.let {
                                tracksMap[track.trackID] = it
                                commitOpt()
                            }
                            toDoVideoMergeOptionsMap.remove(p0)
                        }
                    }
                }
            }

            override fun onUserJoined(p0: String, p1: String?) {
                super.onUserJoined(p0, p1)
            }

            override fun onUserLeft(p0: String) {
                if (mQRtcLiveRoom.meId == p0) {
                    onRoomLeft()
                }
            }
        })
    }


    private fun createDefaultMeOp(): QNTranscodingLiveStreamingConfig {
        return QNTranscodingLiveStreamingConfig().apply {
            streamID = streamId + "?serialnum=${serialnum++}";// 设置 stream id，该 id 为合流任务的唯一标识符
            url = pushUrl + "?serialnum=${serialnum++}"; // 设置合流任务的推流地址
            Log.d("MixStreamHelperImp", "createMergeJob${url} ")
            width = mQMixStreamParams!!.mixStreamWidth; // 设置合流画布的宽度
            height = mQMixStreamParams!!.mixStringHeight; // 设置合流画布的高度
            videoFrameRate = mQMixStreamParams!!.FPS; // 设置合流任务的视频帧率
            bitrate = mQMixStreamParams!!.mixBitrate; // 设置合流任务的码率，单位: kbps
        }
    }

    private fun createMeop(mixStreamParams: QMixStreaming.MixStreamParams): QNTranscodingLiveStreamingConfig {
        return QNTranscodingLiveStreamingConfig().apply {
            streamID = streamId + "?serialnum=${serialnum++}";// 设置 stream id，该 id 为合流任务的唯一标识符
            url = pushUrl + "?serialnum=${serialnum++}"; // 设置合流任务的推流地址
            Log.d("MixStreamHelperImp", "createMergeJob${url} ")
            width = mixStreamParams.mixStreamWidth; // 设置合流画布的宽度
            height = mixStreamParams.mixStringHeight; // 设置合流画布的高度
            videoFrameRate = mixStreamParams.FPS; // 设置合流任务的视频帧率
//            setRenderMode(QNRenderMode.ASPECT_FILL); // 设置合流任务的默认画面填充方式
            bitrate = mixStreamParams.mixBitrate; // 设置合流任务的码率，单位: kbps
            mixStreamParams.backGroundImg?.let { bg ->
                background = QNTranscodingLiveStreamingImage().apply {
                    this.url = bg.url
                    this.height = bg.height
                    this.width = bg.width
                    this.x = x
                    this.x = y
                }
            }
        }
    }

    //创建前台转推
    private fun createForwardJob() {
        val mDirectLiveStreamingConfig = QNDirectLiveStreamingConfig()
        mDirectLiveStreamingConfig.streamID = streamId
        mDirectLiveStreamingConfig.url = pushUrl + "?serialnum=${serialnum++}"
        Log.d("MixStreamHelperImp", "createForwardJob ${mDirectLiveStreamingConfig.url}")
        localAudioTrack?.let {
            mDirectLiveStreamingConfig.audioTrack = it
        }
        localVideoTrack?.let {
            mDirectLiveStreamingConfig.videoTrack = it
        }
        mQNForwardJob = mDirectLiveStreamingConfig

        Log.d("MixStreamHelperImp", "createForwardJob ")
    }


    /**
     * 启动前台转推 默认实现推本地轨道
     */
    fun startForwardJob() {
        lastUserMergeOp.clear()
        mMixType = MixType.forward
        mMixType.isStart = false
        createForwardJob()
        Log.d("MixStreamHelperImp", "startForwardJob ")
        mEngine.startLiveStreaming(mQNForwardJob);
    }

    /**
     * 停止前台推流
     */
    private fun stopForwardJob() {
        Log.d("MixStreamHelperImp", "stopForwardJob ")
        mQNForwardJob?.let {
            mEngine.stopLiveStreaming(mQNForwardJob)
        }
        mQNForwardJob = null
    }

    /**
     * 开始混流转推
     */
    fun startMixStreamJob(mixStreamParams: QMixStreaming.MixStreamParams?) {
        mMixType = MixType.mix
        mMixType.isStart = false
        Log.d("MixStreamHelperImp", "startMixStreamJob ")
        clear()
        if (mixStreamParams != null) {
            mQNMergeJob = createMeop(mixStreamParams)
        } else {
            mQNMergeJob = createDefaultMeOp()
        }
        mEngine.startLiveStreaming(mQNMergeJob)
    }

    private fun clear() {
        Log.d("MixStreamHelperImp", "clear ")
        tracksMap.clear()
        toDoAudioMergeOptionsMap.clear()
        toDoVideoMergeOptionsMap.clear()
    }

    /**
     * 启动新的混流任务
     */
    fun startPkMixStreamJob(mixStreamParams: QMixStreaming.MixStreamParams?) {
        Log.d("MixStreamHelperImp", "startPkMixStreamJob ")
        clear()
        mMixType = MixType.pk
        mMixType.isStart = false
        if (mixStreamParams != null) {
            mPKMergeJob = createMeop(mixStreamParams)
        } else {
            mPKMergeJob = createDefaultMeOp()
        }
        mEngine.startLiveStreaming(mPKMergeJob);
    }


    private fun stopMixStreamJob() {
        Log.d("MixStreamHelperImp", "stopMixStreamJob ")

        mQNMergeJob?.let {
            mEngine.stopLiveStreaming(it)
        }
        mQNMergeJob = null
    }

    private fun stopPKMixStreamJob() {
        Log.d("MixStreamHelperImp", "stopPKMixStreamJob ")

        mPKMergeJob?.let {
            mEngine.stopLiveStreaming(it)
        }
        mPKMergeJob = null
    }

    fun updateUserVideoMergeOptions(
        uid: String,
        option: QMixStreaming.CameraMergeOption?, commitNow: Boolean
    ) {
        val trackId = mQRtcLiveRoom.getUserVideoTrackInfo(uid)?.trackID
        if (trackId == null) {
            if (option != null) {
                toDoVideoMergeOptionsMap[uid] = option
            } else {
                toDoVideoMergeOptionsMap.remove(uid)
            }
        } else {
            if (option != null) {
                tracksMap[trackId] = option
            } else {
                mEngine.removeTranscodingLiveStreamingTracks(
                    mQNMergeJob!!.streamID,
                    listOf(QNTranscodingLiveStreamingTrack().apply {
                        this.trackID = trackId
                    })
                )
                tracksMap.remove(trackId)
            }
            if (commitNow) {
                commitOpt()
            }
        }
    }

    fun updateUserAudioMergeOptions(
        uid: String,
        op: QMixStreaming.MicrophoneMergeOption, commitNow: Boolean
    ) {
        val trackId = mQRtcLiveRoom.getUserAudioTrackInfo(uid)?.trackID
        if (trackId == null) {
            if (op.isNeed) {
                toDoAudioMergeOptionsMap[uid] = op
            } else {
                toDoAudioMergeOptionsMap.remove(uid)
            }
        } else {
            if (op.isNeed) {
                tracksMap[trackId] = op
            } else {
                mEngine.removeTranscodingLiveStreamingTracks(
                    mQNMergeJob!!.streamID,
                    listOf(QNTranscodingLiveStreamingTrack().apply {
                        this.trackID = trackId
                    })
                )
                tracksMap.remove(trackId)
            }
            if (commitNow) {
                commitOpt()
            }
        }
    }

    fun commitOpt() {
//        Log.d(
//            "MixStreamHelperImp",
//            "commitOpt fab发布混流参数  mMixType${mMixType.name} ${mMixType.isStart}\n " +
//                    "${toDoAudioMergeOptionsMap.size}\n" +
//                    "${toDoAudioMergeOptionsMap.size}\n" +
//                    "${tracksMap.size}\n" +
//                    ""
//        )
        if (mMixType != MixType.forward && mMixType.isStart) {
//            Log.d(
//                "MixStreamHelperImp",
//                "commitOpt fab发布混流参数  开始"
//            )
            val mMergeTrackOptions = ArrayList<QNTranscodingLiveStreamingTrack>()
            val sb = StringBuffer("")
            tracksMap.entries.forEach {
                val key = it.key
                val op = it.value
                if (op is QMixStreaming.CameraMergeOption) {
                    val trackOp = QNTranscodingLiveStreamingTrack().apply {
                        trackID = key
                        x = op.x
                        y = op.y
                        zOrder = op.z
                        width = op.width
                        height = op.height
                        // renderMode = op.stretchMode
                    }
                    mMergeTrackOptions.add(trackOp)
                    sb.append("${key} CameraMergeOption" + trackOp.toJsonObject().toString())
                }
                if (op is QMixStreaming.MicrophoneMergeOption) {
                    val opTrack = QNTranscodingLiveStreamingTrack().apply {
                        trackID = key
                    }
                    mMergeTrackOptions.add(opTrack)
                    sb.append("${key} MicrophoneMergeOption" + opTrack.toJsonObject().toString())
                }
            }
            Log.d(
                "MixStreamHelperImp",
                "commitOpt fab发布混流参数  ${sb.toString()}"
            )
            val id = if (mMixType == MixType.mix) {
                mQNMergeJob?.streamID ?: ""
            } else {
                mPKMergeJob?.streamID ?: ""
            }
            mEngine.setTranscodingLiveStreamingTracks(
                id,
                mMergeTrackOptions
            )
        }
    }

    private fun onRoomLeft() {
        mQNForwardJob = null
        mQNMergeJob = null
        tracksMap.clear()
        toDoAudioMergeOptionsMap.clear()
        toDoVideoMergeOptionsMap.clear()
    }
}