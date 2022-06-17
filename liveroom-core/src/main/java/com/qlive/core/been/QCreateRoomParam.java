package com.qlive.core.been;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.HashMap;

public class QCreateRoomParam {

    public String title;
    @JSONField(name = "notice")
    public String notice;
    @JSONField(name = "cover_url")
    public String coverURL;
    @JSONField(name = "extension")
    public HashMap<String,String> extension;
}