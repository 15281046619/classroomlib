package com.xinwang.bgqbaselib.http;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Date:2021/8/4
 * Time;10:53
 * author:baiguiqiang
 */
public class NetCacheInterceptor  implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        int onlineCacheTime = 0;//在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0
        return response.newBuilder()
                .header("Cache-Control", "public, max-age="+onlineCacheTime)
                .removeHeader("Pragma")
                .build();

    }
}
