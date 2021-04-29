package com.xinwang.bgqbaselib.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


import com.xinwang.bgqbaselib.R;

public class CenterDefineDialog extends BaseDialog {

    private Button btOk;
    private Boolean isCancelable;
    @Override
    protected int layoutResId() {
        return R.layout.dialog_center_fine_classroom;
    }
    private Callback1<Integer> callback1;


    public static CenterDefineDialog getInstance(String content){
        CenterDefineDialog instance = new CenterDefineDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content",content);
        bundle.putBoolean("isCancelable",true);
        instance.setArguments(bundle);
        return instance;
    }
    public static CenterDefineDialog getInstance(String content,boolean isCancelable){
        CenterDefineDialog instance = new CenterDefineDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content",content);
        bundle.putBoolean("isCancelable",isCancelable);
        instance.setArguments(bundle);
        return instance;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getContext(),R.style.DialogCenterClassRoom);
        dialog.setContentView(layoutResId());

       // setCancelable(isCancelable);
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
        Button  btCancel = findViewById(R.id.btCancel);
        btOk = findViewById(R.id.btOk);
        isCancelable =getArguments().getBoolean("isCancelable");
        if (!isCancelable) {
            btCancel.setText("否");
            btOk.setText("是");
        }
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback1!=null&&!isCancelable){
                    callback1.run(1);
                }
                dismissDialog();
            }
        });

        ((TextView) findViewById(R.id.tvDes)).setText(getArguments().getString("content"));

        btOk.setOnClickListener(v -> {
            if (callback1!=null){
                callback1.run(0);
            }
            dismissDialog();
        });
    }

    public CenterDefineDialog setCallback(Callback1<Integer> callback1 ){
        this.callback1 = callback1;
        return this;
    }
    public  interface Callback1<T> {
        void run(T t);
    }
}
