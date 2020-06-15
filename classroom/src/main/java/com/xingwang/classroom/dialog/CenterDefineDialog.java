package com.xingwang.classroom.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.X5InstallSuccessBean;
import com.xingwang.classroom.utils.LogUtil;
import com.xingwang.classroom.utils.MyToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class CenterDefineDialog extends BaseDialog {

    private Button btOk;

    @Override
    protected int layoutResId() {
        return R.layout.dialog_center_fine_classroom;
    }
    private Callback1<Integer> callback1;


    public static CenterDefineDialog getInstance(String content){
        CenterDefineDialog instance = new CenterDefineDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content",content);
        instance.setArguments(bundle);
        return instance;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getContext(),R.style.DialogCenterClassRoom);
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
        super.dismissDialog();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findViewById(R.id.btCancel).setOnClickListener(v -> dismissDialog());
        ((TextView) findViewById(R.id.tvDes)).setText(getArguments().getString("content"));
        btOk = findViewById(R.id.btOk);
        btOk.setOnClickListener(v -> {
            if (callback1!=null){
                callback1.run(0);
            }
            dismissDialog();
        });
    }

    public void setCallback(Callback1<Integer> callback1 ){
        this.callback1 = callback1;
    }
    public  interface Callback1<T> {
        void run(T t);
    }
}
