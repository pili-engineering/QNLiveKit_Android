package com.qlive.playerclient

import com.qlive.avparam.QPlayerProvider
import com.qlive.qplayer.QMediaPlayer
import com.qlive.rtm.RtmManager
import com.qlive.rtm.joinChannel
import com.qlive.rtm.leaveChannel
import com.qlive.avparam.QIPlayer
import com.qlive.avparam.QPlayerEventListener
import com.qlive.avparam.QPlayerRenderView
import com.qlive.coreimpl.AppCache
import com.qlive.coreimpl.QNLiveRoomContext
import com.qlive.core.*
import com.qlive.coreimpl.util.backGround
import com.qlive.core.QLiveService
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.coreimpl.datesource.RoomDataSource
import com.qlive.coreimpl.datesource.UserDataSource
import com.qlive.coreimpl.util.getCode

class QPlayerClientImpl : QPlayerClient, QPlayerProvider, QUserJoinObserver {
    companion object {
        fun create(): QPlayerClient {
            val client = QPlayerClientImpl()
            client.init()
            return client
        }
    }

    private val mQPlayerEventListenerWarp = QPlayerEventListenerWarp()
    private val mMediaPlayer by lazy {
        QMediaPlayer(AppCache.appContext).apply {
            setEventListener(mQPlayerEventListenerWarp)
        }
    }
    private val mRoomSource = RoomDataSource()
    private var mPlayerRenderView: QPlayerRenderView? = null
    private var mLiveStatusListener: QLiveStatusListener? = null
    private val mLiveContext by lazy {
        QNLiveRoomContext(this).apply {
            mRoomScheduler.roomStatusChange = {
                mLiveStatusListener?.onLiveStatusChanged(it)
            }
        }
    }

    private fun init() {
        mLiveContext.checkInit()
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
        this.mLiveStatusListener = liveStatusListener
    }

    /**
     * 加入房间
     * @param liveId
     * @param callBack
     */
    override fun joinRoom(liveId: String, callBack: QLiveCallBack<QLiveRoomInfo>?) {
        backGround {
            doWork {
                mLiveContext.enter(liveId, UserDataSource.loginUser)
                val roomInfo = mRoomSource.joinRoom(liveId)
                if (RtmManager.isInit) {
                    RtmManager.rtmClient.joinChannel(roomInfo.chatID)
                }
                mLiveContext.joinedRoom(roomInfo)
                //设置播放地址
                mMediaPlayer.setUp(roomInfo.rtmpURL, null)
                if (mPlayerRenderView != null) {
                    //开始拉流
                    mMediaPlayer.start()
                }
                callBack?.onSuccess(roomInfo)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    /**
     * 离开房间
     * @param callBack
     */
    override fun leaveRoom(callBack: QLiveCallBack<Void>?) {
        backGround {
            doWork {
                mLiveContext.beforeLeaveRoom()
                mRoomSource.leaveRoom(mLiveContext.roomInfo?.liveID ?: "")
                if (RtmManager.isInit) {
                    RtmManager.rtmClient.leaveChannel(mLiveContext.roomInfo?.chatID ?: "")
                }
                mLiveContext.leaveRoom()
                mMediaPlayer.stop()
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun destroy() {
        mLiveStatusListener = null
        mQPlayerEventListenerWarp.clear()
        mMediaPlayer.release()
        mLiveContext.destroy()
    }

    override fun play(renderView: QPlayerRenderView) {
        this.mPlayerRenderView = renderView
        mMediaPlayer.setPlayerRenderView(renderView)
        if (mLiveContext.roomInfo != null) {
            //已经加入了房间就播放
            mMediaPlayer.start()
        }
    }

    override fun pause() {
        mMediaPlayer.pause()
    }

    override fun resume() {
        mMediaPlayer.resume()
    }

    override fun addPlayerEventListener(playerEventListener: QPlayerEventListener) {
        mQPlayerEventListenerWarp.addEventListener(playerEventListener)
    }

    override fun removePlayerEventListener(playerEventListener: QPlayerEventListener) {
        mQPlayerEventListenerWarp.removeEventListener(playerEventListener)
    }

    override fun getClientType(): QClientType {
        return QClientType.PLAYER
    }

    override var playerGetter: (() -> QIPlayer) = {
        mMediaPlayer
    }

    override fun notifyUserJoin(userId: String) {
        if (mLiveContext.roomInfo?.anchor?.userId?.isEmpty() != false) {
            return
        }
        if (userId == mLiveContext.roomInfo?.anchor?.userId) {
            mLiveContext.mRoomScheduler.setAnchorStatus(1)
        }
    }

    override fun notifyUserLeft(userId: String) {
        if (mLiveContext.roomInfo?.anchor?.userId?.isEmpty() != false) {
            return
        }
        if (userId == mLiveContext.roomInfo?.anchor?.userId) {
            mLiveContext.mRoomScheduler.setAnchorStatus(0)
        }
    }
}