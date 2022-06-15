package com.qncube.lcommon

import com.qncube.lcommon.QIPlayer

interface QPlayerLiveProvider {
    var playerGetter: (() -> QIPlayer)
}