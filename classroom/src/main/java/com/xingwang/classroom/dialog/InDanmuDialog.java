package com.xingwang.classroom.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.xingwang.classroom.R;

public class InDanmuDialog extends BaseDialog{


    public static InDanmuDialog getInstance(){
        InDanmuDialog instance = new InDanmuDialog();
        return instance;
    }

    @Override
    protected int layoutResId() {
        return R.layout.dialog_in_danmu;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        WindowManager.LayoutParams att = dialog.getWindow().getAttributes();
        att.height= ViewGroup.LayoutParams.WRAP_CONTENT;
        att.width=ViewGroup.LayoutParams.MATCH_PARENT;
        att.gravity = Gravity.BOTTOM;
        return dialog;
    }
}
