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
     * 设置混流适配器
     * @param adapter
     */
    void setPKMixStreamAdapter(QPKMixStreamAdapter adapter);

    //添加监听
    void addServiceListener(QPKServiceListener QPKServiceListener);

    void removeServiceListener(QPKServiceListener QPKServiceListener);

    /**
     * 跟新扩展自定义字段
     *
     * @param QExtension
     */
    void updatePKExtension(QExtension QExtension, QLiveCallBack<Void> callBack);

    /**
     * 开始pk
     *
     * @param timeoutTimestamp 等待对方流超时时间时间戳 毫秒
     * @param receiverRoomID
     * @param receiverUID
     * @param extension
     * @param callBack
     */
    void start(long timeoutTimestamp, String receiverRoomID, String receiverUID, HashMap<String, String> extension, QLiveCallBack<QPKSession> callBack);

    //结束
    void stop(QLiveCallBack<Void> callBack);

    /**
     * 设置对方的连麦预览
     * @param view
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
