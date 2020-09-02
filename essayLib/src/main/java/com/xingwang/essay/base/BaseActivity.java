package com.xingwang.essay.base;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.Utils;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.xingwang.essay.EssayListActivity;
import com.xingwang.essay.R;
import com.xingwang.swip.utils.ActivityManager;
import com.xingwang.swip.utils.Constants;


public abstract class BaseActivity extends AppCompatActivity {
    private boolean isSwipeBack = !isBackEnable();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.init(this);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        if (isSwipeBack){
            SwipeBackHelper.onCreate(this);
            SwipeBackHelper.getCurrentPage(this)//get current instance
                    .setSwipeEdge(200)//set the touch area。200 mean only the left 200px of screen can touch to begin swipe.
                    .setSwipeEdgePercent(0.2f)//0.2 mean left 20% of screen can touch to begin swipe.
                    .setSwipeSensitivity(0.5f)//sensitiveness of the gesture。0:slow  1:sensitive
                    .setClosePercent(0.8f)//close activity when swipe over this
                    .setSwipeRelateEnable(false)//if should move together with the following Activity
                    .setSwipeRelateOffset(500);//the Offset of following Activity when setSwipeRelateEnable(true)
        }


        ActivityManager.getInstance().addActivity(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            // 标记可改变 SYSTEM_BAR 颜色，加了这一行才可以修改 状态栏颜色
            // 如果不加这行则 需要在相应的activity 的 Theme 属性中标记
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getStatusBarColor());
        }
        initView();
        initData();
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getNavigationBarColor());
        }*/
    }
    protected int getNavigationBarColor(){
        return ContextCompat.getColor(getApplication(), R.color.reslib_colorWindowContentBalck);
    }

    private boolean isBackEnable(){
        return this instanceof EssayListActivity;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (isSwipeBack)
            SwipeBackHelper.onPostCreate(this);
    }


    public abstract int getLayoutId();
    protected void initView(){}
    protected int getStatusBarColor(){
        return getResources().getColor(R.color.reslib_colorWindowContentBalck);
    }

    public abstract  void initData();

    protected   void startActivity(Class<?> cla,Bundle bundle){
        startActivity(new Intent(this,cla).putExtra(Constants.INTENT_DATA,bundle));
    }
    protected void startActivityForResult(Class<?> cls,int requestCode){startActivityForResult (new Intent(this,cls),requestCode);}
    protected void startActivityForResult(Class<?> cls,Bundle bundle,int requestCode){
        startActivityForResult (new Intent(this,cls).putExtra(Constants.INTENT_DATA,bundle),requestCode);}
    protected void startActivity(Class<?> cls){startActivity(new Intent(this,cls));}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
        if (isSwipeBack)
            SwipeBackHelper.onDestroy(this);
    }
    public void setTransparentStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0及以上
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4到5.0
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags;
        }
    }


}
