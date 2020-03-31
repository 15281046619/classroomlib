package com.xingwang.classroomlib;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.xingwang.classroom.ClassRoomLibUtils;


/**
 * Date:2020/3/26
 * Time;9:39
 * author:baiguiqiang
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ClassRoomLibUtils.initLib(this,ClassRoomLibUtils.TYPE_ZY);
    //    X5WebUtils.init(this);
      //  X5LogUtils.setIsLog(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
