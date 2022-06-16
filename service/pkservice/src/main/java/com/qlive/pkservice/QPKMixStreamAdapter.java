package com.qlive.pkservice;

import com.qlive.avparam.QMergeOption;
import com.qlive.avparam.QMixStreamParams;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * pk混流适配器
 */
public  interface QPKMixStreamAdapter {
    /**
     * 当pk开始 如何混流
     *
     * @param pkSession
     * @return 返回混流参数
     */
    List<QMergeOption> onPKLinkerJoin(@NotNull QPKSession pkSession);

    /**
     * pk开始时候混流画布变成多大
     *
     * @param pkSession
     * @return
     */
    QMixStreamParams onPKMixStreamStart(@NotNull QPKSession pkSession);

    /**
     * 当pk结束后如果还有其他普通连麦者 如何混流
     * 如果pk结束后没有其他连麦者 则不会回调
     *
     * @return
     */
    List<QMergeOption> onPKLinkerLeft();
}
