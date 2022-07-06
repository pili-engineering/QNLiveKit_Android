package com.qlive.pkservice;

import com.qlive.rtclive.QPushRenderView;
import com.qlive.core.QInvitationHandler;
import com.qlive.core.been.QExtension;
import com.qlive.core.QLiveCallBack;
import com.qlive.core.QLiveService;
import com.qlive.core.been.QPKSession;

import java.util.HashMap;

/**
 * pk服务
 */
public interface QPKService extends QLiveService {

    /**
     * 主播设置混流适配器
     * @param adapter  混流适配
     */
    void setPKMixStreamAdapter(QPKMixStreamAdapter adapter);

    /**
     * 添加pk监听
     * @param QPKServiceListener
     */
    void addServiceListener(QPKServiceListener QPKServiceListener);

    /**
     * 移除pk监听
     * @param QPKServiceListener
     */
    void removeServiceListener(QPKServiceListener QPKServiceListener);

    /**
     * 开始pk
     *
     * @param timeoutTimestamp 等待对方流超时时间时间戳 毫秒
     * @param receiverRoomID   接受方所在房间ID
     * @param receiverUID      接收方用户ID
     * @param extension        扩展字段
     * @param callBack         操作回调函数
     */
    void start(long timeoutTimestamp, String receiverRoomID, String receiverUID, HashMap<String, String> extension, QLiveCallBack<QPKSession> callBack);

    /**
     * 结束pk
     * @param callBack 操作回调
     */
    void stop(QLiveCallBack<Void> callBack);

    /**
     * 主播设置对方的连麦预览
     * @param view  预览窗口
     */
    void setPeerAnchorPreView( QPushRenderView view);

    /**
     * 获得pk邀请处理
     * @return
     */
    QInvitationHandler getInvitationHandler();
    /**
     * 当前正在pk信息 没有PK则空
     */
    QPKSession currentPKingSession();

}
