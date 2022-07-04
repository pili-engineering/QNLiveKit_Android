package com.qlive.linkmicservice;

import com.qlive.rtclive.QPushRenderView;
import com.qlive.core.QInvitationHandler;
import com.qlive.core.been.QExtension;
import com.qlive.core.QLiveCallBack;
import com.qlive.core.been.QMicLinker;
import com.qlive.core.QLiveService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 连麦服务
 */
public interface QLinkMicService extends QLiveService {

    /**
     * 获取当前房间所有连麦用户
     * @return
     */
    List<QMicLinker> getAllLinker();

    /**
     * 设置某人的连麦视频预览
     * 麦上用户调用  上麦后才会使用切换成rtc连麦 下麦后使用拉流预览
     * @param uid
     * @param preview
     */
    void setUserPreview(String uid, QPushRenderView preview);

    /**
     * 踢人
     * @param uid
     */
    void kickOutUser(String uid, String msg, QLiveCallBack<Void> callBack);

    /**
     * 跟新扩展字段
     * @param micLinker
     * @param QExtension
     */
    void updateExtension(@NotNull QMicLinker micLinker, QExtension QExtension, QLiveCallBack<Void> callBack);

    //添加监听
    void addMicLinkerListener(QLinkMicServiceListener listener);

    void removeMicLinkerListener(QLinkMicServiceListener listener);

    /**
     * 获得连麦邀请处理
     * @return
     */
    QInvitationHandler getInvitationHandler();

    /**
     * 观众向主播连麦
     * @return
     */
    QAudienceMicHandler getAudienceMicHandler();

    /**
     * 主播处理自己被连麦
     * @return
     */
    QAnchorHostMicHandler getAnchorHostMicHandler();
}
