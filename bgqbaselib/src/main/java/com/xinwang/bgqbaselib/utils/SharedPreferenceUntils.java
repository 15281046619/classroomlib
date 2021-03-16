package com.xinwang.bgqbaselib.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tencent.smtt.sdk.QbSdk;

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
    public static void saveX5State(Context context,boolean isInstallFinish){
        putString(context,"isinstallfinish",isInstallFinish?"1":"0");
    }
    public static Boolean getX5IsInstallFinish(Context context){
      /*  if (QbSdk.isTbsCoreInited()){//可能出现安装成功，没有保存数据
            saveX5State(context,true);
            return true;
        }*/

       String state = getString(context,"isinstallfinish","0");
      if (state.equals("1")){
          return true;
      }else {
         if (QbSdk.isTbsCoreInited()){
              return  true;
          }else {
              return false;
          }
      }
    }

    public static String getSavePhone(Context context){
        return getString(context,"phone","");
    }
    public static void savePhone(Context context,String phone){
         putString(context,"phone",phone);
    }
    public static String getSaveName(Context context){
        return getString(context,"name","");
    }
    public static void saveName(Context context,String name){
         putString(context,"name",name);
    }
    public static String getSaveAddress(Context context){
        return getString(context,"address","");
    }
    public static void saveAddress(Context context,String address){
         putString(context,"address",address);
    }
    public static String getSaveCity(Context context){
        return getString(context,"city","");
    }
    public static void saveCity(Context context,String city){
         putString(context,"city",city);
    }
}
