package com.xinwang.bgqbaselib.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Date:2019/8/26
 * Time;10:20
 * author:baiguiqiang
 */
public class Constants {

    public static String DATA ="data";

    public static int LOAD_DATA_TYPE_INIT =1;
    public static int LOAD_DATA_TYPE_MORE =2;
    public static int LOAD_DATA_TYPE_REFRESH =3;

    public static String EXTRA_IS_LIVE= "extra_is_live";

    public final static String CHANNEL_TYPE_NEW_MESSAGE="chat.newitem";
    public final static String CHANNEL_TYPE_NEW_ORDER="chat.neworder";
    public final static String CHANNEL_TYPE_REMOVE_ITEM="chat.removeitem";//删除聊天室消息
    public final static String CHANNEL_TYPE_LIVE_STOP="live.stop";//直播结束
    public final static String CHANNEL_TYPE_LIVE_FIXED1="live.fixed_1";//固顶消息
    public final static String CHANNEL_TYPE_LIVE_FIXED2="live.fixed_2";//取消固顶消息


    public static boolean APP_DBG = false; // 是否是debug模式
    public static void init(Context context){
        APP_DBG = isApkDebugable(context);
    }
    /**
     * 但是当我们没在AndroidManifest.xml中设置其debug属性时:
     * 使用Eclipse运行这种方式打包时其debug属性为true,使用Eclipse导出这种方式打包时其debug属性为法false.
     * 在使用ant打包时，其值就取决于ant的打包参数是release还是debug.
     * 因此在AndroidMainifest.xml中最好不设置android:debuggable属性置，而是由打包方式来决定其值.
     *
     * @param context
     * @return
     * @author SHANHY
     * @date   2015-8-7
     */
    public static boolean isApkDebugable(Context context) {
        try {
            ApplicationInfo info= context.getApplicationInfo();
            return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
        } catch (Exception e) {

        }
        return false;
    }
    public static boolean isShowGiftSubmitDialog= true;//显示礼物提示
}
