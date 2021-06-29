package com.xinwang.shoppingcenter.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Date:2021/6/29
 * Time;9:53
 * author:baiguiqiang
 */
public class CustomNestedScrollView extends NestedScrollView {
    private OnCustomScrollChanged mOnXiaYiYeScrollChanged;

    public CustomNestedScrollView(@NonNull Context context) {
        this(context, null);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnXiaYiYeScrollChanged != null) {
            mOnXiaYiYeScrollChanged.onScroll(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChanged(OnCustomScrollChanged onXiaYiYeScrollChanged) {
        this.mOnXiaYiYeScrollChanged = onXiaYiYeScrollChanged;
    }

    public interface OnCustomScrollChanged {
        /**
         * 滑动的方法
         *
         * @param left    左边
         * @param top     上边
         * @param oldLeft 之前的左边
         * @param oldTop  之前的上边
         */
        void onScroll(int left, int top, int oldLeft, int oldTop);
    }

}
