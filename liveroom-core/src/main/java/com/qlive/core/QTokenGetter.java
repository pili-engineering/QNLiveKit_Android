package com.qlive.core;

import com.qlive.core.QLiveCallBack;

public interface QTokenGetter{
    void getTokenInfo( QLiveCallBack<String> callback);
}