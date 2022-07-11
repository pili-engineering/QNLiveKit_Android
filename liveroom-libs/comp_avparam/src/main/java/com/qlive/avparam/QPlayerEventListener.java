package com.qlive.avparam;

/**
 * 拉流事件回调
 */
public interface QPlayerEventListener {
    /**
     * 拉流器准备中
     *
     * @param preparedTime 准备耗时
     */
    default void onPrepared(int preparedTime) {
    }

    /**
     * 拉流器信息回调
     *
     * @param what  事件 参考七牛霹雳播放器
     * @param extra 数据
     */
    default void onInfo(int what, int extra) {
    }

    /**
     * 拉流缓冲跟新
     *
     * @param percent 缓冲比分比
     */
    default void onBufferingUpdate(int percent) {
    }

    /**
     * /视频尺寸变化回调
     *
     * @param width  变化后的宽
     * @param height 变化后高
     */
    default void onVideoSizeChanged(int width, int height) {
    }

    /**
     * 播放出错回调
     *
     * @param errorCode 错误码 参考七牛霹雳播放器
     * @return 是否处理
     */
    default boolean onError(int errorCode) {
        return false;
    }
}
