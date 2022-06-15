package com.qncube.lcommon


/**
 * 混流画布参数
 */
class QMixStreamParams {
    var mixStreamWidth: Int = 0
    var mixStringHeight: Int = 0
    var mixBitrate: Int = 3420 * 1000
    var FPS: Int = 25
   // var qnBackGround: QNTranscodingLiveStreamingImage? = null
   // var watermarks: List<QNTranscodingLiveStreamingImage>? = null
    constructor(
        mixStreamWidth: Int = 0,
        mixStringHeight: Int = 0,
        mixBitrate: Int = 3420 * 1000,
        fps: Int = 15,
      //  qnBackGround: QNTranscodingLiveStreamingImage? = null
    ) {
        this.mixStreamWidth = mixStreamWidth
        this.mixStringHeight = mixStringHeight
        this.mixBitrate = mixBitrate
        this.FPS = fps
      //  this.qnBackGround = qnBackGround
    }
    constructor()
}

interface TrackMergeOption {
}

class MicrophoneMergeOption : TrackMergeOption {
    var isNeed: Boolean = false
}

class CameraMergeOption : TrackMergeOption {
    var isNeed: Boolean = false
    var x = 0
    var y = 0
    var z = 0
    var width = 0
    var height = 0
   // var stretchMode: QNRenderMode? = null
}

class QMergeOption {
    var uid: String = ""
    var cameraMergeOption: CameraMergeOption = CameraMergeOption()
    var microphoneMergeOption: MicrophoneMergeOption = MicrophoneMergeOption()
}