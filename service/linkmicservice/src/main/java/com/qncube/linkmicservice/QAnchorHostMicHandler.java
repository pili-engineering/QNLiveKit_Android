package com.qncube.linkmicservice;


import com.qncube.lcommon.QMergeOption;
import com.qncube.liveroomcore.been.QMicLinker;

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
         * 混流布局适配
         * @param micLinkers 所有连麦者
         * @return 返回重设后的每个连麦者的混流布局
         */
        List<QMergeOption> onResetMixParam(List<QMicLinker> micLinkers, QMicLinker target, boolean isJoin);
    }

    /**
     * 设置混流适配器
     * @param QMixStreamAdapter
     */
    public void setMixStreamAdapter(QMixStreamAdapter QMixStreamAdapter);
}
