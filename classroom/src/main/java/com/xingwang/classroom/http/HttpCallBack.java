package com.xingwang.classroom.http;

import com.xingwang.classroom.bean.GoodListBean;

public interface HttpCallBack<T> {
    void onFailure(String message);
    void onSuccess(T t);
}
