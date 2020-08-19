package com.xingwang.classroom.utils;




import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.xingwang.classroom.R;


public class GlideUtils {

    public static void loadAvatar(String url,int defaultImg ,ImageView imageView){

        if (!activityIsFinished(imageView.getContext()))
        Glide.with(imageView.getContext()).load(url).placeholder(defaultImg).into(imageView);
    }
    public static void loadAvatar(String url,ImageView imageView){
        if (!activityIsFinished(imageView.getContext()))
        Glide.with(imageView.getContext()).load(url).placeholder(R.color.GrayClassRoom).centerCrop().into(imageView);
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

    static boolean activityIsFinished(Context context){
        Activity activity =(Activity) context;
        return activity == null || activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed());
    }



}
