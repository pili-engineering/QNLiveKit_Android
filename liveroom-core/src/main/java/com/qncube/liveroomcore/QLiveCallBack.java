package com.qncube.liveroomcore;

public interface QLiveCallBack<T> {

    void onError(int code, String msg);

    void onSuccess(T data);

}
