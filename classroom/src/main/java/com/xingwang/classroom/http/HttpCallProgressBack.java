package com.xingwang.classroom.http;

public interface HttpCallProgressBack<T> extends HttpCallBack{
    void onProgressCallBack(long total, long current);
}
