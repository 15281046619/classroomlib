package com.xingwang.classroom.ui;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.xingwang.classroom.R;
import com.xingwang.classroom.utils.LogUtil;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.utils.TimeUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/1/7
 * Time;13:56
 * author:baiguiqiang
 */
public class StatisticPriceFragment extends BaseLazyLoadFragment  {
    private LineChart mLineChar;
    private TextView tvTime,tvUnit,tvPrice,tvPercentage;
    private String mUnit="元/千克";//单位
    private RadioGroup radioGroup;
    private int curPosition=0;
    private List<String> mData=new ArrayList<>();
    private int time=366;//代表天数 有366 183 90 30
    private String startData;//开始计算时间

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_statistic_price,container,false);
        mLineChar =view.findViewById(R.id.mLineChar);
        radioGroup =view.findViewById(R.id.radioGroup);
        tvPercentage =view.findViewById(R.id.tvPercentage);
        tvTime =view.findViewById(R.id.tvTime);
        tvPrice =view.findViewById(R.id.tvPrice);
        tvUnit =view.findViewById(R.id.tvUnit);

        return view;
    }

    @Override
    public void initData() {
        curPosition = getArguments().getInt("position");

        initAllData();
        setLineChart();
        lineChartToData();
        initShow();
    }

    private void initShow() {
        tvTime.setText(TimeUtil.getMD(System.currentTimeMillis()/1000+"")+"更新");
        tvUnit.setText("单位："+mUnit);
        tvPrice.setText(getPriceSpannable());
        tvPercentage.setText(getPercentageSpannable());
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId==R.id.tvMonth){
                time =30;
            }else  if(checkedId==R.id.tvQuarter){
                time =90;
            }else   if(checkedId==R.id.tvYear2){
                time =183;
            }else {
                time =mData.size();
            }
            lineChartToData();
            initShow();
        });
    }

    private SpannableString getPercentageSpannable() {
        String mP ="--";
        if (mPercentage<=0){
            tvPercentage.setTextColor(ContextCompat.getColor(getContext(),R.color.themeClassRoom));
            if (mPercentage!=0)
                mP=mPercentage+"";//负数有符号
        }else {

            mP="+"+mPercentage;
            tvPercentage.setTextColor(ContextCompat.getColor(getContext(),R.color.red));
        }

        SpannableString spannableString =new SpannableString(mP+"%");
        spannableString.setSpan(new RelativeSizeSpan(0.4f),spannableString.length()-1,spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private SpannableString getPriceSpannable(){
        SpannableString spannableString =new SpannableString( mPrice+mUnit);
        spannableString.setSpan(new RelativeSizeSpan(0.4f),mPrice.length(),spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    /**
     * 填充数据
     */
    private void lineChartToData() {

        //设置数据
        setData();
        //默认动画
        mLineChar.animateX(1000);
        List<ILineDataSet> setsCubic = mLineChar.getData().getDataSets();
        for (ILineDataSet iSet : setsCubic) {
            LineDataSet set = (LineDataSet) iSet;
            set.setMode( LineDataSet.Mode.CUBIC_BEZIER);//曲线
            set.setDrawValues(false);//在图标上面不显示数值
            // set.setMode( LineDataSet.Mode.LINEAR); 直线
        }
        //刷新
        mLineChar.invalidate();
        // 得到这个文字
        Legend l = mLineChar.getLegend();
        // 修改文字 ...
        l.setForm(Legend.LegendForm.LINE);
    }
    private LineDataSet set1;
    private String mPrice;//平均价格
    private double mPercentage=0;//同期比列
    private void setData() {
        ArrayList<Entry> values =new ArrayList();
        double totalPrice=0;
        int totalTime=0;
        LogUtil.i("TAG",mData.toString());
        for ( int i=(mData.size()-time);i<mData.size();i++){
            try {
                totalPrice=sum(totalPrice,Double.parseDouble(mData.get(i)));
                values.add(new Entry(i+1,Float.parseFloat(mData.get(i))));
                totalTime +=1;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        int mTotalPrice = (int) (totalPrice*100);
        int mTotalTime =totalTime*100;
        DecimalFormat df=new DecimalFormat("0.00");//保留为啥
        mPrice =df.format((double) mTotalPrice/mTotalTime);

        setPercentage();

        if (mLineChar.getData() != null && mLineChar.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mLineChar.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mLineChar.getData().notifyDataChanged();
            mLineChar.notifyDataSetChanged();
        } else {

            // mLineChar.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//x 显示在表格下方
            mLineChar.getXAxis().setValueFormatter(new ValueFormatter(){//设置显示x轴为时间
                @Override
                public String getFormattedValue(float value) {
                    try {
                        return TimeUtil.byTimeAddDay(startData, (int) value);
                    }catch (Exception e){
                        return super.getFormattedValue(value);
                    }


                }
            });
            // 创建一个数据集,并给它一个类型
            set1 = new LineDataSet(values,"来源于"+getResources().getString(R.string.app_name));

            // 在这里设置线
           /* set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);*/
            set1.setColor(ContextCompat.getColor(getContext(),R.color.themeClassRoom));
            set1.setCircleColor(ContextCompat.getColor(getContext(),R.color.themeClassRoom));
            set1.setLineWidth(1f);
            set1.setCircleRadius(1f);
            set1.setDrawCircleHole(true);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // 填充背景只支持18以上
                //Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
                //set1.setFillDrawable(drawable);
                set1.setFillColor(ContextCompat.getColor(getContext(),R.color.theme1ClassRoom));
            } else {
                set1.setFillColor(ContextCompat.getColor(getContext(),R.color.themeClassRoom));
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList();
            //添加数据集
            dataSets.add(set1);

            //创建一个数据集的数据对象
            LineData data = new LineData(dataSets);

            //谁知数据
            mLineChar.setData(data);
        }
    }

    private void setPercentage() {
        if (mData.size()-time>0) {
            double totalPrice=0;
            int totalTime=0;
            for (int i = (mData.size() - 2 * time); i < mData.size() - time; i++) {
                try {
                    totalPrice=sum(totalPrice,Double.parseDouble(mData.get(i)));
                    totalTime +=1;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            int mTotalPrice = (int) (totalPrice*100);
            int mTotalTime =totalTime*100;
            DecimalFormat df=new DecimalFormat("0.00");//保留为啥
            String mOldPrice =df.format((double) mTotalPrice/mTotalTime);
            Double old = Double.parseDouble(mOldPrice);
            Double cur = Double.parseDouble(mPrice);
            double p3 = (cur-old) / cur;
            mPercentage =Double.parseDouble(df.format(p3*100));
        }else {
            mPercentage=0;
        }
    }


    public double sum(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }
    private void setLineChart() {
        //设置手势滑动事件
       // mLineChar.setOnChartGestureListener(this);
        //设置数值选择监听
        // mLineChar.setOnChartValueSelectedListener(this);
        //后台绘制
        mLineChar.setDrawGridBackground(false);
        //设置描述文本
        mLineChar.getDescription().setEnabled(false);
        //设置支持触控手势
        mLineChar.setTouchEnabled(true);
        //设置缩放
        mLineChar.setDragEnabled(true);
        //设置推动
        mLineChar.setScaleEnabled(true);
        //如果禁用,扩展可以在x轴和y轴分别完成
        mLineChar.setPinchZoom(true);
        mLineChar.setHighlightPerTapEnabled(false);
        mLineChar.setHighlightPerDragEnabled(false);
    }

    public static StatisticPriceFragment getInstance(int position){
        StatisticPriceFragment mFragment = new StatisticPriceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        mFragment.setArguments(bundle);
        return mFragment;
    }
    private void initAllData(){
        StatisticPriceActivity mActivity = (StatisticPriceActivity) getActivity();
        startData=mActivity.mData.getStart_date();

        switch (curPosition){
            case 0:
                mUnit ="元/吨";
                mData=  mActivity.mData.getPrices().getBean();
                break;
            case 1:
                mUnit ="元/吨";
                mData= mActivity.mData.getPrices().getMaize();
                break;
            case 2:
                mUnit ="元/千克";
                mData= mActivity.mData.getPrices().getPig_in();
                break;
            case 3:
                mUnit ="元/千克";
                mData= mActivity.mData.getPrices().getPig_out();
                break;
            case 4:
                mUnit ="元/千克";
                mData= mActivity.mData.getPrices().getPig_local();
                break;
            case 5:
                mUnit ="元/千克";
                mData= mActivity.mData.getPrices().getPork();
                break;
        }
        time = mData.size();
    }


}
