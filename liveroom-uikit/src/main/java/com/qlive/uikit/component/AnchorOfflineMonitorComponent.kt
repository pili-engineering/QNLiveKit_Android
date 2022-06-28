package com.qlive.uikit.component

import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.qlive.core.QLiveClient
import com.qlive.core.QLiveStatus
import com.qlive.core.QLiveStatusListener
import com.qlive.core.been.QLiveRoomInfo
import com.qlive.core.been.QLiveUser
import com.qlive.uikitcore.BaseQLiveComponent
import com.qlive.uikitcore.QLiveComponent
import com.qlive.uikitcore.QLiveUIKitContext

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
            }
        })
    }

}