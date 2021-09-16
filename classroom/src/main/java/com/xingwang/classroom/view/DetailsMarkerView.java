package com.xingwang.classroom.view;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.xingwang.classroom.R;
import com.xinwang.bgqbaselib.utils.TimeUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * Date:2021/1/15
 * Time;8:55
 * author:baiguiqiang
 */
public class DetailsMarkerView extends MarkerView {

    private TextView tvTime;
    private TextView tvprice;
    private String unit,startTime;
    private List<String> mTimeData;
    /**
     * 在构造方法里面传入自己的布局以及实例化控件
     * @param context 上下文

     */
    public DetailsMarkerView(Context context, int layoutResource,String unit,String startTime) {
        super(context, layoutResource);
        this.unit = unit;
        this.startTime =startTime;
        tvTime = findViewById(R.id.tvTime);
        tvprice = findViewById(R.id.tvprice);
    }
    public DetailsMarkerView(Context context, int layoutResource, String unit, List<String> mTimes) {
        super(context, layoutResource);
        this.unit = unit;
        this.mTimeData =mTimes;
        tvTime = findViewById(R.id.tvTime);
        tvprice = findViewById(R.id.tvprice);
    }

    //每次重绘，会调用此方法刷新数据
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
        try {
            //收入
            if (e.getY() == 0) {
                tvprice.setText("暂无数据");
            } else {
                tvprice.setText(concat(e.getY(), "价格："));
            }
            if (startTime!=null)
                tvTime.setText(TimeUtil.byTimeAddDay1(startTime,(int) e.getX()-1));
            else
                tvTime.setText(TimeUtil.byTimeAddDay1(mTimeData.get((int) e.getX()-1),0));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        super.refreshContent(e, highlight);
    }

    //布局的偏移量。就是布局显示在圆点的那个位置
    // -(width / 2) 布局水平居中
    //-(height) 布局显示在圆点上方
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

    public String concat(float money, String values) {
        return values + new BigDecimal(money).setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + unit;
    }

}
