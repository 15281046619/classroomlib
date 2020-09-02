package com.xingwang.circle.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xingwang.circle.R;
import com.xingwang.circle.base.BaseDialog;

public class BottomSheetDialog extends BaseDialog {

    private TextView tv_shotVieo;
    private TextView tv_take_photo;
    private LinearLayout line_full;

    @Override
    protected int layoutResId() {
        return R.layout.circle_dialog_sheet;
    }
    private CardDialogCallback dialogCallback;

    public static BottomSheetDialog getInstance(){
        BottomSheetDialog instance = new BottomSheetDialog();
        return instance;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        dialog.setCanceledOnTouchOutside(true);
        layoutParams.windowAnimations = R.style.circle_BottomSheetStyle;
        window.setAttributes(layoutParams);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_shotVieo=findViewById(R.id.tv_shotVieo);
        tv_take_photo=findViewById(R.id.tv_take_photo);
        line_full=findViewById(R.id.line_full);

        line_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        tv_shotVieo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (dialogCallback!=null){
                   dialogCallback.shotVideo();
                   dismissDialog();
               }
            }
        });

        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogCallback!=null){
                    dialogCallback.takePhoto();
                    dismissDialog();
                }
            }
        });
    }


    public void setCallback(CardDialogCallback dialogCallback ){
        this.dialogCallback = dialogCallback;
    }
    public  interface CardDialogCallback {
        void shotVideo();
        void takePhoto();
    }
}
