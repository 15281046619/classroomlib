package com.xingwang.swip.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xingwang.swip.R;

public class GlideUtils {
    public static void loadPic(String url, ImageView imageView, Context context,int size){
        Glide.with(context).load(url)
                           .placeholder(R.drawable.swip_pp_ic_holder_light)
                           .error(R.drawable.swip_err_img)
                           .override(size,size).dontAnimate().into(imageView);
    }

    public static void loadPic(int id, ImageView imageView, Context context,int size){
        Glide.with(context).load(id)
                .placeholder(R.drawable.swip_pp_ic_holder_light)
                .error(R.drawable.swip_err_img)
                .override(size,size).dontAnimate().into(imageView);
    }

    public static void loadPic(String url, ImageView imageView, Context context,int width,int height){
        Glide.with(context).load(url)
                .placeholder(R.drawable.swip_pp_ic_holder_light)
                .error(R.drawable.swip_err_img)
                .override(width,height).dontAnimate().into(imageView);
    }

    public static void loadPic(String url, ImageView imageView, Context context){
        if (EmptyUtils.isEmpty(url)){
            loadPic(imageView,context);
            return;
        }
        Glide.with(context).load(url)
                .placeholder(R.drawable.swip_pp_ic_holder_light)
                .error(R.drawable.swip_err_img)
                .dontAnimate().into(imageView);
    }

    //加载头像
    public static void loadAvatar(String url, ImageView imageView, Context context){
        loadAvatar(url,imageView,context,false);
    }

    //加载头像
    //isCache-是否缓存 true-不缓存 false-缓存 默认需要缓存
    public static void loadAvatar(String url, ImageView imageView, Context context,boolean isCache){
        if (EmptyUtils.isEmpty(url)){
            Glide.with(context).load(R.drawable.swip_default_avatar)
                    .into(imageView);
            return;
        }

        if (isCache){//此时不缓存
            Glide.with(context).load(url)
                    .placeholder(R.drawable.swip_pp_ic_holder_light)
                    .error(R.drawable.swip_err_img)
                    .skipMemoryCache(isCache)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .dontAnimate().into(imageView);
        }else {
            Glide.with(context).load(url)
                    .placeholder(R.drawable.swip_pp_ic_holder_light)
                    .error(R.drawable.swip_err_img)
                    .skipMemoryCache(isCache)
                    .dontAnimate().into(imageView);
        }



    }

    public static void loadPic(int id, ImageView imageView, Context context){
        Glide.with(context).load(id)
                .placeholder(R.drawable.swip_pp_ic_holder_light)
                .error(R.drawable.swip_err_img)
                .dontAnimate().into(imageView);
    }

    public static void loadPic(Bitmap bitmap, ImageView imageView, Context context){
        Glide.with(context).load(bitmap)
                .placeholder(R.drawable.swip_pp_ic_holder_light)
                .error(R.drawable.swip_err_img)
                .dontAnimate().into(imageView);
    }

    public static void loadPic(ImageView imageView, Context context){
        Glide.with(context).load(R.drawable.swip_pp_ic_holder_light)
                .into(imageView);
    }

}
