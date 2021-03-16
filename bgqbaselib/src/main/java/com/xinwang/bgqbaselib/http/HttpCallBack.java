package com.xinwang.bgqbaselib.http;



public interface HttpCallBack<T> {
    void onFailure(String message);
    void onSuccess(T t);
}
