package com.xingwang.groupchat.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xingwang.groupchat.R;


/**
 * Created by private on 2017/8/15.
 */

public class BottomPopWindow {

    private Context context;
    private Dialog popupWindow;
    private View view;
    private Button btn_photo;//拍照
    private Button btn_album;//相册
    private Button btn_cancel;//取消
    private OnBottomClickEvent mEvent;
    private LinearLayout album_ly;

    public BottomPopWindow(Context context) {
        this.context = context;
        initPopup();
    }

    public BottomPopWindow(Context context, boolean flag) {
        this.context = context;
        initPopup(flag);
    }

    private void initPopup(boolean flag) {
        view = LayoutInflater.from(context)
                .inflate(R.layout.groupchat_pop_bottom, null);
        view.setBackgroundColor(Color.DKGRAY);

        btn_photo = (Button) view.findViewById(R.id.btn_photo);
        btn_album = (Button) view.findViewById(R.id.btn_album);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        if (flag) {
            album_ly = (LinearLayout) view.findViewById(R.id.album_ly);
            album_ly.setVisibility(View.GONE);
            btn_photo.setText("相册");
        }
        popupWindow = new Dialog(context);

        popupWindow.setContentView(view,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = popupWindow.getWindow();
        window.setBackgroundDrawableResource(R.color.groupchat_transparent);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);

        popupWindow.setCanceledOnTouchOutside(false);
    }

    private void initPopup() {
        view = LayoutInflater.from(context)
                .inflate(R.layout.groupchat_pop_bottom, null);
        view.setBackgroundColor(Color.DKGRAY);

        btn_photo = (Button) view.findViewById(R.id.btn_photo);
        btn_album = (Button) view.findViewById(R.id.btn_album);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        popupWindow = new Dialog(context);

        popupWindow.setContentView(view,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = popupWindow.getWindow();
        window.setBackgroundDrawableResource(R.color.groupchat_transparent);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);

        popupWindow.setCanceledOnTouchOutside(false);
    }

    /**
     * 显示popupWindow
     */
    public void show() {
        popupWindow.show();
        initEvent();
    }

    public void dismiss() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public void setOnMyPopClickEvent(OnBottomClickEvent mEvent) {
        this.mEvent = mEvent;
    }

    private void initEvent() {
        if (mEvent != null) {
            btn_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mEvent.takePhoto();
                    popupWindow.dismiss();
                }
            });
            btn_album.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mEvent.photoAlbum();
                    popupWindow.dismiss();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mEvent.cancel();
                    popupWindow.dismiss();
                }
            });

        }
    }

    public interface OnBottomClickEvent {
        void takePhoto();//拍照

        void photoAlbum();//相册

        void cancel();//取消
    }
}
