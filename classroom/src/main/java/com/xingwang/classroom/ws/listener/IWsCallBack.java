package com.xingwang.classroom.ws.listener;

import com.google.gson.internal.$Gson$Types;
import com.xingwang.classroom.ws.WsStatusListener;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class IWsCallBack<T> extends WsStatusListener {

        public Type mType;
        public  void onRequestStart(){}//请求之前
        public abstract void onFailure(String message);
        public abstract void onSuccess(T t);
        public  void onRequestEnd(){}//请求之后 onFailure与onSuccess之前
        static Type getSuperclassTypeParameter(Class<?> subclass) {
                Type superclass = subclass.getGenericSuperclass();
                if (superclass instanceof Class) {
                       // throw new RuntimeException("Missing type parameter.");
                        return null;
                }
                ParameterizedType parameterized = (ParameterizedType) superclass;
                return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }
        public IWsCallBack() {
                mType = getSuperclassTypeParameter(getClass());
        }
}
