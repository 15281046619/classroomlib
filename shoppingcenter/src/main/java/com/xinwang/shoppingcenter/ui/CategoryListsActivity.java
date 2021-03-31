package com.xinwang.shoppingcenter.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.adapter.CategoryListViewpagerAdapter;
import com.xinwang.shoppingcenter.bean.CategoryBean;
import com.xinwang.shoppingcenter.dialog.CategorySelectDialog;
import com.xinwang.shoppingcenter.interfaces.ActivitySendFragmentListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类页面
 * Date:2021/3/24
 * Time;10:00
 * author:baiguiqiang
 */
public class CategoryListsActivity extends BaseNetActivity {
    private CustomToolbar toolbar;
    private TabLayout tabLayout;
    private LinearLayout llCategory;
    private TextView tvSelect;
    private ViewPager viewPager;
    private CategoryBean categoryBean;
    private List<Fragment> mFragments;
    private int curPosition=0;
    @Override
    protected int layoutResId() {
        return R.layout.activity_category_lists_shoppingcenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        requestGet(HttpUrls.URL_GOODS_CATEGORY_LISTS(),new ApiParams(), CategoryBean.class, new HttpCallBack<CategoryBean>() {

            @Override
            public void onFailure(String message) {
                MyToast.myToast(getApplicationContext(),message);
            }

            @Override
            public void onSuccess(CategoryBean categoryBean) {
                categoryBean.getData().add(0,new CategoryBean.DataBean(0,"全部",new ArrayList<>()));
                initTabLayout(categoryBean);
            }
        });
    }
    private int initPos =0;
    private void initTabLayout(CategoryBean categoryBean) {
        this.categoryBean =categoryBean;
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
            mFragments.add(ProductListsFragment.getInstance(null,i==0?null:categoryBean.getData().get(i).getId()+""));
            mTitles.add(categoryBean.getData().get(i).getTitle());
            tabLayout.addTab(tabLayout.newTab().setText(categoryBean.getData().get(i).getTitle()));
        }
        CategoryListViewpagerAdapter mViewPagerAdapter = new CategoryListViewpagerAdapter(getSupportFragmentManager(), mFragments,mTitles);
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(initPos);
        llCategory.setVisibility(View.VISIBLE);
    }
    private void initListener() {
        toolbar.setNavigationOnClickListener(v -> finish());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                curPosition=i;
                if (i==0||categoryBean.getData().get(curPosition).getAttr().size()==0){
                    tvSelect.setEnabled(false);
                }else {

                    tvSelect.setEnabled(true);
                }
                tvSelect.setSelected(!categoryBean.getData().get(curPosition).getSelectAttr().isEmpty());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tvSelect.setOnClickListener(v -> {
            if (categoryBean!=null&&curPosition!=0&&tvSelect.isEnabled())
                CategorySelectDialog.getInstance(categoryBean.getData().get(curPosition)).setCallback(categoryBean -> {
                    this.categoryBean.getData().set(curPosition,categoryBean);
                    tvSelect.setSelected(!this.categoryBean.getData().get(curPosition).getSelectAttr().isEmpty());
                    ((ActivitySendFragmentListener)mFragments.get(curPosition)).onActivitySendFragment(this.categoryBean.getData().get(curPosition).getSelectAttr());


            }).showDialog(getSupportFragmentManager());
        });
    }

    private void initView() {
        toolbar =findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager =findViewById(R.id.viewPager);
        tvSelect =findViewById(R.id.tvSelect);
        llCategory =findViewById(R.id.llCategory);
    }
}
