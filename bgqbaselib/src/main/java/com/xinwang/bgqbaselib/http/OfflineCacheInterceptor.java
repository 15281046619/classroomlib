package com.xinwang.bgqbaselib.http;

import android.content.Context;

import com.xinwang.bgqbaselib.utils.CommentUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Date:2021/8/4
 * Time;17:15
 * author:baiguiqiang
 */
public class OfflineCacheInterceptor implements Interceptor {
    private Context context;
    public OfflineCacheInterceptor(Context context){
        this.context =context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!CommentUtils.isNetworkConnected(context)) {
            int offlineCacheTime = 60*60*24*28;//离线的时候的缓存的过期时间
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + offlineCacheTime)
                    .build();
        }
        return chain.proceed(request);

    }
}
