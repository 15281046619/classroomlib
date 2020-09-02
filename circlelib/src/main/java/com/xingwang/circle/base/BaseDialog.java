package com.xingwang.circle.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xingwang.circle.R;

public abstract class BaseDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.circle_DialogStyle);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        dialog.setContentView(layoutResId());
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        window.setAttributes(layoutParams);
        return dialog;
    }
    protected abstract int layoutResId();

    public   <T extends View>  T findViewById( @IdRes int  viewId){
        return getDialog().findViewById(viewId);
    }
    public void showDialog(FragmentManager fragmentManager, String tag){
        if (!isAdded()&&fragmentManager!=null){
            fragmentManager.beginTransaction().add(this,tag).commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
    }
    public void showDialog(FragmentManager fragmentManager){
        showDialog(fragmentManager,"");
    }

    public void dismissDialog(){
        dismissAllowingStateLoss();
    }
}
