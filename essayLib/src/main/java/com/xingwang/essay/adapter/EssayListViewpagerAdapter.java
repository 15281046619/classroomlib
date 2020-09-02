package com.xingwang.essay.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xingwang.essay.bean.EssayTitle;

import java.util.List;

public class EssayListViewpagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<EssayTitle> titles;

    public EssayListViewpagerAdapter(FragmentManager fm, List<Fragment> fragments, List<EssayTitle> titles) {
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
    public CharSequence getPageTitle(int position) {
        return (CharSequence)this.titles.get(position).getTitle();
    }
}
