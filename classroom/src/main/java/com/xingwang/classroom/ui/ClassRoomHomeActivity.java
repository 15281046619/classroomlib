package com.xingwang.classroom.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.beautydefinelibrary.SystemDefine;
import com.xingwang.classroom.ClassRoomLibUtils;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.HomeViewpagerAdapter;
import com.xingwang.classroom.bean.ADGroupBean;
import com.xingwang.classroom.bean.CategoryBean;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.GlideImageLoader;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.utils.StatusBarUtils;
import com.xingwang.classroom.view.CustomToolbar;
import com.xingwang.classroom.view.VpSwipeRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;


import java.util.ArrayList;
import java.util.List;

/**
 * Date:2019/8/20
 * Time;10:47
 * author:baiguiqiang
 */
public class ClassRoomHomeActivity extends BaseNetActivity {
    private TextView tvTitle;
    private AppBarLayout appBarLayout;
    private CustomToolbar toolBar;
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private Banner banner;
    private List<ADGroupBean.DataBean> mTitleImages =new ArrayList<>();
    private int statusBarHeight =0;
    private float appBarLayoutHeight;
    private TabLayout tabLayout;
    private ViewPager viewpager;
    private List<Fragment> mFragments;
    @Override
    protected int layoutResId() {
        return R.layout.activity_class_room_home_classroom;
    }

    @Override
    protected void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isStartLaunch()) {
            initViews();
            initSettingToolBarHeight();
            initSettingAppBarListener();
            initBannerListener();
            goRequestData(Constants.LOAD_DATA_TYPE_INIT);
        }
    }

    private void goRequestData(int type){
        requestGet(HttpUrls.URL_AD_LISTS(),new ApiParams().with("group", "课程推荐"), ADGroupBean.class, new HttpCallBack<ADGroupBean>() {

            @Override
            public void onFailure(String message) {
                swipeRefreshLayout.setRefreshing(false);
                MyToast.myToast(getApplicationContext(),message);
            }

            @Override
            public void onSuccess(ADGroupBean lectureListsBean) {
                showTitleImages(lectureListsBean.getData());
                getCateGory(type);
            }
        });

    }

    private void getCateGory(int type) {
        if (type==Constants.LOAD_DATA_TYPE_REFRESH) {
            ClassRoomHomeFragment mFragment = (ClassRoomHomeFragment) mFragments.get(viewpager.getCurrentItem());
            mFragment.requestHttpData(type,Integer.MAX_VALUE);
        }else
            requestGet(HttpUrls.URL_CATEGORYS(), new ApiParams(), CategoryBean.class, new HttpCallBack<CategoryBean>() {
                @Override
                public void onFailure(String message) {
                    swipeRefreshLayout.setRefreshing(false);
                    MyToast.myToast(getApplicationContext(),message);
                }


                @Override
                public void onSuccess(CategoryBean categoryBean) {

                    categoryBean.getData().add(0, new CategoryBean.DataBean(-1,"全部"));
                    initTabLayout(categoryBean);

                }
            });
    }

    void showTitleImages(List<ADGroupBean.DataBean> mTitleImages){
        this.mTitleImages = mTitleImages;
        banner.setImages(getTitleImages()).setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE).setBannerAnimation(Transformer.CubeOut)
                .setBannerTitles(getTitleDes()).setImageLoader(new GlideImageLoader()).setDelayTime(3000).start();
    }

    private List<String> getTitleImages() {
        List<String> mLists =new ArrayList<>();
        for (ADGroupBean.DataBean mBean:mTitleImages){
            mLists.add(mBean.getPic());
        }
        return mLists;
    }
    private List<String> getTitleDes() {
        List<String> mLists =new ArrayList<>();
        for (ADGroupBean.DataBean mBean:mTitleImages){
            mLists.add(mBean.getTitle());
        }
        return mLists;
    }
    private int initPos =0;
    private void initTabLayout(CategoryBean categoryBean) {
        String type =getIntent().getStringExtra("type");
        Uri uri = getIntent().getData();
        if (uri != null) {
            type = uri.getQueryParameter("type");
        }
        mFragments =new ArrayList<>();
        tabLayout.removeAllTabs();
        ArrayList<String> mTitles =new ArrayList<>();
        for (int i=0;i<categoryBean.getData().size();i++) {
            if (!TextUtils.isEmpty(type)&& (categoryBean.getData().get(i).getId()+"").equals(type)&&initPos==0){
                initPos =i;
            }
            mFragments.add(ClassRoomHomeFragment.getInstance(i,categoryBean.getData().get(i).getId()+""));
            mTitles.add(categoryBean.getData().get(i).getTitle());
            tabLayout.addTab(tabLayout.newTab().setText(categoryBean.getData().get(i).getTitle()));
        }
        HomeViewpagerAdapter mViewPagerAdapter = new HomeViewpagerAdapter(getSupportFragmentManager(), mFragments,mTitles);
        viewpager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setCurrentItem(initPos);
    }

    @Override
    protected void onPause() {
        super.onPause();
        banner.stopAutoPlay();
    }

    private void initBannerListener() {
        banner.setOnBannerListener(position -> {
            if(mTitleImages!=null) {
                try {
                    ClassRoomLibUtils.startActivityForUri(ClassRoomHomeActivity.this,mTitleImages.get(position).getUri());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void initSettingAppBarListener() {
        appBarLayoutHeight= getResources().getDimension(R.dimen.dp_200_classroom);
        appBarLayout.addOnOffsetChangedListener((AppBarLayout.BaseOnOffsetChangedListener<AppBarLayout>) (appBarLayout, verticalOffset) -> {
            int offset = Math.abs(verticalOffset); //目的是将负数转换为绝对正数；
            //标题栏的渐变
            if (statusBarHeight==0&&toolBar!=null) {
                statusBarHeight = toolBar.getMeasuredHeight();
                appBarLayoutHeight= appBarLayoutHeight-statusBarHeight;
            }

            offset= (int) Math.ceil(255* offset/appBarLayoutHeight);

            if (offset < 255) {
                swipeRefreshLayout.setEnabled(offset==0);
                toolBar.getBackground().mutate().setAlpha(offset);//添加mutate设置透明度不影响其他页面，否则会影响
                tvTitle.setTextColor(Color.argb(offset, 255, 255, 255));
            }else{
                toolBar.getBackground().mutate().setAlpha(255);
                tvTitle.setTextColor(Color.argb(255, 255, 255, 255));
            }
        });
    }

    private void initViews() {
        tvTitle= findViewById(R.id.tv_title);
        toolBar= findViewById(R.id.toolbar);
        tabLayout= findViewById(R.id.tab_layout);
        viewpager= findViewById(R.id.viewpager);
        appBarLayout= findViewById(R.id.app_bar_layout);
        swipeRefreshLayout= findViewById(R.id.swipe_refresh_layout);

        banner= findViewById(R.id.banner);
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (mFragments!=null) {
                goRequestData(Constants.LOAD_DATA_TYPE_REFRESH);
            }else {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        findViewById(R.id.iv_search).setOnClickListener(v ->
                BeautyDefine.getOpenPageDefine(ClassRoomHomeActivity.this).toSearch(OpenPageDefine.SearchType.COURSE));
        toolBar.setNavigationOnClickListener(v -> finish());
    }
    void refreshFinish(){
        swipeRefreshLayout.setRefreshing(false);
    }

    private void initSettingToolBarHeight() {
        int mStatusHeight = StatusBarUtils.getStatusHeight(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.LayoutParams mLayoutParams = tvTitle.getLayoutParams();
            mLayoutParams.height = mLayoutParams.height + mStatusHeight;
            tvTitle.setLayoutParams(mLayoutParams);
            tvTitle.setPadding(0, mStatusHeight, 0, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
