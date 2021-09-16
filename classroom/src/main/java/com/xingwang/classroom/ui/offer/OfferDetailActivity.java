package com.xingwang.classroom.ui.offer;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.HomeViewpagerAdapter;
import com.xingwang.classroom.dialog.CenterEditBaoJiaDialog;
import com.xingwang.classroom.view.NoScrollViewPager;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.CommonEntity;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.ui.ShoppingOrderActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 禽药线 报价详情
 * Date:2021/9/13
 * Time;14:18
 * author:baiguiqiang
 */
public class OfferDetailActivity extends BaseNetActivity {
    private CustomToolbar toolbar;
    private TabLayout tabLayout;
    private NoScrollViewPager viewpager;
    private int initPos =0;
    private List<Fragment> mFragments;
    public String[] types =new String[]{"玉米","豆粕","鸡蛋","鸭蛋","国鸡","肉鸡","肉鸭","肉鹅"};

    @Override
    protected int layoutResId() {
        return R.layout.activity_offer_detail_classroom;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initIntent();
        initData();
        initListener();

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
                initPos =toPositionByName(type);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void initListener() {

        toolbar.setNavigationOnClickListener(v -> finish());
        findViewById(R.id.tvBaoJia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBaoJiaDialog();
            }
        });
    }

    private void showBaoJiaDialog() {
        String provice = ((OfferDetailFragment)mFragments.get(viewpager.getCurrentItem())).provice;
        String unit = ((OfferDetailFragment)mFragments.get(viewpager.getCurrentItem())).mUnits[viewpager.getCurrentItem()];
        CenterEditBaoJiaDialog mDialog = CenterEditBaoJiaDialog.getInstance(TextUtils.isEmpty(provice)?getIntent().getStringExtra("provice"):provice,types[viewpager.getCurrentItem()]
        ,unit);
        mDialog.setCallback(new CenterEditBaoJiaDialog.Callback1() {
            @Override
            public void run(String provice, String type, String price, String content) {
                goRequestBaoJia(provice,type,price,content);
            }
        });
        mDialog.showDialog(getSupportFragmentManager());
    }

    private void initData() {
        viewpager.setIsNoScroll(false);
        toolbar.setText(types[initPos]);
        initTabLayout();
    }

    private void initViews() {
        toolbar =findViewById(R.id.toolbar);
        tabLayout =findViewById(R.id.tabLayout);
        viewpager =findViewById(R.id.viewpager);


    }
    private void initTabLayout() {
        mFragments.clear();
        ArrayList<String> mTitles =new ArrayList<>();
        for (int i=0;i<types.length;i++) {
            mFragments.add(OfferDetailFragment.getInstance(i, getIntent().getStringExtra("provice")));
            String title=types[i];
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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
    private int  toPositionByName(String name){
        for (int i=0;i<types.length;i++){
            if (types[i].equals(name)){
                return i;
            }
        }
        return 0;
    }
    private void goRequestBaoJia(String provice,String type,String price,String content){
        BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("加载中",false));
        requestPost(HttpUrls.URL_BAOJIA_PUBLISH(),new ApiParams().with("type",type).with("provice",provice).with("price",price).with("content",content)
                , CommonEntity.class, new HttpCallBack<CommonEntity>() {
                    @Override
                    public void onFailure(String message) {
                        MyToast.myToast(getApplicationContext(),message);
                        BeautyDefine.getOpenPageDefine(OfferDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                    }

                    @Override
                    public void onSuccess(CommonEntity s) {
                        MyToast.myToast(getApplicationContext(),"报价成功");
                        BeautyDefine.getOpenPageDefine(OfferDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                        ((OfferDetailFragment)mFragments.get(viewpager.getCurrentItem())).goPercentageProData(null,0);
                    }
                });

    }
}
