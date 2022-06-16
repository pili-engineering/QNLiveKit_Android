package com.qncube.linveroominner;

import com.alibaba.fastjson.annotation.JSONField;
import com.qncube.liveroomcore.been.QLiveUser;

public class InnerUser  extends QLiveUser {
    @JSONField (serialize = false)
    public String im_password;

}
