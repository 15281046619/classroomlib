package com.xingwang.classroom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.xingwang.classroom.bean.X5InstallSuccessBean;
import com.xingwang.classroom.dialog.CenterQuiteDialog;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.ui.ClassRoomDetailActivity;
import com.xingwang.classroom.ui.ClassRoomHomeActivity;
import com.xingwang.classroom.ui.LiveDetailActivity;
import com.xingwang.classroom.ui.LiveWebActivity;
import com.xingwang.classroom.ui.StatisticPriceActivity;
import com.xingwang.classroom.utils.ActivityUtil;
import com.xingwang.classroom.utils.CommentUtils;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.HttpUtil;
import com.xingwang.classroom.utils.LogUtil;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.utils.SharedPreferenceUntils;
import com.ycbjie.webviewlib.X5LogUtils;
import com.ycbjie.webviewlib.X5WebUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

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
    public static final String TYPE_TEST="test";//测试域名
    /**
     * 在application
     * @param type
     */
    public static void initLib(Context context,String type){
        IjkPlayerManager.setLogLevel(IjkMediaPlayer.IJK_LOG_SILENT);//关闭日志
        HttpUrls.URL_TYPE =type;
        switch (type){
            case TYPE_ZY:
                HttpUrls.URL_HOST ="http://zyapp.app.xw518.com/";
                HttpUrls.CHANNEL ="zyapp.lecture.lecture_";
                HttpUrls.CHANNEL_WS_URL ="ws://subscribe.app.xw518.com/server";
                HttpUrls.LIVE_CHANNEL ="zyapp.live.";
                break;
            case TYPE_JQ:
                break;
            case TYPE_SC:
                HttpUrls.URL_HOST ="http://yyapp.app.xw518.com/";
                HttpUrls.CHANNEL ="yyapp.lecture.lecture_";
                HttpUrls.CHANNEL_WS_URL ="ws://subscribe.app.xw518.com/server";
                HttpUrls.LIVE_CHANNEL ="yyapp.live.";
                break;
            case TYPE_NY:
                HttpUrls.URL_HOST ="http://nyapp.app.xw518.com/";
                HttpUrls.CHANNEL ="nyapp.lecture.lecture_";
                HttpUrls.CHANNEL_WS_URL="ws://subscribe.app.xw518.com/server";
                HttpUrls.LIVE_CHANNEL ="nyapp.live.";
                break;
            case TYPE_TEST:
                HttpUrls.URL_HOST ="http://xielei.test.xw518.com/zyapp.test.xw518.com/public/";//测试地址 更改了
                HttpUrls.CHANNEL_WS_URL="ws://192.168.65.74:10101";
                HttpUrls.CHANNEL ="zyapp.lecture.lecture_";
                HttpUrls.LIVE_CHANNEL ="zyapp.live.";
                break;
            default:
        }

        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        //1.确认您的App为非X86架构，x86架构无法发起内核下载
        //2.确认您的APN为wifi。由于非wifi下内核下载可能会消耗用户流量，建议提示用户。如果在非wifi下仍需要下载内核，请使用QbSdk.setDownloadWithoutWifi(true)接口
        QbSdk.setDownloadWithoutWifi(true);
      //  X5WebUtils.init(context);

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功
                //否则表示x5内核加载失败，会自动切换到系统内核。
                if (!SharedPreferenceUntils.getX5IsInstallFinish(context)&&arg0)
                      SharedPreferenceUntils.saveX5State(context, true);

            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(context,  cb);
        X5LogUtils.setIsLog(false);
        QbSdk.setTbsListener(new TbsListener(){

            @Override
            public void onDownloadFinish(int i) {

            }

            @Override
            public void onInstallFinish(int i) {
                if (i == 232){//经过测试232 是最后一次收到的状态{
                    SharedPreferenceUntils.saveX5State(context,true);
                    EventBus.getDefault().post(new X5InstallSuccessBean(1,100));
                   // MyToast.myToast(context,"安装x5内核成功");
                }else {
                    EventBus.getDefault().post(new X5InstallSuccessBean(1,0));
                }
            }

            @Override
            public void onDownloadProgress(int i) {
                EventBus.getDefault().post(new X5InstallSuccessBean(0,i));
               // MyToast.myToast(context,"正在后台下载安装x5内核");
            }
        });

        Constants.init(context);//初始化 检查debug或者正式版本
        initGSYVideoSetting();
    }

    /**
     * 初始化gsyvideo一些基础配置
     */
    private static void initGSYVideoSetting(){
        //尝试降低倍数 视频声音画面不同步
        VideoOptionModel videoOptionModel = new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 30);
        List<VideoOptionModel> list = new ArrayList<>();
        list.add(videoOptionModel);
        GSYVideoManager.instance().setOptionModelList(list);
        PlayerFactory.setPlayManager(Exo2PlayerManager.class);//使用系统解码器全屏切换会卡顿黑屏

        //exo缓存模式，支持m3u8，只支持exo
        // CacheFactory.setCacheManager(ExoPlayerCacheManager.class);
        GSYVideoType.setRenderType(GSYVideoType.GLSURFACE);
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


    public static void startHistoryPriceActivity(Context context){
        context.startActivity(new Intent(context, StatisticPriceActivity.class));
    }
    /**
     *统计猪历史价格
     * @param context
     * @param type 0 大豆 1 玉米 2 内三元 ...
     */
    public static void startHistoryPriceActivity(Context context,String type){
        context.startActivity(new Intent(context,StatisticPriceActivity.class).putExtra("type",type));
    }
    /**
     *
     */
    public static void startLiveDetailActivity(FragmentActivity activity,String id,Boolean isLive){
        activity.startActivity(new Intent(activity, LiveDetailActivity.class).putExtra("id",id).putExtra(Constants.EXTRA_IS_LIVE,isLive));
    }
    public static void startLiveDetailActivity(FragmentActivity activity,String id,Boolean isLive,int position){
        activity.startActivity(new Intent(activity, LiveDetailActivity.class).putExtra("id",id)
                .putExtra(Constants.EXTRA_IS_LIVE,isLive).putExtra("position",position));
    }
 /*   public static void startLiveDetailActivity(FragmentActivity activity, String id, String thumb, View view){
        ActivityUtil.startTransitionAnimationActivity(activity,new Intent(activity, LiveDetailActivity.class).putExtra("id",id).putExtra("thumb",thumb),
                view,activity.getResources().getString(R.string.share_str_ClassRoom));
    }*/

    /**
     * 返回码100 正在直播 返回码 101直播结束
     * @param activity
     * @param id
     * @param isLive
     * @param requestCode
     */
 public static void startForResultLiveDetailActivity(FragmentActivity activity,String id,Boolean isLive,int requestCode)
    {
     activity.startActivityForResult(new Intent(activity, LiveDetailActivity.class).putExtra("id",id).putExtra(Constants.EXTRA_IS_LIVE,isLive),requestCode);
     }
    /***
     * @param activity
     * @param url
     * @param isProduct 是否是产品， 如果是产品就会有咨询按钮
     */
    public static void startWebActivity(FragmentActivity activity,String url,boolean isProduct,String title){
        if (!SharedPreferenceUntils.getX5IsInstallFinish(activity)){//没有加载完成提示用户
            CenterQuiteDialog centerQuiteDialog =   new CenterQuiteDialog();
            centerQuiteDialog.setCallback(integer ->     activity.startActivity(new Intent(activity, LiveWebActivity.class).putExtra("url",url).putExtra("isProduct",isProduct).putExtra("title",title)));

            centerQuiteDialog.showDialog(activity.getSupportFragmentManager());
            return;
        }
        activity.startActivity(new Intent(activity, LiveWebActivity.class).putExtra("url",url).putExtra("isProduct",isProduct).putExtra("title",title));
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
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())|| !Environment.isExternalStorageRemovable()) {
            videoCachePath = context.getExternalCacheDir().getPath();
        }else{
            videoCachePath = context.getCacheDir().getPath();
        }
        videoCachePath = videoCachePath + "/cachePath/";
        return videoCachePath;
    }
}
