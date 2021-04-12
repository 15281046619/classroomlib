package com.xinwang.bgqbaselib.sku.widget;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Date:2021/4/8
 * Time;15:28
 * author:baiguiqiang
 */
public class SkuMaxHeightScrollView extends NestedScrollView {



    public SkuMaxHeightScrollView(Context context) {

        super(context);

    }



    public SkuMaxHeightScrollView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }



    @Override

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);

        int height = 0;

        for (int i = 0; i < getChildCount(); i++) {

            View child = getChildAt(i);

            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            int h = child.getMeasuredHeight();

            if (h > height)

                height = h;

        }

  /*      float heightDp = ScreenUtils.px2Dp(getContext(), height);

        if (heightDp > 220) {

            int maxHeight = ScreenUtils.dp2PxInt(getContext(), 220);

            setMeasuredDimension(width, maxHeight);

        } else if (heightDp < 75) {

            int minHeight = ScreenUtils.dp2PxInt(getContext(), 75);

            setMeasuredDimension(width, minHeight);

        } else {
*/
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

       /* }*/

    }

}
