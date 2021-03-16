package com.xingwang.classroom.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.xingwang.classroom.R;
import com.xinwang.bgqbaselib.utils.StatusBarUtils;

/**
 * Date:2019/8/20
 * Time;16:02
 * author:baiguiqiang
 *必须toolbar父控件是LinearLayout才生效
 */
public class CustomToolbar extends Toolbar {
    private  int statHeight;
    private  TextView titleView;

    public CustomToolbar(Context context) {
        this(context,null);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.classroom_CustomToolbar);
        String mText = typeArray.getString(R.styleable.classroom_CustomToolbar_android_text);
        int  mTextColor = typeArray.getColor(R.styleable.classroom_CustomToolbar_android_textColor, Color.WHITE);
        float mTextSize = typeArray.getDimensionPixelSize(R.styleable.classroom_CustomToolbar_android_textSize,16);
        int divColor = typeArray.getColor(R.styleable.classroom_CustomToolbar_divColor,Color.TRANSPARENT);
        boolean isCleanTop = typeArray.getBoolean(R.styleable.classroom_CustomToolbar_isCleanStatHeight,false);
        typeArray.recycle();
        titleView = new TextView(context);
        titleView.setText(mText);
        titleView.setTextColor(mTextColor);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTextSize*1f);
        titleView.setMaxLines(1);
        titleView.setBackgroundResource(android.R.color.transparent);
        titleView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
        titleView.setEllipsize( TextUtils.TruncateAt.END);

        titleView.setGravity(Gravity.CENTER);
        Toolbar.LayoutParams titleLayoutParams = new Toolbar.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT );
        titleLayoutParams.gravity = Gravity.CENTER;
        addView(titleView,titleLayoutParams);
        statHeight = StatusBarUtils.getStatusHeight(context);
        if (isCleanTop){
            statHeight=0;
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setPadding(0,statHeight,0,0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setText(String title){
        titleView.setText(title);
    }


    public void setText(@StringRes int title){
        titleView.setText( title);
    }

    public void setTextColor( ColorStateList color){
        titleView.setTextColor(color);
    }
    public TextView getText(){
        return titleView;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec+statHeight);
        else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
