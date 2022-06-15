package com.qbcube.pkservice;


import com.qncube.liveroomcore.been.QExtension;

import org.jetbrains.annotations.NotNull;

/**
 * pk回调
 */
public interface QPKServiceListener {

    //开始
    void onStart(@NotNull QPKSession pkSession);

    //结束
    void onStop(@NotNull QPKSession pkSession, int code, @NotNull String msg);

    //pk 收对方流超时
    void onStartTimeOut(@NotNull QPKSession pkSession);

    /**
     * 扩展自定义字段跟新
     *
     * @param QExtension
     */
    void onPKExtensionUpdate(@NotNull QPKSession pkSession, @NotNull QExtension QExtension);
}