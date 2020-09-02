package com.xingwang.swip.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.wang.avi.AVLoadingIndicatorView;
import com.xingwang.swip.R;


/**
 * loading对话框
 *
 */
public class DialogLoading extends Dialog {


    private AVLoadingIndicatorView avi;
    private Context context;

    public DialogLoading(@NonNull Context context) {
        super(context, R.style.swip_customDialogTheme);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.swip_dialog_loading);

        avi=findViewById(R.id.avi);

        setViewLocation();
        //外部点击取消
        setCanceledOnTouchOutside(false);
        //默认可以点击取消
        isBack(true);
    }

    /**
     * 是否可以点击返回键取消
     *
     * @param isBack
     */
    public void isBack(boolean isBack) {
        if (isBack) {
            setOnKeyListener(null);
        } else {
            setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void show() {
        super.show();
        avi.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (avi!=null)
        avi.hide();
    }

    /**
     * 设置dialog位于屏幕底部
     */
    private void setViewLocation() {
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        onWindowAttributesChanged(lp);
    }
}