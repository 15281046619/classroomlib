package com.xingwang.classroom.http;

import android.text.TextUtils;


import com.beautydefinelibrary.BeautyDefine;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Date:2019/8/20
 * Time;10:17
 * author:baiguiqiang
 */
public class HttpHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String mAuth = BeautyDefine.getAccountDefine().getAuthStr();
        //String mAuth = "RAlUUlcOQ1BbUABTAlRWCQABDVEGXggEV1BUBQdQUFYDVwhWBg1XcUQVMXtODyo+VTd1IV4qdmA1cGEgYlNyYEBcNCIzbiRlIVV7dGYueWNYQ1VmdAF2clBUJydlNDd2RHp2cGUNdHdUEi58WlsvJUUeUCVKCA8WXw==";
        if (!TextUtils.isEmpty(mAuth))
        builder.addHeader("Authorization",mAuth);

        return chain.proceed(builder.build());
    }
}
