package com.qlive.shoppingservice

import com.qlive.core.QLiveCallBack
import com.qlive.core.been.QExtension
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.coreimpl.BaseService
import com.qlive.coreimpl.Scheduler
import com.qlive.coreimpl.util.backGround
import com.qlive.coreimpl.util.getCode
import com.qlive.rtm.RtmManager
import com.qlive.rtm.msg.RtmTextMsg
import com.qlive.rtm.sendChannelMsg

class QShoppingServiceImpl : BaseService(), QShoppingService {
    private val mShoppingDataSource = ShoppingDataSource()
    private var mExplaining: QItem? = null
    private val mShoppingServiceListeners = ArrayList<QShoppingServiceListener>()
    override fun getItemList(callBack: QLiveCallBack<List<QItem>>?) {
        backGround {
            doWork {
                val ret = mShoppingDataSource.getItemList(currentRoomInfo?.liveID ?: "")
                callBack?.onSuccess(ret)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    /**
     * 正在展示的商品 轮训同步状态
     */
    private val mItemShader = Scheduler(5000) {
        if (currentRoomInfo == null) {
            return@Scheduler
        }
        backGround {
            doWork {
                val newItem = mShoppingDataSource.getExplaining(currentRoomInfo?.liveID ?: "")
                if (newItem.itemID.isNotEmpty()) {
                    if (mExplaining == null || newItem.itemID != mExplaining?.itemID) {
                        mExplaining = newItem;
                        mShoppingServiceListeners.forEach {
                            it.onExplainingUpdate(mExplaining)
                        }
                        return@doWork
                    }
                } else {
                    if (mExplaining != null) {
                        mExplaining = null;
                        mShoppingServiceListeners.forEach {
                            it.onExplainingUpdate(null)
                        }
                    }
                }
            }
            catchError {

            }
        }
    }

    override fun onJoined(roomInfo: QLiveRoomInfo) {
        super.onJoined(roomInfo)
        mItemShader.start()
    }

    override fun onLeft() {
        super.onLeft()
        mItemShader.cancel()
    }

    override fun updateItemStatus(itemID: String, status: QItemStatus, callBack: QLiveCallBack<Void>?) {
        backGround {
            doWork {
                mShoppingDataSource.updateStatus(
                    currentRoomInfo?.liveID ?: "",
                    HashMap<String, Int>().apply {
                        put(itemID, status.value)
                    })
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun updateItemExtension(
        item: QItem,
        extension: QExtension,
        callBack: QLiveCallBack<Void>?
    ) {
        backGround {
            doWork {
                mShoppingDataSource.updateItemExtension(
                    currentRoomInfo?.liveID ?: "", item.itemID, extension
                )
                val msg = QItemExtMsg()
                msg.item = item
                msg.extension = extension
                RtmManager.rtmClient.sendChannelMsg(
                    RtmTextMsg<QItemExtMsg>("liveroom_shopping_extension", msg).toJsonString(),
                    currentRoomInfo?.chatID ?: "",
                    true
                )
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun setExplaining(item: QItem, callBack: QLiveCallBack<Void>?) {
        backGround {
            doWork {
                mShoppingDataSource.setExplaining(
                    currentRoomInfo?.liveID ?: "", item.itemID
                )
                try {
                    RtmManager.rtmClient.sendChannelMsg(
                        RtmTextMsg<QItem?>("liveroom_shopping_explaining", item).toJsonString(),
                        currentRoomInfo?.chatID ?: "",
                        false
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                mExplaining = item
                mShoppingServiceListeners.forEach {
                    it.onExplainingUpdate(mExplaining)
                }
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun cancelExplaining(callBack: QLiveCallBack<Void>?) {
        backGround {
            doWork {
                mShoppingDataSource.cancelExplaining(
                    currentRoomInfo?.liveID ?: "", explaining?.itemID ?: ""
                )
                try {
                    RtmManager.rtmClient.sendChannelMsg(
                        RtmTextMsg<QItem?>("liveroom_shopping_explaining", null).toJsonString(),
                        currentRoomInfo?.chatID ?: "",
                        false
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                mExplaining = null
                mShoppingServiceListeners.forEach {
                    it.onExplainingUpdate(null)
                }
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun getExplaining(): QItem? {
        return mExplaining
    }

    override fun changeSingleOrder(param: QSingleOrderParam, callBack: QLiveCallBack<Void>?) {
        backGround {
            doWork {
                mShoppingDataSource.changeSingleOrder(
                    currentRoomInfo?.liveID ?: "",
                    param.itemID, param.from, param.to
                )
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun changeOrder(params: MutableList<QOrderParam>, callBack: QLiveCallBack<Void>?) {
        backGround {
            doWork {
                mShoppingDataSource.changeOrder(
                    currentRoomInfo?.liveID ?: "",
                    HashMap<String, Int>().apply {
                        params.forEach { p ->
                            put(p.itemID, p.order)
                        }
                    }
                )
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun deleteItems(itemIDS: MutableList<String>, callBack: QLiveCallBack<Void>?) {
        backGround {
            doWork {
                mShoppingDataSource.deleteItems(
                    currentRoomInfo?.liveID ?: "",
                    itemIDS
                )
                callBack?.onSuccess(null)
            }
            catchError {
                callBack?.onError(it.getCode(), it.message)
            }
        }
    }

    override fun addServiceListener(listener: QShoppingServiceListener) {
        mShoppingServiceListeners.add(listener)
    }

    override fun removeServiceListener(listener: QShoppingServiceListener) {
        mShoppingServiceListeners.remove(listener)
    }
}