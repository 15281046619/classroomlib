package com.xinwang.bgqbaselib.http;

public interface HttpCallProgressBack<T> extends HttpCallBack{
    void onProgressCallBack(long total, long current);
}
