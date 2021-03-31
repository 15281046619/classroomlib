package com.xingwang.classroom.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xingwang.classroom.R;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.dialog.BaseDialog;

public class BottomADSheetDialog extends BaseDialog {

    @Override
    protected int layoutResId() {
        return R.layout.dialog_bottom_ad_sheet_classroom;
    }
    private Callback1<Integer> callback1;


    public static BottomADSheetDialog getInstance(String imgUrl,int mDialogHeight,int mDialogWidth,float verticalMargin){
        BottomADSheetDialog instance = new BottomADSheetDialog();
        Bundle bundle = new Bundle();
        bundle.putString("imgurl",imgUrl);
        bundle.putInt("mDialogHeight",mDialogHeight);
        bundle.putInt("mDialogWidth",mDialogWidth);
        bundle.putFloat("verticalMargin",verticalMargin);
        instance.setArguments(bundle);
        return instance;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        Window window = dialog.getWindow();
        if (window!=null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (getArguments()!=null) {
                layoutParams.width = getArguments().getInt("mDialogWidth");
                layoutParams.height = getArguments().getInt("mDialogHeight");
                if (getArguments().getFloat("verticalMargin") == 0)
                    layoutParams.gravity = Gravity.BOTTOM | Gravity.END;
                else {
                    layoutParams.gravity = Gravity.TOP | Gravity.END;
                }
            }


            window.setAttributes(layoutParams);
        }
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RelativeLayout rlAd =findViewById(R.id.rl_ad);

        rlAd.setOnClickListener(v -> dismissDialog());
        findViewById(R.id.iv_close_ad).setOnClickListener(v -> dismissDialog());
        ImageView ivAda =findViewById(R.id.iv_ad);
        if (getArguments()!=null)
        GlideUtils.loadAnimateAvatar(getArguments().getString("imgurl"),ivAda);
        ivAda.setOnClickListener(v -> {
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
