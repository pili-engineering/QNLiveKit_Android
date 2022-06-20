package com.qlive.uikitcore

import com.qlive.avparam.CameraMergeOption
import com.qlive.avparam.MicrophoneMergeOption
import com.qlive.avparam.QMergeOption
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser

/**
 * 混流参数
 */
object LinkerUIHelper {

    val mixWidth = 720
    val mixHeight = 1280

    /**
     * 混流每个麦位宽大小
     */
    var mixMicWidth = 184

    /**
     * 混流每个麦位高
     */
    var mixMicHeight = 184

    /**
     * 混流第一个麦位上间距
     */
    var mixTopMargin = 174
    /**
     * 混流参数 每个麦位间距
     */
    var micBottomMixMargin = 15
    /**
     * 混流参数 每个麦位右间距
     */
    var micRightMixMargin = 30*3


    var uiMicWidth = 0
    var uiMicHeight = 0
    var uiTopMargin = 0
    /**
     * 混流换算成屏幕 每个麦位的间距
     */
    var micBottomUIMargin = 0

    var micRightUIMargin = 0;
    //页面宽高
    var containerWidth = 0
    var containerHeight = 0

    fun attachUIWidth(w: Int, h: Int, centerCrop: Boolean = true) {
        containerWidth = w
        containerHeight = h

        val uiRatio = h.toDouble() / w
        val mixRatio = mixHeight.toDouble() / mixWidth

        var ratio = 0.0
        if (mixRatio > uiRatio) {
            //视频太高
            ratio = containerWidth.toDouble() / mixWidth

            uiMicWidth = (mixMicWidth * ratio).toInt()
            uiMicHeight = (mixMicHeight * ratio).toInt()
            uiTopMargin = (mixTopMargin * ratio - (mixHeight * ratio - containerHeight) / 2).toInt()
            micBottomUIMargin = (micBottomMixMargin * ratio).toInt()
            micRightUIMargin = (micRightMixMargin * ratio).toInt()

        } else {
            //视频太矮
            ratio = containerHeight.toDouble() / mixHeight

            uiMicWidth = (mixMicWidth * ratio).toInt()
            uiMicHeight = (mixMicHeight * ratio).toInt()
            uiTopMargin = (mixTopMargin * ratio).toInt()
            micBottomUIMargin = (micBottomMixMargin * ratio).toInt()
            micRightUIMargin = 0
        }
    }

    fun getLinkers(micLinkers : List<QLiveUser>, roomInfo: QLiveRoomInfo): ArrayList<QMergeOption> {
        val ops = ArrayList<QMergeOption>()
        var lastX =
            mixWidth - mixMicWidth - micRightMixMargin
        var lastY = mixTopMargin
        micLinkers.forEach {
            if (it.userId == roomInfo.anchor?.userId) {
                ops.add(QMergeOption().apply {
                    uid = it.userId
                    cameraMergeOption = CameraMergeOption().apply {
                        isNeed = true
                        x = 0
                        y = 0
                        z = 0
                        width = mixWidth
                        height = mixHeight
                        // mStretchMode=QNRenderMode.
                    }
                    microphoneMergeOption = MicrophoneMergeOption().apply {
                        isNeed = true
                    }
                })
            } else {
                ops.add(QMergeOption().apply {
                    uid = it.userId
                    cameraMergeOption = CameraMergeOption().apply {
                        isNeed = true
                        x = lastX
                        y = lastY
                        z = 1
                        width = mixMicWidth
                        height = mixMicHeight
                        // mStretchMode=QNRenderMode.
                    }
                    lastY += micBottomMixMargin + mixMicHeight
                    microphoneMergeOption = MicrophoneMergeOption().apply {
                        isNeed = true
                    }
                })
            }
        }
       return ops

    }

}