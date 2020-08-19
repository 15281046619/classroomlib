package com.xingwang.classroom.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingwang.classroom.R;

/**
 * Date:2020/8/13
 * Time;15:24
 * author:baiguiqiang
 */
public class LiveDesFragment extends BaseLazyLoadFragment {
    public static LiveDesFragment getInstance(String des){
        LiveDesFragment mFragment = new LiveDesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data",des);
        mFragment.setArguments(bundle);
        return mFragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live_des,container,false);

        return view;
    }

    @Override
    public void initData() {

    }
}
