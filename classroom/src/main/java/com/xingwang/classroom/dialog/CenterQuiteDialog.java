package com.xingwang.classroom.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.tencent.smtt.sdk.QbSdk;
import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.X5InstallSuccessBean;
import com.ycbjie.webviewlib.WebProgress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class CenterQuiteDialog extends BaseDialog {

    private Button btOk;
    private WebProgress progres;
    @Override
    protected int layoutResId() {
        return R.layout.dialog_quite_classroom;
    }
    private Callback1<Integer> callback1;


    public static CenterQuiteDialog getInstance(){
        CenterQuiteDialog instance = new CenterQuiteDialog();
        return instance;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getContext(),R.style.DialogCenterClassRoom);
        EventBus.getDefault().register(this);
        dialog.setContentView(layoutResId());
        Window window = dialog.getWindow();
        if (window!=null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);

        }
        return dialog;
    }



    @Override
    public void dismissDialog() {
        if (btOk!=null){
            btOk.removeCallbacks(runnable);
        }
        EventBus.getDefault().unregister(this);
        super.dismissDialog();
    }

    Runnable runnable =new Runnable() {
        @Override
        public void run() {
            if (callback1!=null) {
                callback1.run(0);
                dismissDialog();
            }
        }
    };
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findViewById(R.id.btCancel).setOnClickListener(v -> dismissDialog());
        findViewById(R.id.btCancel).setOnClickListener(v -> dismissDialog());
        btOk = findViewById(R.id.btOk);
        progres = findViewById(R.id.progress);
        progres.show();
        progres.setColor(this.getResources().getColor(R.color.colorAccentClassRoom));
      //  btOk.removeCallbacks(runnable);
     //   btOk.postDelayed(runnable,2000);
        findViewById(R.id.btOk).setOnClickListener(v -> {
            if (callback1!=null){
                callback1.run(0);
            }
            dismissDialog();
        });
    }
    @Subscribe()
    public void notifacationClose(X5InstallSuccessBean x5InstallSuccessBean){
        if (x5InstallSuccessBean.getType()==1) {
            btOk.removeCallbacks(runnable);
            btOk.postDelayed(runnable, 1000);//安装成功延迟两秒提醒安装成功。否则进入x5内核可能卡顿
        }else {
            progres.setProgress(x5InstallSuccessBean.getProgress()/10);
        }
    }
    public void setCallback(Callback1<Integer> callback1 ){
        this.callback1 = callback1;
    }
    public  interface Callback1<T> {
        void run(T t);
    }
}
