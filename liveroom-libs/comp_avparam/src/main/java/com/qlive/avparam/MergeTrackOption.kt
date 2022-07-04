package com.qlive.avparam


/**
 * 混流画布参数
 */
class QMixStreamParams {
    var mixStreamWidth: Int = 0
    var mixStringHeight: Int = 0
    var mixBitrate: Int = 3420 * 1000
    var FPS: Int = 25
    var backGroundImg: QTranscodingLiveStreamingImage? = null
}

/**
 * 背景图片
 */
public class QTranscodingLiveStreamingImage {
    var url: String? = null
    var x = 0
    var y = 0
    var width = 0
    var height = 0
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