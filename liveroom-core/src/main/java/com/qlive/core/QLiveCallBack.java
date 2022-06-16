package com.qlive.core;

public interface QLiveCallBack<T> {

    void onError(int code, String msg);

    void onSuccess(T data);

}
