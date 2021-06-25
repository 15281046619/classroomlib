package com.xinwang.shoppingcenter.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinwang.bgqbaselib.R;
import com.xinwang.bgqbaselib.dialog.BaseDialog;
import com.xinwang.bgqbaselib.utils.CommentUtils;

/**
 * 选择页面
 */
public class ShoppingEditChooseDialog extends BaseDialog {

    @Override
    protected int layoutResId() {
        return R.layout.dialog_shopping_edit_choose_classroom;
    }
    private Callback1<Integer> callback1;


    public static ShoppingEditChooseDialog getInstance(String[] mString){
        ShoppingEditChooseDialog instance = new ShoppingEditChooseDialog();
        Bundle bundle = new Bundle();
        bundle.putStringArray("content",mString);
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
        String[] mLists =  getArguments().getStringArray("content");
        LinearLayout llContent = findViewById(R.id.llContent);
        for (int i=0;i<mLists.length;i++){
            TextView textView =new TextView(getContext());
            textView.setBackgroundResource(android.R.color.white);
            textView.setGravity(Gravity.CENTER);
            textView.setText(mLists[i]);
            int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (callback1!=null){
                        callback1.run(finalI);
                    }
                    dismissDialog();
                }
            });
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommentUtils.dip2px(getContext(), 48));
            if (i!=0)
            mLayoutParams.setMargins(0,CommentUtils.dip2px(getContext(),1),0,0);
            llContent.addView(textView,mLayoutParams);
        }






    }

    public ShoppingEditChooseDialog setCallback(Callback1<Integer> callback1 ){
        this.callback1 = callback1;
        return this;
    }
    public  interface Callback1<T> {
        void run(T t);
    }
}
