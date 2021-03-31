package com.xinwang.bgqbaselib.utils;

import android.app.Activity;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.xinwang.bgqbaselib.R;

/**
 * Date:2021/3/22
 * Time;14:40
 * author:baiguiqiang
 */
public class FragmentUtils {
    private static  void AddFragment(FragmentActivity activity, @IdRes int idRes, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction().add(idRes, fragment) .commitAllowingStateLoss();
    }
    private static  void AddChildFragment(Fragment activity, @IdRes int idRes, Fragment fragment) {
        activity.getChildFragmentManager().beginTransaction().add(idRes, fragment) .commitAllowingStateLoss();
    }

    /**
     * 显示fragment
     * @param activity
     * @param idRes
     * @param fragment
     */
    public static  void showFragment(FragmentActivity activity, @IdRes int idRes, Fragment fragment) {

        if (fragment.isAdded())
            activity.getSupportFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
        else
            AddFragment(activity,idRes,fragment);

    }

    /**
     * 替换fragment
     * @param activity
     * @param idRes
     * @param fragment
     */
    public static void replaceFragment(FragmentActivity activity, @IdRes int idRes, Fragment fragment){
        activity.getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.alpha_out_shoppingcneter
                ,R.anim.alpha_in_shoppingcneter).replace(idRes,fragment).commitAllowingStateLoss();
    }
    /**
     * fragment fragment
     * @param parentFragment
     * @param idRes
     * @param fragment
     */
    public static  void showChildFragment(Fragment parentFragment,@IdRes int idRes, Fragment fragment) {
        if (fragment.isAdded())
            parentFragment.getChildFragmentManager().beginTransaction().show(fragment).commitAllowingStateLoss();
        else
            AddChildFragment(parentFragment,idRes,fragment);

    }
}
