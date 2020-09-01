package com.xingwang.classroom.dialog;

import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingwang.classroom.R;
import com.xingwang.classroom.utils.GlideUtils;
import com.xingwang.classroom.view.CircularImage;
import com.xingwang.classroom.view.MyFrameAnimation;


public class CenterRedPackDialog extends BaseDialog {



    @Override
    protected int layoutResId() {
        return R.layout.dialog_red_pack_classroom;
    }



    public static CenterRedPackDialog getInstance(String headUrl,String name,String tip){
        CenterRedPackDialog instance = new CenterRedPackDialog();
        Bundle bundle = new Bundle();
        bundle.putString("headUrl",headUrl);
        bundle.putString("name",name);
        bundle.putString("tip",tip);
        instance.setArguments(bundle);
        return instance;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getContext(),R.style.DialogCenterRedPackClassRoom);
        dialog.setContentView(layoutResId());
        Window window = dialog.getWindow();
        if (window!=null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);

        }
        return dialog;
    }


    private MyFrameAnimation animation;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView imageView = findViewById(R.id.ivThumb);
        CircularImage ivHead = findViewById(R.id.ivHead);
        TextView tvCenter = findViewById(R.id.tvCenter);
        TextView tvName = findViewById(R.id.tvName);
        findViewById(R.id.ivClose).setOnClickListener(v -> dismissDialog());
      /*  SpannableString spannableString =new SpannableString();
        spannableString.setSpan(new RelativeSizeSpan(1.3f),5,6,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);*/
        tvCenter.setText(getArguments().getString("tip"));
        tvName.setText(getArguments().getString("name")+"的红包");
        GlideUtils.loadAvatar(getArguments().getString("headUrl"),ivHead);
        imageView.setOnClickListener(v -> {
            if (animation==null) {
                animation = new MyFrameAnimation();
                animation.setOnFrameAnimationListener(new MyFrameAnimation.OnFrameAnimationListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onEnd() {
                        animation.stop();
                        tvCenter.setVisibility(View.VISIBLE);
                    }
                });
                //添加单帧
                animation.addFrame(ContextCompat.getDrawable(getContext(), R.mipmap.ic_pack_open_classroom), 0);
                animation.addFrame(ContextCompat.getDrawable(getContext(), R.mipmap.ic_pack_5_classroom), 150);
                animation.addFrame(ContextCompat.getDrawable(getContext(), R.mipmap.ic_pack_3_classroom), 150);
                animation.addFrame(ContextCompat.getDrawable(getContext(), R.mipmap.ic_pack_4_classroom), 150);
                animation.addFrame(ContextCompat.getDrawable(getContext(), R.mipmap.ic_pack_2_classroom), 150);
                animation.addFrame(ContextCompat.getDrawable(getContext(), R.mipmap.ic_pack_1_classroom), 150);


                //设置给ImageView
                imageView.setImageDrawable(animation);
                imageView.setBackgroundResource(0);
                animation.start();
            }
        });
    }


    public  interface Callback1<T> {
        void run(T t);
    }
}
