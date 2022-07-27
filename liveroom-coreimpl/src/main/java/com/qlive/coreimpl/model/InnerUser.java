package com.qlive.coreimpl.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.qlive.core.been.QLiveUser;

import java.io.Serializable;

public class InnerUser  extends QLiveUser implements Serializable {
    @JSONField (serialize = false)
    public String im_password;

}
