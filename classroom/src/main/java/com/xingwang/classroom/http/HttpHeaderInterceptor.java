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
        //String mAuth = "FQIGCgUPFQwDUwQDWwYFUwJbBVACAwMEVlFSCQdVAgYBBRcEAFQJMAIvfzlHVW8lZHl4f2R2NmAJeXgiaQUWJwJGdnZzLGB1ZAR0f31CJTNleFA1Vm8iZDIAACAwUGU2YF11ImEIMitxJgN9VR0CewZOMH9XCmBwb2d3Nkp5eXgwWy4lJztjelR3I29fd3h9B25WMDBxdXclPHclWV5mJxgOcXYPWWR2BAZbOQRHBiJUAjgiaSQ9IU8WVG9bSg8RTg==";
        //  String mAuth = "wQDWwYFUwJbBVACAwMEVlFSCQdVAgYBBRcEAFQJMAIvfzlHVW8lZHl4f2R2NmAJeXgiaQUWJwJGdnZzLGB1ZAR0f31CJTNleFA1Vm8iZDIAACAwUGU2YF11ImEIMitxJgN9VR0CewZOMH9XCmBwb2d3Nkp5eXgwWy4lJztjelR3I29fd3h9B25WMDBxdXclPHclWV5mJxgOcXYPWWR2BAZbOQRHBiJUAjgiaSQ9IU8WVG9bSg8RTg==";
        if (!TextUtils.isEmpty(mAuth))
            builder.addHeader("Authorization",mAuth);

        return chain.proceed(builder.build());
    }
}
