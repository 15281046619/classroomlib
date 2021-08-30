package com.xinwang.bgqbaselib.utils;

import android.os.Build;
import android.text.TextUtils;

import com.beautydefinelibrary.BeautyDefine;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HttpHeaderInterceptor implements Interceptor {



    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String mAuth;
        if (Constants.APP_DBG) {
             mAuth = "Yt7daGui/aoyy7Q1vj4A/azFlG8oeOOEMiISto6P6ZtO3qKgeZcj6xHoz0yykhVpZtK2wbys7b0DSFdo79LolM3gopkMdJ2J";//我的用户信息
            // mAuth = "Yt7daGui/aoyy7Q1vj4A/WqGN2chCvDj+SGmyUDgz60FjuR+4+BxbMDo4yA3lEGRKJzSaWO+R9y9M3UB5dpB+uEaGoCQoPsc";//张满级用户
           // mAuth = "Yt7daGui/aoyy7Q1vj4A/QJMKyyeMuHM+cdTXYTCFSdIUrSaYV37J96Xjub3+3i847UybbiDyuP5EHmwUi7IbEvmYzYrKfyW";
           // mAuth = "HcfMahHBhWynR47DVaH+buO+3HH+XLZFZDW6R+JpmbImddhaV99iHhs0FP7dZEPWixjlCLhKgoCJ0PTYb5/BUQ1YzbTFXUrX";//nyapp
        }
        else
            mAuth = BeautyDefine.getAccountDefine().getAuthStr();


        if (!TextUtils.isEmpty(mAuth))
            builder.addHeader("Authorization",mAuth);
        builder.addHeader("User-Agent", "Android/"+ Build.VERSION.RELEASE+" Brand/"+Build.BRAND+" Board/"+Build.BOARD+" Model/"+Build.MODEL);
        return chain.proceed(builder.build());
    }
}
