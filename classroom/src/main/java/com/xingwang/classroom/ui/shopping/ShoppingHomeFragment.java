package com.xingwang.classroom.ui.shopping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingwang.classroom.R;
import com.xingwang.classroom.ui.BaseLazyLoadFragment;


/**
 * Date:2021/3/15
 * Time;13:17
 * author:baiguiqiang
 */
public class ShoppingHomeFragment  extends BaseLazyLoadFragment {
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_shopping_home_classroom,container,false);
        return view;
    }

    @Override
    public void initData() {

    }
}
