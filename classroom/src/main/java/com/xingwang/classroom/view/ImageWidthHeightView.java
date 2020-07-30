package com.xingwang.classroom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.xingwang.classroom.R;

/**
 * Date:2020/7/29
 * Time;17:54
 * author:baiguiqiang
 */
public class ImageWidthHeightView extends android.support.v7.widget.AppCompatImageView {


    private  float widthToHeightSum;
    private  boolean byWidthToHeight;

    public ImageWidthHeightView(Context context) {
        super(context,null);
    }

    public ImageWidthHeightView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomImage);
        byWidthToHeight = typeArray.getBoolean(R.styleable.CustomImage_byWidthToHeight, true);
        widthToHeightSum = typeArray.getFloat(R.styleable.CustomImage_widthToHeightSum, 1.0f);
        typeArray.recycle();
    }

    public ImageWidthHeightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.byWidthToHeight) {
            int finalWidth = MeasureSpec.getSize(widthMeasureSpec);
            float finalHeight = finalWidth / this.widthToHeightSum;
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec(finalWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec((int) finalHeight, MeasureSpec.EXACTLY));
        }else {
            int finalHeight = MeasureSpec.getSize(heightMeasureSpec);
            float finalWidth = finalHeight * this.widthToHeightSum;
            super.onMeasure(
                    MeasureSpec.makeMeasureSpec((int) finalWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(finalHeight, MeasureSpec.EXACTLY));
        }

    }
}