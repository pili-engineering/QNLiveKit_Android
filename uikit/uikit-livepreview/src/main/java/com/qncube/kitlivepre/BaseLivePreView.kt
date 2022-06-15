package com.qncube.kitlivepre

import com.qncube.liveroomcore.QCreateRoomParam
import com.qncube.liveroomcore.QLiveCallBack
import com.qncube.uikitcore.QLiveComponent

interface BaseLivePreView : QLiveComponent {
    var onStartCreateCall: ((param: QCreateRoomParam, call: QLiveCallBack<Void>) -> Unit)?
}