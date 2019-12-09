package com.xingwang.classroom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.xingwang.classroom.ui.ClassRoomDetailActivity;
import com.xingwang.classroom.ui.ClassRoomHomeActivity;

import java.io.File;
import java.io.IOException;

/**
 * 外部应用工具类
 * Date:2019/10/17
 * Time;11:42
 * author:baiguiqiang
 */
public class ClassRoomLibUtils {

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
     * @param context 当前页面的context
     * @param id 课程id
     */
    public static void startDetailActivity(Context context,int id){
       context.startActivity(ClassRoomDetailActivity.getIntent(context,id));
    }
    /**
     * 启动课程详情页面
     * @param activity 当前页面的activity
     * @param id 课程id
     *           只有点击取消了收藏才会返回
     */
    public static void startForResultDetailActivity(Activity activity, int id,int requestCode,int resultCode){
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
    public static void startForResultDetailActivity(Activity activity, int id,int requestCode,int resultCode,String resultParameter){
            activity.startActivityForResult(ClassRoomDetailActivity.getIntent(activity,id,resultCode,resultParameter),requestCode);
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
