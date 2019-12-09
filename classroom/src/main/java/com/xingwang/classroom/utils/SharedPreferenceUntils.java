package com.xingwang.classroom.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Date:2019/9/26
 * Time;9:20
 * author:baiguiqiang
 */
public class SharedPreferenceUntils {
    private final static  String saveName ="classroom";
    public static void putString(Context context,String key,String value){
        SharedPreferences.Editor mSharedPreferences = context.getSharedPreferences(saveName, Context.MODE_PRIVATE).edit();
        mSharedPreferences.putString(key,value).apply();

    }
    public static String getString(Context context,String key,String defValue){
        SharedPreferences mSharedPreferences = context.getSharedPreferences(saveName, Context.MODE_PRIVATE);
       return mSharedPreferences.getString(key,defValue);
    }

}
