package com.xingwang.classroom.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.lang.ref.WeakReference;

/**
 * Date:2021/1/15
 * Time;14:04
 * author:baiguiqiang
 */
public class MyLineChart extends LineChart {



    //弱引用覆盖物对象,防止内存泄漏

    private WeakReference<DetailsMarkerView> mDetailsReference;
    private WeakReference<RoundMarker> mRoundMarkerReference;
    private WeakReference<PositionMarker> mPositionMarkerReference;



    /**

     * 所有覆盖物是否为空

     *

     * @return TRUE FALSE

     */

    public boolean isMarkerAllNull() {

        return mDetailsReference.get() == null && mRoundMarkerReference.get() == null && mPositionMarkerReference.get() == null;

    }



    public void setDetailsMarkerView(DetailsMarkerView detailsMarkerView) {
        mDetailsReference = new WeakReference<>(detailsMarkerView);
    }


    public void setRoundMarker(RoundMarker roundMarker) {
        mRoundMarkerReference = new WeakReference<>(roundMarker);

    }

    public void setPositionMarker(PositionMarker positionMarker) {
        mPositionMarkerReference = new WeakReference<>(positionMarker);
    }

    public MyLineChart(Context context) {
        super(context);
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public MyLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    /**

     * draws all MarkerViews on the highlighted positions

     */

    protected void drawMarkers(Canvas canvas) {
        // if there is no marker view or drawing marker is disabled
        DetailsMarkerView mDetailsMarkerView = mDetailsReference.get();
        RoundMarker mRoundMarker = mRoundMarkerReference.get();
        PositionMarker mPositionMarker = mPositionMarkerReference.get();
        if (mDetailsMarkerView == null || mRoundMarker == null || mPositionMarker == null || !isDrawMarkersEnabled() || !valuesToHighlight())
            return;
        for (int i = 0; i < mIndicesToHighlight.length; i++) {
            Highlight highlight = mIndicesToHighlight[i];
            IDataSet set = mData.getDataSetByIndex(highlight.getDataSetIndex());
            Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
            int entryIndex = set.getEntryIndex(e);
            // make sure entry not null
            if (e == null || entryIndex > set.getEntryCount() * mAnimator.getPhaseX())
                continue;
            float[] pos = getMarkerPosition(highlight);
            LineDataSet dataSetByIndex = (LineDataSet) getLineData().getDataSetByIndex(highlight.getDataSetIndex());
            // check bounds

            if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                continue;


            float circleRadius = dataSetByIndex.getCircleRadius();
            // callbacks to update the content

            mPositionMarker.refreshContent(e, highlight);
            mPositionMarker.draw(canvas, pos[0] - mPositionMarker.getWidth() / 2, pos[1] - mPositionMarker.getHeight());
            mRoundMarker.refreshContent(e, highlight);
            mRoundMarker.draw(canvas, pos[0] - mRoundMarker.getWidth() / 2, pos[1] + circleRadius - mRoundMarker.getHeight());
            mDetailsMarkerView.refreshContent(e, highlight);
            mDetailsMarkerView.draw(canvas, pos[0], pos[1] - mPositionMarker.getHeight());

        }

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent evt) {

        switch (evt.getAction()) {

            case MotionEvent.ACTION_DOWN:

               // downPoint.x = evt.getX();

              //  downPoint.y = evt.getY();

                break;

            case MotionEvent.ACTION_MOVE:


              /*  if (getScaleX() > 1 && Math.abs(evt.getX() - downPoint.x) > 5) {

                    getParent().requestDisallowInterceptTouchEvent(true);

                }else if (getScrollY()>1&&Math.abs(evt.getY()-downPoint.y)>5){*/
                    getParent().requestDisallowInterceptTouchEvent(true);
               /* }*/

                break;

        }

        return super.onTouchEvent(evt);

    }



}
