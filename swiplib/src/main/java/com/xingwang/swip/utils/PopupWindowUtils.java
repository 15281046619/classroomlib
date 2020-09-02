package com.xingwang.swip.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.ConvertUtils;
import com.xingwang.swip.R;
import com.xingwang.swip.callback.OnItemClickListener;
import com.xingwang.swip.callback.OnItemLongClickListener;
import com.xingwang.swip.view.WrapContentLinearLayoutManager;
import com.xingwang.swip.view.popup.PopupWindowListAdapter;
import com.xingwang.swip.view.popup.SupportPopupWindow;

import java.util.Arrays;


public class PopupWindowUtils {
    /**
     * @param activity      Activity
     * @param attachOnView  显示在这个View的下方
     * @param popView       被显示在PopupWindow上的View
     * @param popShowHeight 被显示在PopupWindow上的View的高度，可以传默认值defaultBotom
     * @param popShowWidth  被显示在PopupWindow上的View的宽度，一般是传attachOnView的getWidth()
     * @return PopupWindow
     */
    public static PopupWindow show(Activity activity, View attachOnView, View popView, final int popShowHeight, final int popShowWidth,float downx,float downy) {

        if (popView != null && popView.getParent() != null) {
            ((ViewGroup) popView.getParent()).removeAllViews();
        }
        SupportPopupWindow popupWindow = null;
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        int location[] = new int[2];
        int x, y;
        int popHeight = 0, popWidth = 0;

        attachOnView.getLocationOnScreen(location);
        x = location[0];
        y = location[1];

        int h = attachOnView.getHeight();
        int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();

        if (popShowHeight == 0) {
            popHeight = 0;
            popHeight = Math.abs(screenHeight - (h + y)) - popHeight;
        } else if (popHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            popHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            popHeight = popShowHeight;
        }

        if (popShowWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
            popWidth = attachOnView.getWidth();
        } else {
            popWidth = popShowWidth;
        }

        popupWindow = new SupportPopupWindow(popView, popWidth, popHeight, true);

        //这行代码时用来让PopupWindow点击区域之外消失的，这个应该是PopupWindow的一个bug。
        //但是利用这个bug可以做到点击区域外消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());


        //popuwindow的有阴影，高度必须减去阴影高度

        if (screenHeight/2>=downy){//点击屏幕上方
            if (screenWidth/2>=downx){//点击屏幕左边
                //显示在右下方
                popupWindow.setAnimationStyle(R.style.swip_popupRightBottomAnimationDown);
                popupWindow.showAtLocation(attachOnView, Gravity.NO_GRAVITY, (int) (downx-x), (int) (downy-ConvertUtils.dp2px(3)));
            }else {
                //显示在左下方
                popupWindow.setAnimationStyle(R.style.swip_popupLeftBottomAnimationDown);
                popupWindow.showAtLocation(attachOnView, Gravity.NO_GRAVITY, (int) (downx-popShowWidth), (int) (downy-ConvertUtils.dp2px(3)));
            }
        }else {//点击屏幕下方
            if (screenWidth/2>=downx){
                //显示在右上方
                popupWindow.setAnimationStyle(R.style.swip_popupRightTopAnimationDown);
                popupWindow.showAtLocation(attachOnView, Gravity.NO_GRAVITY, (int) (downx-x), (int) (downy-popShowHeight-ConvertUtils.dp2px(3)));
            }else {
                //显示在左上方
                popupWindow.setAnimationStyle(R.style.swip_popupLeftTopAnimationDown);
                popupWindow.showAtLocation(attachOnView, Gravity.NO_GRAVITY, (int) (downx-popShowWidth), (int) (downy - popShowHeight-ConvertUtils.dp2px(3)));
            }
        }

        popupWindow.update();
        return popupWindow;
    }
    /**
     * 显示在正中
     * @param activity      Activity
     * @param attachOnView  显示在这个View的下方
     * @param popView       被显示在PopupWindow上的View
     * @param popShowHeight 被显示在PopupWindow上的View的高度，可以传默认值defaultBotom
     * @param popShowWidth  被显示在PopupWindow上的View的宽度，一般是传attachOnView的getWidth()
     * @return PopupWindow
     */
    public static PopupWindow showCenter(Activity activity, View attachOnView, View popView, final int popShowHeight, final int popShowWidth) {
        if (popView != null && popView.getParent() != null) {
            ((ViewGroup) popView.getParent()).removeAllViews();
        }
        SupportPopupWindow popupWindow = null;
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);

        int location[] = new int[2];
        int x, y;
        int popHeight = 0, popWidth = 0;

        attachOnView.getLocationOnScreen(location);
        x = location[0];
        y = location[1];

        int h = attachOnView.getHeight();
        int screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();

        if (popShowHeight == 0) {
            popHeight = 0;
            popHeight = Math.abs(screenHeight - (h + y)) - popHeight;
        } else if (popHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            popHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            popHeight = popShowHeight;
        }

        if (popShowWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
            popWidth = attachOnView.getWidth();
        } else {
            popWidth = popShowWidth;
        }

        popupWindow = new SupportPopupWindow(popView, popWidth, popHeight, true);

        //这行代码时用来让PopupWindow点击区域之外消失的，这个应该是PopupWindow的一个bug。
        //但是利用这个bug可以做到点击区域外消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());


        //popuwindow的有阴影，高度必须减去阴影高度
        popupWindow.showAtLocation(attachOnView, Gravity.NO_GRAVITY, x, h + y);

        popupWindow.update();
        return popupWindow;
    }

    /**
     * 根据点击位置显示不同位置popupWindow
     * @param activity
     * @param attachOnView
     */
    public static PopupWindow showListPopuWindom(final String[] strings, final Activity activity, View attachOnView,float downx,float downY, final OnItemLongClickListener mOnClik){
        final View sharepopu = View.inflate(activity, R.layout.swip_layout_list_popupwindow, null);
        RecyclerView mRecyclerView =  sharepopu.findViewById(R.id.recyclerview);
        final PopupWindowListAdapter mAdapter = new PopupWindowListAdapter(Arrays.asList(strings));

        //高100dp 高80dp
        final PopupWindow mPopupWindow = show(activity, attachOnView, sharepopu, ConvertUtils.dp2px( 50* strings.length +3), ConvertUtils.dp2px(100)
                , downx ,downY);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(activity));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mPopupWindow.dismiss();
                mOnClik.onItemClick(view,position);
            }
        });
      /*  mAdapter.setOnItemClickListener((convertView, position) -> {
            mPopupWindow.dismiss();
            mOnClik.onItemClick(convertView,position);

        });*/
        return mPopupWindow;
    }
}
