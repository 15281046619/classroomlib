package com.xingwang.classroom.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.contrarywind.view.WheelView;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.BottomDialogWheelAdapter;
import com.xingwang.classroom.view.CircularImage;
import com.xinwang.bgqbaselib.dialog.BaseDialog;
import com.xinwang.bgqbaselib.utils.GlideUtils;

import java.util.ArrayList;

/**
 * 年月选择
 */
public class BottomChooseMonAndYearDialog extends BaseDialog {

    @Override
    protected int layoutResId() {
        return R.layout.dialog_choose_month_and_year_classroom;
    }
    private Callback1<String> callback1;


    public static BottomChooseMonAndYearDialog getInstance(ArrayList<String> mYearData,ArrayList<String> mMonthData,int  mYearPos,int mMonthPos){
        BottomChooseMonAndYearDialog instance = new BottomChooseMonAndYearDialog();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("list",mYearData);
        bundle.putStringArrayList("list1",mMonthData);
        bundle.putInt("position",mYearPos);
        bundle.putInt("position1",mMonthPos);
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
        WheelView mWheelView1 = findViewById(R.id.wheel_view1);
        assert getArguments() != null;
        ArrayList<String> mData =getArguments().getStringArrayList("list");
        ArrayList<String> mData1 =getArguments().getStringArrayList("list1");
        mWheelView.setAdapter(new  BottomDialogWheelAdapter(mData));
        mWheelView.setLineSpacingMultiplier(8f/3);
        mWheelView.setCurrentItem(getArguments().getInt("position"));

        mWheelView1.setAdapter(new  BottomDialogWheelAdapter(mData1));
        mWheelView1.setLineSpacingMultiplier(8f/3);
        mWheelView1.setCurrentItem(getArguments().getInt("position1"));
        findViewById(R.id.tv_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback1!=null) {
                    callback1.run((mWheelView.getCurrentItem()+2000)+"-" +(mWheelView1.getCurrentItem()<9?("0"+(mWheelView1.getCurrentItem()+1)):(mWheelView1.getCurrentItem()+1)));
                }
                dismissDialog();
            }
        });


    }


    public void setCallback(Callback1<String> callback1 ){
        this.callback1 = callback1;
    }
    public  interface Callback1<T> {
        void run(T t);
    }
}
