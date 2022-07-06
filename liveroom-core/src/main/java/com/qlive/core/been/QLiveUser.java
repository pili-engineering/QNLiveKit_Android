package com.qlive.core.been;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户
 */
public class QLiveUser implements Serializable {

    /**
     * 用户ID
     */
    @JSONField(name = "user_id")
    public String userId;
    /**
     * 用户头像
     */
    public String avatar;
    /**
     * 名字
     */
    public String nick;
    /**
     * 扩展字段
     */
    @JSONField(name = "extends")
    public Map<String, String> extensions;
    /**
     * 用户im id
     */
    @JSONField(name = "im_userid")
    public String imUid;
    /**
     * 用户Im名称
     */
    public String im_username;
}
