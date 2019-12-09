package com.xingwang.classroom.utils;

import android.content.Context;

/**
 * Date:2019/8/21
 * Time;8:44
 * author:baiguiqiang
 */
public class StatusBarUtils {
    public static   int getStatusHeight(Context context){
        int statusBarHeight = -1;
        //获取status_bar_height资源的ID
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight =context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;

    }
}
