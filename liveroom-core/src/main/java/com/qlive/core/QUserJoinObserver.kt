package com.qlive.core

interface QUserJoinObserver {
    fun notifyUserJoin(userId:String)
    fun notifyUserLeft(userId:String)
}