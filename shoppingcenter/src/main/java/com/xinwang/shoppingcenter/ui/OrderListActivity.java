package com.xinwang.shoppingcenter.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.adapter.CategoryListViewpagerAdapter;
import com.xinwang.shoppingcenter.bean.CategoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/4/23
 * Time;8:59
 * author:baiguiqiang
 */
public class OrderListActivity extends BaseNetActivity {
    private CustomToolbar toolbar;
    private EditText etContent;
    private ImageView ivDelete;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> mFragments;
    @Override
    protected int layoutResId() {
        return R.layout.activity_order_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        etContent = findViewById(R.id.etContent);
        ivDelete = findViewById(R.id.ivDelete);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }
    private void initListener() {
        toolbar.setNavigationOnClickListener(v -> finish());
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s!=null&&s.length()>0){
                    ivDelete.setVisibility(View.VISIBLE);
                }else {
                    ivDelete.setVisibility(View.INVISIBLE);
                  //  showFragment(0,null);
                }
            }
        });
        etContent.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId== EditorInfo.IME_ACTION_SEARCH){
                String content =etContent.getText().toString().trim();
                if (!TextUtils.isEmpty(content)){
                    etContent.clearFocus();
                    //showFragment(1,content);
                }
                return true;
            }
            return false;
        });
       // mKeyBoardHelper.setOnKeyBoardStatusChangeListener((visible, keyBoardHeight) -> borHelperVisible =visible);
        ivDelete.setOnClickListener(v -> etContent.setText(""));
    }
    private int initPos =0;
    private void initTabLayout() {

        String type =getIntent().getStringExtra("state");
        Uri uri = getIntent().getData();
        if (uri != null) {
            type = uri.getQueryParameter("state");
        }
        mFragments =new ArrayList<>();
        tabLayout.removeAllTabs();
        ArrayList<String> mTitles =new ArrayList<>();
        String[] mLists = getResources().getStringArray(R.array.order_state_name_ShoppingCenter);
    /*    for (int i=0;i<mLists.length;i++) {

            if (!TextUtils.isEmpty(type)&& (categoryBean.getData().get(i).getId()+"").equals(type)&&initPos==0){
                initPos =i;
            }
            mFragments.add();
            mTitles.add(mLists[i]);
            tabLayout.addTab(tabLayout.newTab().setText(mLists[i]));
        }*/
        CategoryListViewpagerAdapter mViewPagerAdapter = new CategoryListViewpagerAdapter(getSupportFragmentManager(), mFragments,mTitles);
        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(initPos);
    }
}
