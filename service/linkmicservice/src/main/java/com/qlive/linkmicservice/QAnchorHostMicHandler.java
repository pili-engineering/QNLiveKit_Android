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
         * @return
         */
        QMixStreamParams onMixStreamStart();

        /**
         * 混流布局适配
         *
         * @param micLinkers 所有连麦者
         * @return 返回重设后的每个连麦者的混流布局
         */
        List<QMergeOption> onResetMixParam(List<QMicLinker> micLinkers, QMicLinker target, boolean isJoin);
    }

    /**
     * 设置混流适配器
     *
     * @param QMixStreamAdapter
     */
    public void setMixStreamAdapter(QMixStreamAdapter QMixStreamAdapter);
}
