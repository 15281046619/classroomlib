package com.xingwang.classroom.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;

/**
 * Date:2021/1/14
 * Time;13:21
 * author:baiguiqiang
 */
public class LineChartInViewPager extends LineChart {
    PointF downPoint = new PointF();



    public LineChartInViewPager(Context context) {

        super(context);

    }



    public LineChartInViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

    }



    public LineChartInViewPager(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);

    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent evt) {

        switch (evt.getAction()) {

            case MotionEvent.ACTION_DOWN:

                downPoint.x = evt.getX();

                downPoint.y = evt.getY();

                break;

            case MotionEvent.ACTION_MOVE:


                if (getScaleX() > 1 && Math.abs(evt.getX() - downPoint.x) > 5) {

                    getParent().requestDisallowInterceptTouchEvent(true);

                }else if (getScrollY()>1&&Math.abs(evt.getY()-downPoint.y)>5){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                break;

        }

        return super.onTouchEvent(evt);

    }
}
