package com.qlive.pkservice;

import com.qlive.avparam.QMergeOption;
import com.qlive.avparam.QMixStreamParams;
import com.qlive.core.been.QPKSession;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * pk混流适配器
 */
public interface QPKMixStreamAdapter {
    /**
     * 当pk开始 如何混流
     *
     * @param pkSession
     * @return 返回混流参数
     */
    List<QMergeOption> onPKLinkerJoin(@NotNull QPKSession pkSession);

    /**
     * pk开始时候混流画布变成多大
     *返回null则原来主播有多大就有多大
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
    default List<QMergeOption> onPKLinkerLeft() {
        return new ArrayList<QMergeOption>();

    }
    /**
     * 当pk结束后如果还有其他普通连麦者 如何混流 如果pk结束后没有其他连麦者 则不会回调 返回空则默认之前的不变化
     * @return
     */
    default QMixStreamParams onPKMixStreamStop() {
        return null;
    }

}
