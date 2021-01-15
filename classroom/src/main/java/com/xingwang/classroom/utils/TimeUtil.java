package com.xingwang.classroom.utils;


import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Date:2019/9/24
 * Time;10:20
 * author:baiguiqiang
 */
public class TimeUtil {
    private final static long minute = 60 * 1000;// 1分钟

    private final static long hour = 60 * minute;// 1小时

    private final static long day = 24 * hour;// 1天

    private final static long month = 31 * day;// 月

    private final static long year = 12 * month;// 年
    /**
     * 返回文字描述的日期
     *
     * @return
     */
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
    public static final String getYMD(String s) {
        if (TextUtils.isEmpty(s))
            return "";
        Date date = new Date(Long.parseLong(s) * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
    public static final String getMD(String s) {
        if (TextUtils.isEmpty(s))
            return "";
        Date date = new Date(Long.parseLong(s) * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(date);
    }
    public static final String getYMDHMS(String s) {
        if (TextUtils.isEmpty(s))
            return "";
        Date date = new Date(Long.parseLong(s) * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return sdf.format(date);
    }
    /**
     *
     * @param mTime yyyy-MM-dd HH:mm:ss这种格式
     * @return
     */

    public static String getTimeFormatText(String mTime) {
        if (TextUtils.isEmpty(mTime)){
            return "刚刚";
        }
      Date date= new Date(getStringToDate(mTime,"yyyy-MM-dd HH:mm:ss"));
        if (date == null) {
            return "刚刚";
        }

        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }

        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }

        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }

        if (diff > hour) {
            r = (diff / hour);
            return r + "个小时前";
        }

        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";

    }
    public static  String getCurTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    /**
     * 某个时间点 加上多少天
     * @param time
     * @param day
     * @return
     */
    public static String byTimeAddDay(String time,int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd", Locale.CHINA);

        try {
            Date date = sdf.parse(time);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            rightNow.add(Calendar.DAY_OF_YEAR, day);
            Date mTime = rightNow.getTime();
            String format = sdf1.format(mTime);
            //2019-06-10减60天为：2019-04-11

            return format;
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
    }
    public static String byTimeAddDay1(String time,int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);


        try {
            Date date = sdf.parse(time);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            rightNow.add(Calendar.DAY_OF_YEAR, day);
            Date mTime = rightNow.getTime();
            String format = sdf1.format(mTime);
            //2019-06-10减60天为：2019-04-11

            return format;
        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }
    }
}
