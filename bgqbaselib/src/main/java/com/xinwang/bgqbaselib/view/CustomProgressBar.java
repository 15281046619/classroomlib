package com.xinwang.bgqbaselib.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.AttributeSet;

import com.xinwang.bgqbaselib.R;


/**
 * Date:2019/9/11
 * Time;14:54
 * author:baiguiqiang
 */
public class CustomProgressBar extends ContentLoadingProgressBar {
    public CustomProgressBar(@NonNull Context context) {
        super(context);
    }

    public CustomProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.themeClassRoom), PorterDuff.Mode.MULTIPLY);
    }
}
