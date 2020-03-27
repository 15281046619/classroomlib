package com.xingwang.classroom.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xingwang.classroom.bean.CategoryBean;

import java.util.List;

/**
 * Date:2019/8/21
 * Time;14:12
 * author:baiguiqiang
 */
public class HomeViewpagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<CategoryBean.DataBean> titles;
    public HomeViewpagerAdapter(FragmentManager fm,List<Fragment> fragments,List<CategoryBean.DataBean> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position).getTitle();
    }
}
