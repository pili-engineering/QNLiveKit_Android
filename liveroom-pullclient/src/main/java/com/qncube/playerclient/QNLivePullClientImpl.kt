package com.qncube.playerclient

import com.niucube.rtm.RtmManager
import com.niucube.rtm.joinChannel
import com.niucube.rtm.leaveChannel
import com.qncube.playerclient.player.PLEngine
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.datasource.RoomDataSource
import com.qncube.liveroomcore.mode.QLiveRoomInfo

class QNLivePullClientImpl : QNLivePullClient {

    private val mRoomSource = RoomDataSource()
    private var renderView: QRenderView? = null
    private var mLiveStatusListener: QLiveStatusListener? = null
    private val mQNLiveRoomContext by lazy { QNLiveRoomContext(this) }
    private val mPLEngine by lazy { PLEngine(AppCache.appContext) }

    init {
        mQNLiveRoomContext.mRoomScheduler.roomStatusChange = {
            mLiveStatusListener?.onLiveStatusChanged(it)
        }
    }

    /**
     * 获取服务实例
     *
     * @param serviceClass
     * @param <T>
     * @return
    </T> */
    override fun <T : QNLiveService> getService(serviceClass: Class<T>): T? {
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
                mQNLiveRoomContext.enter(liveId, QNLiveRoomEngine.getCurrentUserInfo())
                val roomInfo = mRoomSource.joinRoom(liveId)

                if (RtmManager.isInit) {
                    RtmManager.rtmClient.joinChannel(roomInfo.chatId)
                }
                mQNLiveRoomContext.joinedRoom(roomInfo)
                mPLEngine.setUpUrl(roomInfo.rtmpUrl, null)
                mPLEngine.start()
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
                mRoomSource.leaveRoom(mQNLiveRoomContext.roomInfo?.liveId ?: "")
                if (RtmManager.isInit) {
                    RtmManager.rtmClient.leaveChannel(mQNLiveRoomContext.roomInfo?.chatId ?: "")
                }
                mQNLiveRoomContext.leaveRoom()
                mPLEngine.stop()
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun destroy() {
        mQNLiveRoomContext.close()
    }

    override fun play(renderView: QRenderView) {
        this.renderView = renderView
    }

    override fun setPlayerEventListener(playerEventListener: QPlayerEventListener) {
        mPLEngine.setPlayerEventListener(playerEventListener)
    }

    override fun getClientType(): ClientType {
        return ClientType.PLAYER
    }

}