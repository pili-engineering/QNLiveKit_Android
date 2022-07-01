package com.qlive.sensebeautyservice

import com.qiniu.sensetimeplugin.QNSenseTimePlugin

class SenseBeautyServiceManager {
    companion object {
        var isEnable = true
        var sSenseTimePlugin: QNSenseTimePlugin? = null
        val sSubModelFromAssetsFile: ArrayList<String> = ArrayList<String>()
        val sSubModelFromFile: ArrayList<String> = ArrayList<String>()
    }
}