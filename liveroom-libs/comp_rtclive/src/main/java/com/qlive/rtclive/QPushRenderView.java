package com.qlive.rtclive;

import android.view.View;

import com.qiniu.droid.rtc.QNRenderView;

/**
 * 推流预览窗口
 * 子类实现 QPushSurfaceView 和 QPushTextureView
 */
public interface QPushRenderView {
    View getView();

    /**
     * renview
     * @return QNRenderView 七牛rtc render
     */
    QNRenderView getQNRender();
}
