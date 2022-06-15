package com.qncube.linveroominner;

import com.qncube.liveroomcore.QLiveCallBack;

public interface QTokenGetter{
    void getTokenInfo( QLiveCallBack<String> callback);
}