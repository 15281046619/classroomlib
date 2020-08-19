package com.xingwang.classroom.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;


/**
 * Date:2020/8/6
 * Time;14:10
 * author:baiguiqiang
 */
public class ActivityUtil {

    public static  void startTransitionAnimationActivity(Activity activity, Intent intent, View view,String shareStr){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            activity.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity,
                    view,
                    shareStr)
                    .toBundle());
        }else {
            activity.startActivity(intent);
        }
    }
    public static void finishTransitionAnimation(Activity activity){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            activity.finishAfterTransition();
        }else {
            activity.finish();
        }
    }
}
