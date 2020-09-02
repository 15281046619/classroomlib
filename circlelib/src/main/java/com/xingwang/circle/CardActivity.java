package com.xingwang.circle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.xingwang.circle.adapter.CardListViewpagerAdapter;
import com.xingwang.circle.base.BaseActivity;
import com.xingwang.circle.fragment.CardListFragment;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.title.TopTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子
 * */
public class CardActivity extends BaseActivity{

    private static final int POST_CODE = 100;
    private static final String PARAM_ID = "id";

    protected TopTitleView title;
    protected TabLayout mTabLayout;
    protected ViewPager viewpager;

    private List<Fragment> mFragments=new ArrayList<>();
    private List<String> titleList=new ArrayList<>();

    private String forum_id;

    public static void getIntent(Context context,ArrayList<String> titleList,String id) {
        Intent intent = new Intent(context, CardActivity.class);
        intent.putStringArrayListExtra(Constants.INTENT_DATA,titleList);
        intent.putExtra(PARAM_ID,id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.circle_activity_card;
    }

    @Override
    protected void initView() {
        title = (TopTitleView) findViewById(R.id.title);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        mTabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.reslib_colorPrimary));

        title.setRightFristEnable(true);
        title.setRightFristText("发帖", new TopTitleView.OnRightFirstClickListener() {
            @Override
            public void onRightFirstClickListener(View v) {
               PostActivity.getIntent(CardActivity.this);
            }
        });

        title.setTitleText("帖子");
    }

    @Override
    public void initData() {
        titleList.clear();
        titleList.addAll(getIntent().getStringArrayListExtra(Constants.INTENT_DATA));
        forum_id=getIntent().getStringExtra(PARAM_ID);

        if (EmptyUtils.isNotEmpty(titleList)){
            for (String title:titleList){
                mTabLayout.addTab(mTabLayout.newTab().setText(title));
                mFragments.add(CardListFragment.newInstance(title,forum_id));
            }
            CardListViewpagerAdapter mViewPagerAdapter = new CardListViewpagerAdapter(getSupportFragmentManager(), mFragments,titleList);
            viewpager.setAdapter(mViewPagerAdapter);
            mTabLayout.setupWithViewPager(viewpager);
            //viewpager.setCurrentItem(pos);
        }
    }

}
