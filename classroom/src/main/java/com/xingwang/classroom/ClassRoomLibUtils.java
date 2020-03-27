package com.xingwang.classroom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;

import com.xingwang.classroom.dialog.CenterQuiteDialog;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.ui.ClassRoomDetailActivity;
import com.xingwang.classroom.ui.ClassRoomHomeActivity;
import com.xingwang.classroom.utils.CommentUtils;
import com.xingwang.classroom.utils.SharedPreferenceUntils;

import java.io.File;
import java.io.IOException;

/**
 * 外部应用工具类
 * Date:2019/10/17
 * Time;11:42
 * author:baiguiqiang
 */
public class ClassRoomLibUtils {

    public static final String TYPE_ZY ="zy";
    public static final String TYPE_JQ ="jq";
    public static final String TYPE_SC ="sc";
    public static final String TYPE_NY ="ny";

    public static void initLib(String type){
        switch (type){
            case TYPE_ZY:
                HttpUrls.URL_HOST ="http://zyapp.test.xw518.com/";
                HttpUrls.CHANNEL ="xwapp.zy.lecture.lecture_";
                break;
            case TYPE_JQ:
                break;
            case TYPE_SC:
                break;
            case TYPE_NY:
                break;
            default:
        }

    }

    /**
     * 获取视频缓存文件夹
     * @param context
     * @return
     */
    public static File getVideoCachePathFile(Context context){
        File file =new File(getVideoCachePath(context));
        if (!file.exists()){
            file.mkdir();
        }
        return  file;
    }

    /**
     * 启动课程详情页面
     * @param id 课程id
     */
    public static void startDetailActivity(FragmentActivity activity,int id){
        startDetailActivityType(activity,id,"video");
    }

    public static void startDetailActivityType(FragmentActivity activity,int id,String type){
        if (type.equals("tplink")) {//直播 判断x5内核是否加载完成
            if (!SharedPreferenceUntils.getX5IsInstallFinish(activity)){//没有加载完成提示用户
                CenterQuiteDialog centerQuiteDialog =   new CenterQuiteDialog();
                centerQuiteDialog.setCallback(integer ->  activity.startActivity(ClassRoomDetailActivity.getIntent(activity,id)));
                centerQuiteDialog.showDialog(activity.getSupportFragmentManager());
                return;
            }
        }
        activity.startActivity(ClassRoomDetailActivity.getIntent(activity,id));
    }
    /**
     * 启动课程详情页面
     * @param activity 当前页面的activity
     * @param id 课程id
     *           只有点击取消了收藏才会返回
     */
    public static void startForResultDetailActivity(FragmentActivity activity, int id,int requestCode,int resultCode){
        startForResultDetailActivityType(activity,id,requestCode,resultCode,"video");
    }
    /**
     * 启动课程详情页面 有判断是否直播
     * @param activity 当前页面的activity
     * @param id 课程id
     *           只有点击取消了收藏才会返回
     */
    public static void startForResultDetailActivityType(FragmentActivity activity, int id,int requestCode,int resultCode,String type){
        if (type.equals("tplink")) {//直播 判断x5内核是否加载完成
            if (!SharedPreferenceUntils.getX5IsInstallFinish(activity)){//没有加载完成提示用户
                CenterQuiteDialog centerQuiteDialog =   new CenterQuiteDialog();
                centerQuiteDialog.setCallback(integer -> activity.startActivityForResult(ClassRoomDetailActivity.getIntent(activity,id,resultCode),requestCode));
                centerQuiteDialog.showDialog(activity.getSupportFragmentManager());
                return;
            }
        }
        activity.startActivityForResult(ClassRoomDetailActivity.getIntent(activity,id,resultCode),requestCode);
    }
    /**
     * 启动课程详情页面
     * @param activity
     * @param id
     * @param requestCode 请求code
     * @param resultCode  结果code
     * @param resultParameter 返回是否收藏参数名 在跳转activity种onActivityResult方法的intent获取该参数值 为boolean类型，表示是否收藏  只有点击取消了收藏才会返回
     */
    public static void startForResultDetailActivity(FragmentActivity activity, int id,int requestCode,int resultCode,String resultParameter){
        startForResultDetailActivityType(activity,id,requestCode,resultCode,resultParameter,"video");
    }
    /**
     * 启动课程详情页面 判断了type是直播的
     * @param activity
     * @param id
     * @param requestCode 请求code
     * @param resultCode  结果code
     * @param resultParameter 返回是否收藏参数名 在跳转activity种onActivityResult方法的intent获取该参数值 为boolean类型，表示是否收藏  只有点击取消了收藏才会返回
     */
    public static void startForResultDetailActivityType(FragmentActivity activity, int id,int requestCode,int resultCode,String resultParameter,String type){
        if (type.equals("tplink")) {//直播 判断x5内核是否加载完成
            if (!SharedPreferenceUntils.getX5IsInstallFinish(activity)){//没有加载完成提示用户
                CenterQuiteDialog centerQuiteDialog =   new CenterQuiteDialog();
                centerQuiteDialog.setCallback(integer ->  activity.startActivityForResult(ClassRoomDetailActivity.getIntent(activity,id,resultCode,resultParameter),requestCode));
                centerQuiteDialog.showDialog(activity.getSupportFragmentManager());
                return;
            }
        }
        activity.startActivityForResult(ClassRoomDetailActivity.getIntent(activity,id,resultCode,resultParameter),requestCode);
    }

    /**
     *
     * uri方式打开activity 判断了是不是直播课程
     * @param activity
     * @param url
     */
    public static void startActivityForUri(FragmentActivity activity,String url){
        if (url.contains("classroom://com.xingw.zyapp.kcdetail")&&url.contains("tplink")){
                String id=  CommentUtils.getParamByUrl(url,"id");
                startDetailActivityType(activity,id==null?0:Integer.parseInt(id),"tplink");
        }else {
            Uri mRui = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, mRui);
            activity.startActivity(intent);
        }
    }

    /**
     * 启动课程主页
     * @param context
     */
    public static void startListActivity(Context context){
        context.startActivity(new Intent(context,ClassRoomHomeActivity.class));
    }

    /**
     *
     * @param context
     * @param type 分类名字
     */
    public static void startListActivity(Context context,String type){
        context.startActivity(new Intent(context,ClassRoomHomeActivity.class).putExtra("type",type));
    }

    /**
     *清除缓存
     * @param context
     */
    public static void cleanAllCache(Context context){
        File mFile =getVideoCachePathFile(context);
        deleteFileOrDirectory(mFile);
    }



    /**
     * 删除指定文件，如果是文件夹，则递归删除
     *
     * @param file
     * @return
     * @throws IOException
     */
    private static boolean deleteFileOrDirectory(File file) {
        try {
            if (file != null && file.isFile()) {
                return file.delete();
            }
            if (file != null && file.isDirectory()) {
                File[] childFiles = file.listFiles();
                // 删除空文件夹
                if (childFiles == null || childFiles.length == 0) {
                    return file.delete();
                }
                // 递归删除文件夹下的子文件
                for (int i = 0; i < childFiles.length; i++) {
                    deleteFileOrDirectory(childFiles[i]);
                }
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private static String getVideoCachePath(Context context){
        String videoCachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            videoCachePath = context.getExternalCacheDir().getPath();
        } else {
            videoCachePath = context.getCacheDir().getPath();
        }
        videoCachePath = videoCachePath + "/cachePath/";
        return videoCachePath;
    }
}
