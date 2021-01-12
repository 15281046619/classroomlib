package com.xingwang.classroom.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xingwang.classroom.R;
import com.xingwang.classroom.utils.GlideUtils;
import com.xingwang.classroom.view.CircularImage;

/**
 * 提示用户送礼物
 */
public class BottomGifSubmitDialog extends BaseDialog {

    @Override
    protected int layoutResId() {
        return R.layout.dialog_bottom_gif_submit_classroom;
    }
    private Callback1<Integer> callback1;


    public static BottomGifSubmitDialog getInstance(String imgUrl, String name){
        BottomGifSubmitDialog instance = new BottomGifSubmitDialog();
        Bundle bundle = new Bundle();
        bundle.putString("imgurl",imgUrl);
        bundle.putString("name",name);
        instance.setArguments(bundle);
        return instance;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.DialogGifSubmitStyleClassRoom);
//        dialog.setCancelable(false)
//        dialog.setCanceledOnTouchOutside(false)
        Window window = dialog.getWindow();
        dialog.setContentView(layoutResId());
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.windowAnimations = R.style.BottomSheetStyleClassRoom;

        window.setAttributes(layoutParams);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CircularImage ivAvatar =findViewById(R.id.ivAvatar);
        GlideUtils.loadAvatar(getArguments().getString("imgurl"),ivAvatar);
        TextView tvName = findViewById(R.id.tvName);
        tvName.setText("Hi,"+getArguments().getString("name"));
        findViewById(R.id.btSend).setOnClickListener(v -> {
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
