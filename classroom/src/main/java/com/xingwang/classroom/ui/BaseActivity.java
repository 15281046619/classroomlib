package com.xingwang.classroom.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.SystemDefine;
import com.xingwang.classroom.utils.ActivityManager;

import static android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

/**
 * Date:2019/8/19
 * Time;13:58
 * author:baiguiqiang
 */
public abstract class BaseActivity extends AppCompatActivity {
    @LayoutRes
    protected abstract int layoutResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null&&savedInstanceState.getBoolean("isClean")){
            //finish();
            ActivityManager.getInstance().finishAllActivity();
        }

        transparentStatusBar();
        setContentView(layoutResId());
        ActivityManager.getInstance().addActivity(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isClean",true);
        super.onSaveInstanceState(outState);
    }
    protected void setNavigationBarColor( @ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, id));
        }
    }

    /**
     *  super.onCreate(savedInstanceState)后面判断
     * 是否启动launch页面 启动的话 不在执行下面代码
     * @return
     */
    public boolean isStartLaunch(){
        if (getIntent().getData()!=null){
            SystemDefine systemDefine = BeautyDefine.getSystemDefine();
            if (!systemDefine.checkLaunch(this)){
                systemDefine.launch(this,getIntent().getData().toString());
                return true;
            }
        }
        return false;
    }
    /*  @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();    //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, event)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken()) ;  //收起键盘
            }
        }
        return super.dispatchTouchEvent(event);
    }*/

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *

     * @return
     */
    public Boolean isShouldHideKeyboard(View v,MotionEvent  event) {

        if (v instanceof EditText) {  //判断得到的焦点控件是否包含EditText
            int[] l = new int[]{0, 0};
            v.getLocationInWindow(l);
            int left = l[0];
            //得到输入框在屏幕中上下左右的位置
            int top = l[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略
        return false;
    }
    /**
     * 获取InputMethodManager，隐藏软键盘

     */
    public void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 透明状态栏
     */
    void transparentStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor( Color.TRANSPARENT);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
    }
}
