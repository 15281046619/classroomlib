package com.xinwang.bgqbaselib.utils;


import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.xinwang.bgqbaselib.R;
import com.xinwang.bgqbaselib.view.GlideRoundTransform;


public class GlideUtils {

    public static void loadAvatar(String url,int defaultImg ,ImageView imageView){

        if (!activityIsFinished(imageView.getContext()))
            Glide.with(imageView.getContext()).load(url).placeholder(defaultImg).centerCrop().into(imageView);
    }
    public static void loadAvatarNoCenterCrop(String url,int defaultImg ,ImageView imageView){

        if (!activityIsFinished(imageView.getContext()))
            Glide.with(imageView.getContext()).load(url).placeholder(defaultImg).into(imageView);
    }
    public static void loadAvatarNoPlaceholder(String url ,ImageView imageView){
        if (!activityIsFinished(imageView.getContext()))
            Glide.with(imageView.getContext()).load(url).centerCrop().into(imageView);
    }
    public static void loadAvatar(String url,int defaultImg ,ImageView imageView,int width,int height){
        if (!activityIsFinished(imageView.getContext()))
            Glide.with(imageView.getContext()).load(url).override(width,height).placeholder(defaultImg).centerCrop().into(imageView);
    }

    public static void loadAvatar(String url,ImageView imageView){
        if (!activityIsFinished(imageView.getContext()))//asbitmap 解决默认图片 与实际图片不一致，导致刷新变化问题
            Glide.with(imageView.getContext()).load(url).asBitmap().placeholder(R.mipmap.bg_default_placeholder_classroom)
                    .error(R.mipmap.bg_default_error_classroom).centerCrop().into(imageView);
    }
    public static void loadAvatar(Integer resourceId,ImageView imageView){
        if (!activityIsFinished(imageView.getContext()))
            Glide.with(imageView.getContext()).load(resourceId).placeholder(R.color.GrayClassRoom).centerCrop().into(imageView);
    }
    public static void loadGif(Integer resourceId,ImageView imageView){
        if (!activityIsFinished(imageView.getContext()))
            Glide.with(imageView.getContext()).load(resourceId).asGif().centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
    }
    public static void loadAvatar(String url,ImageView imageView,int placeholder){
        if (!activityIsFinished(imageView.getContext()))
            Glide.with(imageView.getContext()).load(url).placeholder(placeholder).centerCrop().into(imageView);
    }
    public static void loadAnimateAvatar(String url,ImageView imageView){
        if (!activityIsFinished(imageView.getContext()))
            Glide.with(imageView.getContext()).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop().crossFade().into(imageView);
    }
    public static void loadRoundedCorners(String url ,int placeholder,ImageView imageView, int dp){

        if (!activityIsFinished(imageView.getContext()))
            Glide.with(imageView.getContext()).load(url).placeholder(placeholder)
                    .transform(new CenterCrop(imageView.getContext()), new GlideRoundTransform(imageView.getContext(),dp)).into(imageView);

    }
    static boolean activityIsFinished(Context context){
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return activity == null || activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed());
        }else //可能出在fragment里面
            return false;
    }



}
