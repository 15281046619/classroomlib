package com.xinwang.bgqbaselib.utils;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * Date:2019/8/21
 * Time;9:21
 * author:baiguiqiang
 */
public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        GlideUtils.loadAvatar((String) path,imageView);
    }
}
