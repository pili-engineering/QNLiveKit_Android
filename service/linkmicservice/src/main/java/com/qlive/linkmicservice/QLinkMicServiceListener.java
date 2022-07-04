package com.qlive.linkmicservice;

import com.qlive.core.been.QExtension;
import com.qlive.core.been.QMicLinker;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 麦位监听
 */
public interface QLinkMicServiceListener {

    /**
     * 有人上麦
     *
     * @param micLinker
     */
    void onLinkerJoin(QMicLinker micLinker);

    /**
     * 有人下麦
     *
     * @param micLinker
     */
    void onLinkerLeft(@NotNull QMicLinker micLinker);

    /**
     * 有人麦克风变化
     *
     * @param micLinker
     */
    void onLinkerMicrophoneStatusChange(@NotNull QMicLinker micLinker);

    /**
     * 有人摄像头状态变化
     *
     * @param micLinker
     */
    void onLinkerCameraStatusChange(@NotNull QMicLinker micLinker);

    /**
     * 有人被踢
     *
     * @param micLinker
     * @param msg 自定义扩展消息
     */
    void onLinkerKicked(@NotNull QMicLinker micLinker, String msg);

    /**
     * 有人扩展字段变化
     *
     * @param micLinker
     * @param QExtension
     */
    void onLinkerExtensionUpdate(@NotNull QMicLinker micLinker, QExtension QExtension);
}