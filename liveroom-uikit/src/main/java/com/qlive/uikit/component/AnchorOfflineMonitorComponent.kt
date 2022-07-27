package com.qlive.uikit.component

import android.widget.Toast
import com.qlive.core.QLiveClient
import com.qlive.core.QLiveStatus
import com.qlive.core.QLiveStatusListener
import com.qlive.uikitcore.BaseQLiveComponent

/**
 * 房主离线事件处理功能组件
 */
class AnchorOfflineMonitorComponent : BaseQLiveComponent() {

    override fun attachLiveClient(client: QLiveClient) {
        client.setLiveStatusListener(object : QLiveStatusListener {
            override fun onLiveStatusChanged(liveStatus: QLiveStatus) {
                //如果房主离线 关闭页面
                if (liveStatus == QLiveStatus.ANCHOR_OFFLINE) {
                    Toast.makeText(kitContext?.androidContext, "房主离线", Toast.LENGTH_SHORT).show()
                    kitContext?.currentActivity?.finish()
                }
                if (liveStatus == QLiveStatus.OFF) {
                    Toast.makeText(kitContext?.androidContext, "房主销毁", Toast.LENGTH_SHORT).show()
                    kitContext?.currentActivity?.finish()
                }
            }
        })
    }
}