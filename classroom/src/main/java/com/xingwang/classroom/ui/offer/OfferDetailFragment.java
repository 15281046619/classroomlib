package com.xingwang.classroom.ui.offer;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.BaoJiaListAdapter;
import com.xingwang.classroom.bean.BaoJiaBean;
import com.xingwang.classroom.bean.OfferBean;
import com.xingwang.classroom.dialog.BottomChooseDialog;
import com.xingwang.classroom.dialog.BottomChooseMonAndYearDialog;
import com.xingwang.classroom.ui.statistic.StatisticPriceActivity;
import com.xingwang.classroom.ui.statistic.StatisticPriceFragment;
import com.xingwang.classroom.view.DetailsMarkerView;
import com.xingwang.classroom.view.MyLineChart;
import com.xingwang.classroom.view.PositionMarker;
import com.xingwang.classroom.view.RoundMarker;
import com.xingwang.classroom.view.WrapContentLinearLayoutManager;
import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.base.BaseLazyLoadFragment;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.TimeUtil;
import com.xinwang.bgqbaselib.view.DividerItemDecoration;
import com.xinwang.bgqbaselib.view.VpSwipeRefreshLayout;
import com.xinwang.bgqbaselib.view.loadmore.EndlessRecyclerOnScrollListener;
import com.xinwang.shoppingcenter.adapter.ShoppingGoodOrderListAdapter;
import com.xinwang.shoppingcenter.interfaces.OrderButtonListener;
import com.xinwang.shoppingcenter.ui.OrderDetailActivity;
import com.xinwang.shoppingcenter.ui.ReviewDetailActivity;
import com.xinwang.shoppingcenter.ui.ShoppingReviewActivity;
import com.xinwang.shoppingcenter.ui.WaybillDetailActivity;

import org.json.JSONException;
import org.json.JSONObject;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Date:2021/9/13
 * Time;16:34
 * author:baiguiqiang
 */
public class OfferDetailFragment extends BaseLazyLoadFragment {
    private MyLineChart mLineChar;
    private AppBarLayout appBarLayout;
    private TextView tvTime,tvUnit,tvPrice,tvPercentage,tvBJDDes;
    public String[] mUnits=new String[]{"元/吨","元/吨","元/公斤","元/斤","元/斤","元/斤","元/斤","元/斤"};//单位
    private String[] mCity=new String[]{"安徵","北京","重庆","福建","广东","甘肃","广西","贵州","海南","河北","河南","香港","黑龙","湖南","湖北","吉林","江苏","江西","辽宁","澳门",
            "内蒙","宁夏","青海","陕西","四川","山东","上海","山西","天津","台湾","新辐","西藏","云南","浙江"};
    private Button tvCity,tvMonth,tvYear;
    private int curPosition=0;
    private List<String> mTimeData=new ArrayList<>();//存时间list
    private String time;
    public String provice;
    private int pageNum =10;
    private int curPage =1;
    private OfferDetailActivity mActivity;
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerview;
    private Object mTopData;//上年或者上月数据
    private BaoJiaListAdapter mAdapter;
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_offer_detail_classroom,container,false);
        mLineChar =view.findViewById(R.id.mLineChar);
        tvCity =view.findViewById(R.id.tvCity);
        tvMonth =view.findViewById(R.id.tvMonth);
        tvYear =view.findViewById(R.id.tvYear);
        appBarLayout =view.findViewById(R.id.app_bar_layout);
        tvPercentage =view.findViewById(R.id.tvPercentage);
        tvTime =view.findViewById(R.id.tvTime);
        tvPrice =view.findViewById(R.id.tvPrice);
        tvUnit =view.findViewById(R.id.tvUnit);
        tvBJDDes =view.findViewById(R.id.tvBJDDes);
        recyclerview =view.findViewById(R.id.recyclerView);
        swipeRefreshLayout =view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setOnRefreshListener(() -> goPercentageProData(null,0));
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                swipeRefreshLayout.setEnabled(i>=0);
            }
        });
        if (getContext()!=null) {
            recyclerview.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
            recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        }
        recyclerview.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (mData.size()>0&&!isRequesting)
                    goRequestUserBaojia(Constants.LOAD_DATA_TYPE_MORE);

            }
        });
        return view;
    }

    @Override
    public void initData() {
        curPosition = getArguments().getInt("position");
        mActivity = (OfferDetailActivity) getActivity();
        time = TimeUtil.getCurTimeYM();
        provice = getArguments().getString("provice");
        tvCity.setText(provice);
        setTimeShow();
        goPercentageProData(null,0);
    }

    private void initShow() {
        tvTime.setText(TimeUtil.getMD(System.currentTimeMillis() / 1000 + "") + "更新");
        tvUnit.setText("单位：" + mUnits[curPosition]);
        tvPrice.setText(getPriceSpannable());
        tvPercentage.setText(getPercentageSpannable());
        tvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> mYear =new ArrayList<>();
                ArrayList<String> mMonth =new ArrayList<>();
                String mCurYear =time.contains("-")?time.substring(0,time.indexOf("-")):time;
                String mCurMoth =time.contains("-")?time.substring(time.indexOf("-")+1):"1";
                int curYearPos =0;
                int curMonthPos =0;
                for (int i=0;i<100;i++){
                    mYear.add(2000+i+"");
                    if (mCurYear.equals(i+2000+"")){
                        curYearPos =i;
                    }
                }
                for (int i=0;i<12;i++){
                    mMonth.add(i+1+"");
                    if (Integer.parseInt(mCurMoth)==(i+1)){
                        curMonthPos =i;
                    }
                }
                BottomChooseMonAndYearDialog mDialog =  BottomChooseMonAndYearDialog.getInstance(mYear,mMonth,curYearPos,curMonthPos);
                mDialog.setCallback(new BottomChooseMonAndYearDialog.Callback1<String>() {
                    @Override
                    public void run(String s) {
                        swipeRefreshLayout.setRefreshing(true);
                        goPercentageProData(s,1);
                    }
                });
                mDialog.showDialog(getFragmentManager());
            }
        });
        tvYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> mYear =new ArrayList<>();

                String mCurYear =time.contains("-")?time.substring(0,time.indexOf("-")):time;
                int curYearPos =0;
                for (int i=0;i<100;i++){
                    mYear.add(2000+i+"");
                    if (mCurYear.equals(i+2000+"")){
                        curYearPos =i;
                    }
                }
                BottomChooseDialog mDialog =  BottomChooseDialog.getInstance(mYear,curYearPos);
                mDialog.setCallback(new BottomChooseDialog.Callback1<Integer>() {
                    @Override
                    public void run(Integer s) {
                        swipeRefreshLayout.setRefreshing(true);
                        goPercentageProData(mYear.get(s),1);

                    }
                });
                mDialog.showDialog(getFragmentManager());
            }
        });
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<String> mList = new ArrayList(Arrays.asList(mCity));
                BottomChooseDialog mDialog =  BottomChooseDialog.getInstance( mList,mList.indexOf(provice));
                mDialog.setCallback(new BottomChooseDialog.Callback1<Integer>() {
                    @Override
                    public void run(Integer s) {
                        swipeRefreshLayout.setRefreshing(true);
                        goPercentageProData(mList.get(s),2);
                    }
                });
                mDialog.showDialog(getFragmentManager());
            }
        });
    }

    private void setTimeShow(){
        if (time.contains("-")){
            tvMonth.setSelected(true);
            tvMonth.setText(time);
            tvYear.setText("按年查找");
            tvYear.setSelected(false);
        }else {
            tvYear.setSelected(true);
            tvYear.setText(time);
            tvMonth.setText("按月查找");
            tvMonth.setSelected(false);
        }
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
        SpannableString spannableString =new SpannableString( mPrice+mUnits[curPosition]);
        spannableString.setSpan(new RelativeSizeSpan(0.4f),mPrice.length(),spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    /**
     * 填充数据
     */
    private void lineChartToData(JSONObject jsonObject) {

        //设置数据
        setData(jsonObject);
        //默认动画

        List<ILineDataSet> setsCubic = mLineChar.getData().getDataSets();
        mLineChar.animateX(getAnimateTime(setsCubic.size()));
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
    private int getAnimateTime(int sum){// 250个数量 1000计算
        if (sum*4<200){
            return 200;
        }else if (sum*4<1000){
            return sum*4;
        }else {
            return 1000;
        }
    }
    private LineDataSet set1;
    private String mPrice;//平均价格
    private float mPercentage=0;//同期比列
    private void setData(JSONObject jsonObject) {
        float totalPrice=0;
        int totalTime=0;
        ArrayList<Entry> values =new ArrayList();
        Iterator kes = jsonObject.keys();
        while (kes.hasNext()){
            try {
                String key = (String) kes.next();
                float value = Float.parseFloat(jsonObject.getString(key));
                totalPrice=sum(totalPrice,value);
                values.add(new Entry(totalTime+1,value));
                mTimeData.add(key);
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
                        return TimeUtil.byTimeAddDay(mTimeData.get((int) value-1), 0);
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
            mLineChar.getAxisRight().setEnabled(false); //设置图表右边的y轴禁用
            //隐藏左边坐标轴横网格线
            mLineChar.getAxisLeft().setDrawGridLines(false);
            //隐藏右边坐标轴横网格线
            mLineChar.getAxisRight().setDrawGridLines(false);
            //隐藏X轴竖网格线
            mLineChar.getXAxis().setDrawGridLines(false);

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

        try {
            float totalPrice=0;
            int totalTime=0;
            JSONObject jsonObject =new JSONObject(GsonUtils.createGsonString(mTopData));
            Iterator kes = jsonObject.keys();
            while (kes.hasNext()){
                String key = (String) kes.next();
                float value = Float.parseFloat(jsonObject.getString(key));
                totalPrice=sum(totalPrice,value);
                totalTime+=1;
            }

            int mTotalPrice= CountUtil.multiplyByInt(100,totalPrice);
            int mTotalTime =totalTime*100;
            DecimalFormat df=new DecimalFormat("0.00");//保留为啥
            String mOldPrice =df.format((float) mTotalPrice/mTotalTime);

            Float old = Float.parseFloat(mOldPrice);
            Float cur = Float.parseFloat(mPrice);
            Double p3 =  Double.parseDouble((cur-old) / old+"");

            mPercentage =Float.parseFloat(df.format(p3*100));
        }catch (JSONException e){
            mPercentage=0;
        }
    }


    public float sum(float d1,float d2){
        BigDecimal bd1 = new BigDecimal(Float.toString(d1));
        BigDecimal bd2 = new BigDecimal(Float.toString(d2));
        return bd1.add(bd2).floatValue();
    }
    private void setLineChart() {
        //设置手势滑动事件
        // mLineChar.setOnChartGestureListener(this);
        //设置数值选择监听
        mLineChar.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //查看覆盖物是否被回收
                if (mLineChar.isMarkerAllNull()) {
                    //重新绑定覆盖物
                    createMakerView();
                    //并且手动高亮覆盖物
                    mLineChar.highlightValue(h);
                }
            }

            @Override
            public void onNothingSelected() {

            }
        });
        //后台绘制
        mLineChar.setDrawGridBackground(false);
        //设置描述文本
        mLineChar.getDescription().setEnabled(false);
        //设置支持触控手势
        mLineChar.setTouchEnabled(true);
        //设置缩放
        mLineChar.setDragEnabled(false);
        //设置推动
        mLineChar.setScaleEnabled(false);
        //如果禁用,扩展可以在x轴和y轴分别完成
        mLineChar.setPinchZoom(true);
        mLineChar.setHighlightPerTapEnabled(true);
        mLineChar.setHighlightPerDragEnabled(true);
        mLineChar.setDragEnabled(true);//拖动高亮

       /* //透明化图例
        Legend legend = mLineChar.getLegend();
        legend.setForm(Legend.LegendForm.NONE);
        legend.setTextColor(Color.WHITE);*/
      /*  DetailsMarkerView detailsMarkerView = new DetailsMarkerView(getContext(),R.layout.layout_marker_view_detail,mUnit,startData);
        //一定要设置这个玩意，不然到点击到最边缘的时候不会自动调整布局
        detailsMarkerView.setChartView(mLineChar);
        mLineChar.setMarker(detailsMarkerView);*/
        createMakerView();
    }
    /**
     * 创建覆盖物
     */
    public void createMakerView() {
        if (mTimeData!=null) {
            DetailsMarkerView detailsMarkerView = new DetailsMarkerView(getContext(), R.layout.layout_marker_view_detail, mUnits[curPosition], mTimeData);
            detailsMarkerView.setChartView(mLineChar);
            mLineChar.setDetailsMarkerView(detailsMarkerView);
            mLineChar.setPositionMarker(new PositionMarker(getContext()));
            mLineChar.setRoundMarker(new RoundMarker(getContext()));
        }
    }
    public static OfferDetailFragment getInstance(int position,String provice){
        OfferDetailFragment mFragment = new OfferDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putString("provice",provice);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    /**
     *
     * @param content 可以是时间 省份 null不更新
     * @param type  0 null  1 时间 2 省份
     */
    private void goRequestData(String content,int type){
        String curTime = type==1?content:time;
        requestGet(curTime.contains("-") ? HttpUrls.URL_BAOJIA_MONTH():HttpUrls.URL_BAOJIA_YEAR(),new ApiParams().with("provice",type==2?content:provice)
                .with("month",curTime).with("year",curTime).with("type",mActivity.types[curPosition]), OfferBean.class, new HttpCallBack<OfferBean>() {

            @Override
            public void onFailure(String message) {
                swipeRefreshLayout.setRefreshing(false);
                MyToast.myToast(mActivity.getApplicationContext(),message);
            }

            @Override
            public void onSuccess(OfferBean historyPriceBean) {
                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject mJsonObject = new JSONObject(GsonUtils.createGsonString(historyPriceBean.getData()));
                    if (type==1) {
                        time = content;
                        setTimeShow();
                    }else if (type==2){
                        provice =content;
                        tvCity.setText(provice);
                        curPage=1;
                        goRequestUserBaojia(Constants.LOAD_DATA_TYPE_REFRESH);
                    }else {
                        curPage=1;
                        goRequestUserBaojia(Constants.LOAD_DATA_TYPE_REFRESH);
                    }
                    mLineChar.clear();
                    setLineChart();
                    lineChartToData(mJsonObject);
                    initShow();
                }catch (JSONException e){
                    e.printStackTrace();
                    if (type==1) {
                        time = content;
                        setTimeShow();
                    }else if (type==2){
                        provice =content;
                        tvCity.setText(provice);
                        //情况用户报价
                        mData.clear();
                        initAdapter(2);
                    }
                    mLineChar.clear();
                    mPrice ="--";
                    tvPrice.setText(getPriceSpannable());
                    mPercentage=0f;
                    tvPercentage.setText(getPercentageSpannable());
                    MyToast.myToast(mActivity,"暂未查找到历史数据");
                }
            }
        });
    }
    private List<BaoJiaBean.DataBean> mData =new ArrayList<>();
    boolean isRequesting =false;
    private void goRequestUserBaojia(int loadDataTypeInit){
        isRequesting =true;
        requestGet(HttpUrls.URL_BAOJIA_BAOJIA(),new ApiParams().with("provice",provice)
                        .with("type",mActivity.types[curPosition])
                        .with("page",curPage).with("page_num",pageNum), BaoJiaBean.class, new HttpCallBack<BaoJiaBean>() {

            @Override
            public void onFailure(String message) {
                isRequesting =false;
                MyToast.myToast(mActivity.getApplicationContext(),message);
            }

            @Override
            public void onSuccess(BaoJiaBean baoJiaBean) {
                if (loadDataTypeInit!= Constants.LOAD_DATA_TYPE_MORE){
                    mData.clear();
                }
                mData.addAll(baoJiaBean.getData());
                curPage++;
                if(baoJiaBean.getData().size()<pageNum){
                    initAdapter(loadDataTypeInit!= Constants.LOAD_DATA_TYPE_MORE&&baoJiaBean.getData().size()==0?2:3);
                }else {
                    initAdapter(1);
                }
                isRequesting =false;
            }
        });
    }
    /**
     *
     * @param content 可以是时间 省份 null不更新
     * @param type  0 null  1 时间 2 省份
     */
    public void goPercentageProData(String content,int type){
        String curTime = type==1?content:time;
        String percentageTime = getTopTime(curTime);
        requestGet(percentageTime.contains("-") ? HttpUrls.URL_BAOJIA_MONTH():HttpUrls.URL_BAOJIA_YEAR(),new ApiParams().with("provice",type==2?content:provice)
                .with("month",percentageTime).with("year",percentageTime).with("type",mActivity.types[curPosition]), OfferBean.class, new HttpCallBack<OfferBean>() {

            @Override
            public void onFailure(String message) {
                swipeRefreshLayout.setRefreshing(false);
                MyToast.myToast(mActivity.getApplicationContext(),message);
            }

            @Override
            public void onSuccess(OfferBean historyPriceBean) {
                mTopData =historyPriceBean.getData();
                goRequestData(content,type);

            }
        });
    }
    private void initAdapter(int state){
        if (state==2){
            tvBJDDes.setVisibility(View.GONE);
        }else {
            tvBJDDes.setVisibility(View.VISIBLE);
        }
        if (mAdapter==null) {
            mAdapter = new BaoJiaListAdapter(mData,mUnits[curPosition]);
            mAdapter.setLoadStateNoNotify(state);
            recyclerview.setAdapter(mAdapter);

        }else {
            mAdapter.setLoadStateNoNotify(state);
            mAdapter.notifyDataSetChanged();
        }
    }
    private String getTopTime(String curTime) {
        if (curTime.contains("-")){//月份
            String mCurYear =curTime.substring(0,curTime.indexOf("-"));
            String mCurMoth =curTime.substring(curTime.indexOf("-")+1);
            int topMonth =Integer.parseInt(mCurMoth)-1;
            if (topMonth>0){
                return mCurYear+"-"+(topMonth<10?("0"+topMonth):topMonth);
            }else {
                return Integer.parseInt(mCurYear)-1+"-12";
            }
        }else {//年份
            return Integer.parseInt(curTime)-1+"";
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
