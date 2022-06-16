package com.qlive.coreimpl.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.qlive.core.been.QLiveUser;

public class InnerUser  extends QLiveUser {
    @JSONField (serialize = false)
    public String im_password;

}
