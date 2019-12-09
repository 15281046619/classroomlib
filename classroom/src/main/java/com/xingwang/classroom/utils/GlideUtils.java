package com.xingwang.classroom.utils;



import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.xingwang.classroom.R;


public class GlideUtils {

    public static void loadAvatar(String url,int defaultImg ,ImageView imageView){

        if (imageView.getContext()!=null)
        Glide.with(imageView.getContext()).load(url).placeholder(defaultImg).into(imageView);
    }
    public static void loadAvatar(String url,ImageView imageView){
        if (imageView.getContext()!=null)
        Glide.with(imageView.getContext()).load(url).placeholder(R.color.GrayClassRoom).centerCrop().into(imageView);
    }
    public static void loadAnimateAvatar(String url,ImageView imageView){
        //transition(new DrawableTransitionOptions().crossFade(500))

        if (imageView.getContext()!=null)
        Glide.with(imageView.getContext()).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                .centerCrop().crossFade().into(imageView);
    }




}
