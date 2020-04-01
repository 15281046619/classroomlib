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
         //String mAuth = "FQIGBQcPFQFTUVAFCAcHUFRTV1ZTAwEDBQNWCAQCUgZdBUJSBV4IJwIRfiUyEX8xZGF5fF5xIXRpZmwnCSw/JxJZYnZ0DXxzSndwZVR/JS5mSVU2J046cRF8IDUnZWwhZ1VjJlAMLDcGKTN8DhoreRY2KWpXZ31lCFl5NHRxcng3Wys1VxJUaXJdIHkGRnZgB3p2MSR6bmE2BX00YydjJg4VfXY1CXlxJQ93IjJALSIJCjsuT1xKRwM=";
        if (!TextUtils.isEmpty(mAuth))
        builder.addHeader("Authorization",mAuth);

        return chain.proceed(builder.build());
    }
}
