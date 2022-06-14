package com.qncube.playerclient

import com.niucube.rtm.RtmManager
import com.niucube.rtm.joinChannel
import com.niucube.rtm.leaveChannel
import com.qncube.lcommon.IPlayer
import com.qncube.lcommon.QPlayerEventListener
import com.qncube.lcommon.QPlayerRenderView
import com.qncube.lcommon.QRenderView
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.QClientType
import com.qncube.linveroominner.backGround
import com.qncube.liveroomcore.service.QLiveService
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.roomservice.QRoomService

class QPlayerClientImpl : QPlayerClient {

    private val mRoomSource = com.qncube.linveroominner.RoomDataSource()
    private var renderView: QPlayerRenderView? = null
    private var mLiveStatusListener: QLiveStatusListener? = null
    private val mQNLiveRoomContext by lazy { com.qncube.linveroominner.QNLiveRoomContext(this) }

    private val mPLEngine by lazy { PLEngine(com.qncube.linveroominner.AppCache.appContext) }

    //反射字段
    private var player: IPlayer? = null

    init {
        player = mPLEngine
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
                mQNLiveRoomContext.enter(liveId, com.qncube.linveroominner.UserDataSource.loginUser)
                val roomInfo = mRoomSource.joinRoom(liveId)

                if (RtmManager.isInit) {
                    RtmManager.rtmClient.joinChannel(roomInfo.chatId)
                }
                mQNLiveRoomContext.joinedRoom(roomInfo)
                mPLEngine.setUp(roomInfo.rtmpUrl, null)
                if (renderView != null && getService(QRoomService::class.java)?.currentRoomInfo != null) {
                    mPLEngine.start()
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
        mPLEngine.release()
        mQNLiveRoomContext.destroy()
    }

    override fun play(renderView: QPlayerRenderView) {
        this.renderView = renderView
        mPLEngine.setPlayerRenderView(renderView)
        if (getService(QRoomService::class.java)?.currentRoomInfo != null) {
            mPLEngine.start()
        }
    }

    override fun setPlayerEventListener(playerEventListener: QPlayerEventListener) {
        mPLEngine.setPlayerEventListener(playerEventListener)
    }

    override fun getClientType(): QClientType {
        return QClientType.PLAYER
    }

}