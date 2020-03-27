package com.xingwang.classroom;

import android.app.Application;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;
import com.xingwang.classroom.bean.X5InstallSuccessBean;
import com.xingwang.classroom.utils.LogUtil;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.utils.SharedPreferenceUntils;
import com.ycbjie.webviewlib.X5LogUtils;
import com.ycbjie.webviewlib.X5WebUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

/**
 * Date:2020/3/26
 * Time;9:39
 * author:baiguiqiang
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if(!QbSdk.isTbsCoreInited()) {
            HashMap map = new HashMap();
            map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
            map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
            QbSdk.initTbsSettings(map);
            X5WebUtils.init(this);
            X5LogUtils.setIsLog(false);
            QbSdk.setTbsListener(new TbsListener(){

                @Override
                public void onDownloadFinish(int i) {
                    EventBus.getDefault().post(new X5InstallSuccessBean(0,i));
                    LogUtil.e("TAG","onDownloadFinish"+i);
                }

                @Override
                public void onInstallFinish(int i) {
                    LogUtil.e("TAG","onInstallFinish"+i);
                    if (i == 232){//经过测试232 是最后一次收到的状态{
                        SharedPreferenceUntils.saveX5State(MyApplication.this,true);
                        EventBus.getDefault().post(new X5InstallSuccessBean(1,100));
                        MyToast.myToast(MyApplication.this,"安装x5内核成功");
                    }else {
                        EventBus.getDefault().post(new X5InstallSuccessBean(1,0));
                    }
                }

                @Override
                public void onDownloadProgress(int i) {
                    EventBus.getDefault().post(new X5InstallSuccessBean(0,i));
                    LogUtil.e("TAG","onDownloadProgress"+i);
                    MyToast.myToast(MyApplication.this,"正在后台下载安装x5内核");
                }
            });

        }else {
            SharedPreferenceUntils.saveX5State(this,true);
        }
    }
}
