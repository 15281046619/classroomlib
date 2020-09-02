package com.xingwang.groupchat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.Utils;
import com.xingwang.swip.utils.ActivityManager;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.view.DialogLoading;

public abstract class BaseActivity extends AppCompatActivity {
    protected DialogLoading mLoadingDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.init(this);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

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
    }

    public abstract int getLayoutId();
    protected void initView(){}
    protected int getStatusBarColor(){
        return getResources().getColor(R.color.reslib_colorWindowContentBalck);
    }

    public abstract  void initData();

    protected  void startActivity(Class<?> cla, Bundle bundle){
        startActivity(new Intent(this,cla).putExtra(Constants.INTENT_DATA,bundle));
    }
    protected void startActivityForResult(Class<?> cls, int requestCode){startActivityForResult (new Intent(this,cls),requestCode);}
    protected void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode){
        startActivityForResult (new Intent(this,cls).putExtra(Constants.INTENT_DATA,bundle),requestCode);}
    protected void startActivity(Class<?> cls){startActivity(new Intent(this,cls));}

    protected void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new DialogLoading(this);
        }
        mLoadingDialog.show();
    }

    protected void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog= null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
    }
}
