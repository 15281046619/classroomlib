package com.xingwang.classroom.utils;

import android.util.Log;



/**
 * log工具类
 */
public class LogUtil {
   // private static Boolean isOpenLog = false;
    public static void e(String mTag,String mContent){
        e(Constants.APP_DBG,mTag,mContent);
    }
    public static void e(String mContent,Class mClass){
        e("TAG",mClass.getSimpleName()+":"+mContent);
    }
    public static void e(Boolean isOpenLog,String mTag,String mContent){
        if (isOpenLog)
            Log.e(mTag,mContent);
    }
    public static void i(String mContent){
        i("TAG",mContent);
    }
    public static void i(String mContent,Class mClass){
        i("TAG",mClass.getSimpleName()+":"+mContent);
    }
    public static void i(String mTag,String mContent){
        i(Constants.APP_DBG,mTag,mContent);
    }
    public static void i(Boolean isOpenLog,String mTag,String mContent){
        if (isOpenLog)
            Log.i(mTag,mContent);
    }
}
