package com.xingwang.classroom.http;

public interface HttpCallBack<T> {
    void onFailure(String message);
    void onSuccess(T t);

}
