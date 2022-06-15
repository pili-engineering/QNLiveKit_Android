package com.qncube.danmakuservice;


import com.qncube.liveroomcore.QLiveCallBack;
import com.qncube.liveroomcore.service.QLiveService;

import java.util.HashMap;

/**
 * 弹幕服务
 */
public interface QDanmakuService extends QLiveService {


    public void addDanmakuServiceListener(QDanmakuServiceListener listener);

    public void removeDanmakuServiceListener(QDanmakuServiceListener listener);

    /**
     * 发送弹幕消息
     */
    public void sendDanmaku(String msg, HashMap<String,String> extension, QLiveCallBack<QDanmaku> callBack);
}

