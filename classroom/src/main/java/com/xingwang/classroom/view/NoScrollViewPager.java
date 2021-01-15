package com.xingwang.classroom.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Date:2021/1/15
 * Time;9:53
 * author:baiguiqiang
 */
public class NoScrollViewPager extends ViewPager {
    // 是否禁止 viewpager 左右滑动
    private boolean noScroll = true;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll){
            return false;
        }else{
            return super.onTouchEvent(arg0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll){
            return false;
        }else{
            return super.onInterceptTouchEvent(arg0);
        }
    }
}