package com.qlive.linkmicservice;


import com.qlive.avparam.QMergeOption;
import com.qlive.avparam.QMixStreamParams;
import com.qlive.core.been.QMicLinker;
import com.qlive.core.been.QPKSession;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 主播端连麦器
 */
public interface QAnchorHostMicHandler {
    /**
     * 混流适配器
     */
    public static interface QMixStreamAdapter {

        /**
         * 连麦开始如果要自定义混流画布和背景
         * 返回空则主播推流分辨率有多大就多大默认实现
         *
         * @return QMixStreamParams
         */
        QMixStreamParams onMixStreamStart();

        /**
         * 混流布局适配
         *
         * @param micLinkers 变化后所有连麦者
         * @param target     当前变化的连麦者
         * @param isJoin     当前变化的连麦者是新加还是离开
         * @return 返回重设后的每个连麦者的混流布局
         */
        List<QMergeOption> onResetMixParam(List<QMicLinker> micLinkers, QMicLinker target, boolean isJoin);
    }

    /**
     * 设置混流适配器
     *
     * @param mixStreamAdapter  混流适配器
     */
    public void setMixStreamAdapter(@NotNull QMixStreamAdapter mixStreamAdapter);
}
