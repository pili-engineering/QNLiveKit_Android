package com.qncube.roomservice

import com.niucube.rtm.RtmManager
import com.niucube.rtm.sendChannelMsg
import com.qiniu.jsonutil.JsonUtils
import com.qncube.linveroominner.LiveIdExtensionMode
import com.qncube.linveroominner.RoomDataSource
import com.qncube.linveroominner.UserDataSource
import com.qncube.linveroominner.backGround
import com.qncube.liveroomcore.*
import com.qncube.liveroomcore.been.QExtension
import com.qncube.liveroomcore.service.BaseService
import com.qncube.liveroomcore.been.QLiveRoomInfo
import com.qncube.liveroomcore.been.QLiveUser
import java.lang.Exception
import java.util.*

class QRoomServiceImpl : BaseService(), QRoomService {

    private val roomDataSource = RoomDataSource()
    private val userDataSource = UserDataSource()

    private val mQRoomServiceListeners = LinkedList<QRoomServiceListener>()

    override fun addRoomServiceListener(listener: QRoomServiceListener) {
        mQRoomServiceListeners.add(listener)
    }

    override fun removeRoomServiceListener(listener: QRoomServiceListener) {
        mQRoomServiceListeners.remove(listener)
    }

    /**
     * 获取当前房间
     *
     * @return
     */
    override fun getRoomInfo(): QLiveRoomInfo? {
        return currentRoomInfo
    }

    /**
     * 刷新房间信息
     */
    override fun getRoomInfo(callBack: QLiveCallBack<QLiveRoomInfo>?) {
        backGround {
            doWork {
                val room = roomDataSource.refreshRoomInfo(currentRoomInfo?.liveId ?: "")
                currentRoomInfo = room
                callBack?.onSuccess(currentRoomInfo)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    /**
     * 跟新直播扩展信息
     *
     * @param extension
     */
    override fun updateExtension(extension: QExtension, callBack: QLiveCallBack<Void>?) {
        backGround {
            doWork {
                roomDataSource.updateRoomExtension(currentRoomInfo?.liveId ?: "", extension)
                callBack?.onSuccess(null)
                try {
                    val mode = LiveIdExtensionMode()
                    mode.liveId = currentRoomInfo?.liveId ?: ""
                    mode.extension = extension
                    RtmManager.rtmClient.sendChannelMsg(
                        JsonUtils.toJson(mode),
                        currentRoomInfo?.chatId ?: "",
                        true
                    )
                } catch (e: Exception) {
                }
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    /**
     * 当前房间在线用户
     *
     * @param callBack
     */
    override fun getOnlineUser(
        page_num: Int,
        page_size: Int,
        callBack: QLiveCallBack<MutableList<QLiveUser>>?
    ) {
        backGround {
            doWork {
                val users =
                    userDataSource.getOnlineUser(currentRoomInfo?.liveId ?: "", page_num, page_size)
                callBack?.onSuccess(users.list)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    /**
     * 某个房间在线用户
     *
     * @param callBack
     */
    override fun getOnlineUser(
        pageNum: Int,
        pageSize: Int,
        roomId: String,
        callBack: QLiveCallBack<MutableList<QLiveUser>>?
    ) {

        backGround {
            doWork {
                val users =
                    userDataSource.getOnlineUser(roomId, pageNum, pageSize)
                callBack?.onSuccess(users.list)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    /**
     * 使用用户ID搜索房间用户
     *
     * @param uid
     * @param callBack
     */
    override fun searchUserByUserId(uid: String, callBack: QLiveCallBack<QLiveUser>?) {
        backGround {
            doWork {
                val users =
                    userDataSource.searchUserByUserId(uid)
                callBack?.onSuccess(users)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    /**
     * 使用用户im uid 搜索用户
     * @param imUid
     * @param callBack
     */
    override fun searchUserByIMUid(imUid: String, callBack: QLiveCallBack<QLiveUser>?) {
        backGround {
            doWork {
                val users =
                    userDataSource.searchUserByIMUid(imUid)
                callBack?.onSuccess(users)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }
}