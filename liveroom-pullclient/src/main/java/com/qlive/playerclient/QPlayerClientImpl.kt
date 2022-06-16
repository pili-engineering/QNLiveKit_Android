package com.qlive.playerclient

import com.qlive.avparam.QPlayerLiveProvider
import com.qlive.qplayer.QPLEngine
import com.qlive.rtm.RtmManager
import com.qlive.rtm.joinChannel
import com.qlive.rtm.leaveChannel
import com.qlive.avparam.QIPlayer
import com.qlive.avparam.QPlayerEventListener
import com.qlive.avparam.QPlayerRenderView
import com.qlive.coreimpl.AppCache
import com.qlive.coreimpl.QNLiveRoomContext
import com.qlive.core.*
import com.qlive.core.QClientType
import com.qlive.coreimpl.backGround
import com.qlive.core.QLiveService
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.roomservice.QRoomService

class QPlayerClientImpl : QPlayerClient, QPlayerLiveProvider {
    companion object {
        fun create(): QPlayerClient {
            return QPlayerClientImpl()
        }
    }

    private val mQPLEngine by lazy { QPLEngine(AppCache.appContext) }
    private val mRoomSource = com.qlive.coreimpl.RoomDataSource()
    private var renderView: QPlayerRenderView? = null
    private var mLiveStatusListener: QLiveStatusListener? = null
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
                mQNLiveRoomContext.enter(liveId, com.qlive.coreimpl.UserDataSource.loginUser)
                val roomInfo = mRoomSource.joinRoom(liveId)

                if (RtmManager.isInit) {
                    RtmManager.rtmClient.joinChannel(roomInfo.chatID)
                }
                mQNLiveRoomContext.joinedRoom(roomInfo)
                mQPLEngine.setUp(roomInfo.rtmpURL, null)
                if (renderView != null && getService(QRoomService::class.java)?.roomInfo != null) {
                    mQPLEngine.start()
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
                mRoomSource.leaveRoom(mQNLiveRoomContext.roomInfo?.liveID ?: "")
                if (RtmManager.isInit) {
                    RtmManager.rtmClient.leaveChannel(mQNLiveRoomContext.roomInfo?.chatID ?: "")
                }
                mQNLiveRoomContext.leaveRoom()
                mQPLEngine.stop()
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun destroy() {
        mQPLEngine.release()
        mQNLiveRoomContext.destroy()
    }

    override fun play(renderView: QPlayerRenderView) {
        this.renderView = renderView
        mQPLEngine.setPlayerRenderView(renderView)
        if (getService(QRoomService::class.java)?.roomInfo != null) {
            mQPLEngine.start()
        }
    }

    override fun setPlayerEventListener(playerEventListener: QPlayerEventListener) {
        mQPLEngine.setPlayerEventListener(playerEventListener)
    }

    override fun getClientType(): QClientType {
        return QClientType.PLAYER
    }

    override var playerGetter: (() -> QIPlayer) = {
        mQPLEngine
    }
}