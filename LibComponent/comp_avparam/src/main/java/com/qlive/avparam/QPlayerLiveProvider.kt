package com.qlive.avparam

interface QPlayerLiveProvider {
    var playerGetter: (() -> QIPlayer)
}