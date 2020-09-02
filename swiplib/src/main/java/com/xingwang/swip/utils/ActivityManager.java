package com.xingwang.swip.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import java.util.Stack;

public class ActivityManager {

    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {}

    /**
     * 单一实例
     */
    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {

        activityStack.add(activity);
    }
    public Stack<Activity> getActivityStack(){
        for (int i=0;i<activityStack.size();i++){
            Log.i("ActivityManager",activityStack.get(i).getLocalClassName());
        }
        return activityStack;
    }
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityStack.size()==0){
            return null;
        }else {
            Activity activity = activityStack.lastElement();
            return activity;
        }
    }
    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * 结束上一个activity
     */
    public void finishLastActivity(){
        if (activityStack.size()>=2) {
           Activity activity = activityStack.elementAt(activityStack.size() - 2);
           if (activity!=null)
               activity.finish();
        }
    }
    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 结束指定类名的Activity
     */
   public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().getName().equals(cls.getName())) {
                activity.finish();
            }
        }
    }

    /**
     * 寻找指定类名的Activity
     */
   public boolean findActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().getName().equals(cls.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    public static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            ContextWrapper wrapper = (ContextWrapper) context;
            return findActivity(wrapper.getBaseContext());
        } else {
            return null;
        }
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

} 