package com.xingwang.classroom.utils;

import android.text.TextUtils;

import com.beautydefinelibrary.BeautyDefine;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpHeaderInterceptor implements Interceptor {
   /* //private String mAuth= BeautyDefine.getAccountDefine().getAuthStr();
    private String mAuth="Yt7daGui/arohoSc7Kzgam07KF+iDO9M/dM1k/cZ/eG17lL99fvNn7utihSkxEKaMx4HqCrW/1ooz5zTOeHLoHqt2AIfuHEmh/LB1qxB2Rk=";
    //private String mAuth="Yt7daGui/arohoSc7Kzgam07KF+iDO9M66EzpWTxN3TEx0xNj2D1iVbjfAOv6d6qL8Q+u1nYd/ZAOWCUboPvoARJACwvFGC3tNIW8ABySss=";
    public HttpHeaderInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder=chain.request().newBuilder();
        if (EmptyUtils.isNotEmpty(mAuth))
            builder.addHeader("Authorization",mAuth);
        return chain.proceed(builder.build());
    }*/


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String mAuth;
        if (Constants.APP_DBG) {
            mAuth = "Yt7daGui/arohoSc7Kzgam07KF+iDO9M66EzpWTxN3TEx0xNj2D1iVbjfAOv6d6qL8Q+u1nYd/ZAOWCUboPvoARJACwvFGC3tNIW8ABySss=";
         //   mAuth = "Yt7daGui/arohoSc7Kzgam07KF+iDO9M/dM1k/cZ/eG17lL99fvNn7utihSkxEKaMx4HqCrW/1ooz5zTOeHLoHqt2AIfuHEmh/LB1qxB2Rk=";
        }
        else
            mAuth = BeautyDefine.getAccountDefine().getAuthStr();



        if (!TextUtils.isEmpty(mAuth))
            builder.addHeader("Authorization",mAuth);
        return chain.proceed(builder.build());
    }
}
