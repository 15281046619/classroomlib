package com.xingwang.classroom.utils;

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
}
