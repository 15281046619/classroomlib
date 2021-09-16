package com.xingwang.classroom.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.contrarywind.view.WheelView;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.BottomDialogWheelAdapter;
import com.xinwang.bgqbaselib.dialog.BaseDialog;

import java.util.ArrayList;

/**
 *底部选择
 */
public class BottomChooseDialog extends BaseDialog {

    @Override
    protected int layoutResId() {
        return R.layout.dialog_choose_classroom;
    }
    private Callback1<Integer> callback1;


    public static BottomChooseDialog getInstance(ArrayList<String> mData,  int  mPos){
        BottomChooseDialog instance = new BottomChooseDialog();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("list",mData);
        bundle.putInt("position",mPos);
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
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        WheelView mWheelView = findViewById(R.id.wheel_view);
        assert getArguments() != null;
        ArrayList<String> mData =getArguments().getStringArrayList("list");
        mWheelView.setAdapter(new  BottomDialogWheelAdapter(mData));
        mWheelView.setLineSpacingMultiplier(8f/3);
        mWheelView.setCurrentItem(getArguments().getInt("position"));

        findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback1!=null) {
                    callback1.run(mWheelView.getCurrentItem());
                }
                dismissDialog();
            }
        });


    }


    public void setCallback(Callback1<Integer> callback1 ){
        this.callback1 = callback1;
    }
    public  interface Callback1<T> {
        void run(T t);
    }
}
