package com.qncube.danmakuservice;


import com.qncube.liveroomcore.QLiveCallBack;
import com.qncube.liveroomcore.service.QLiveService;

import java.util.HashMap;

/**
 * 弹幕服务
 */
public interface QDanmakuService extends QLiveService {

    public interface QNDanmakuServiceListener {

        /**
         * 收到弹幕消息
         */
        void onReceiveDanmaku(DanmakuModel model);
    }

    public void addDanmakuServiceListener(QNDanmakuServiceListener listener);

    public void removeDanmakuServiceListener(QNDanmakuServiceListener listener);

    /**
     * 发送弹幕消息
     */
    public void sendDanmaku(String msg, HashMap<String,String> extensions, QLiveCallBack<DanmakuModel> callBack);
}

