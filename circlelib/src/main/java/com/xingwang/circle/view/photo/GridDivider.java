package com.xingwang.circle.view.photo;

import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;

public class GridDivider extends RecyclerView.ItemDecoration {
    private int mSpace;

    private GridDivider(int space) {
        mSpace = space;
    }

    /**
     * 设置间距资源 id
     *
     * @param resId
     * @return
     */
//    public static GridDivider newInstanceWithSpaceRes(@DimenRes int resId) {
//        return new GridDivider(getApp().getResources().getDimensionPixelOffset(resId));
//    }

    /**
     * 设置间距
     *
     * @param spaceDp 单位为 dp
     * @return
     */
    public static GridDivider newInstanceWithSpaceDp(int spaceDp) {
        return new GridDivider(ConvertUtils.dp2px(spaceDp));
    }

    /**
     * 设置间距
     *
     * @param spacePx 单位为 px
     * @return
     */
    public static GridDivider newInstanceWithSpacePx(int spacePx) {
        return new GridDivider(spacePx);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.top = mSpace;
        outRect.bottom = mSpace;
    }
}
