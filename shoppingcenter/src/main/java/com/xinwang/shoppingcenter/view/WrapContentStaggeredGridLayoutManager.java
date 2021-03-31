package com.xinwang.shoppingcenter.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 *
 * 会出现 java.lang.IndexOutOfBoundsException: Inconsistency detected. Invalid view holder adapter positionViewHolder异常
 * 解决
 */
public class WrapContentStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    public WrapContentStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount,orientation);
    }



    public WrapContentStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }
}
