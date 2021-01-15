package com.xingwang.classroom.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;

import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.HomeViewpagerAdapter;
import com.xingwang.classroom.bean.ADGroupBean;
import com.xingwang.classroom.bean.CategoryBean;
import com.xingwang.classroom.bean.HistoryPriceBean;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.view.CustomToolbar;
import com.xingwang.classroom.view.NoScrollViewPager;
import com.xingwang.classroom.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/1/7
 * Time;13:05
 * author:baiguiqiang
 */
public class StatisticPriceActivity extends BaseNetActivity{
    private CustomToolbar toolbar;
    private TabLayout tabLayout;
    private NoScrollViewPager viewpager;
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private int initPos =0;
    private List<Fragment> mFragments;
    public HistoryPriceBean.DataBean mData;
    @Override
    protected int layoutResId() {
        return R.layout.activity_statistic_price;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isStartLaunch()) {
            initViews();
            initIntent();
            initData();
            initListener();
        }
    }
    private void initIntent(){
        String type =getIntent().getStringExtra("type");
        Uri uri = getIntent().getData();
        if (uri != null) {
            type = uri.getQueryParameter("type");
        }
        mFragments =new ArrayList<>();
        tabLayout.removeAllTabs();
        if (!TextUtils.isEmpty(type)&&initPos==0){
            try {
                initPos =Integer.parseInt(type);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(() -> goRequestData());
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initData() {
        toolbar.setText(toNameByPosition(initPos));
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        goRequestData();
    }
    private void goRequestData(){
        requestGet(HttpUrls.URL_PRICE_HISTORY(),new ApiParams(), HistoryPriceBean.class, new HttpCallBack<HistoryPriceBean>() {

            @Override
            public void onFailure(String message) {
                swipeRefreshLayout.setRefreshing(false);
                MyToast.myToast(getApplicationContext(),message);
            }

            @Override
            public void onSuccess(HistoryPriceBean historyPriceBean) {
                swipeRefreshLayout.setRefreshing(false);
                mData =  historyPriceBean.getData();
                initTabLayout();
            }
        });

    }
    private void initViews() {
        toolbar =findViewById(R.id.toolbar);
        tabLayout =findViewById(R.id.tabLayout);
        viewpager =findViewById(R.id.viewpager);
        swipeRefreshLayout =findViewById(R.id.swipeRefreshLayout);

    }
    private void initTabLayout() {
        mFragments.clear();
        ArrayList<String> mTitles =new ArrayList<>();
        for (int i=0;i<6;i++) {
            mFragments.add(StatisticPriceFragment.getInstance(i));
            String title=toNameByPosition(i);
            mTitles.add(title);
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }

        HomeViewpagerAdapter mViewPagerAdapter = new HomeViewpagerAdapter(getSupportFragmentManager(), mFragments,mTitles);
        viewpager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(initPos);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                toolbar.setText(mTitles.get(i));
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private String toNameByPosition(int i){
        switch (i){
            case 0:
                return "大豆";

            case 1:
                return  "玉米";
            case 2:
                return "内三元";
            case 3:
                return "外三元";
            case 4:
                return "土杂猪";
            case 5:
                return "猪肉";
        }
        return "";
    }
}
