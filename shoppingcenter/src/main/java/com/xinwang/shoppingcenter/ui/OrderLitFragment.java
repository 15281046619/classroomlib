package com.xinwang.shoppingcenter.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xinwang.bgqbaselib.base.BaseLazyLoadFragment;
import com.xinwang.shoppingcenter.R;

/**
 * Date:2021/4/23
 * Time;9:21
 * author:baiguiqiang
 */
public class OrderLitFragment extends BaseLazyLoadFragment {
    private  View view;
    private RecyclerView recyclerView;
    private int pageNum =10;
    private int curPage =1;
    private RelativeLayout rl_empty;
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_productlist_shoppingcenter,container,false);
        recyclerView =view.findViewById(R.id.recyclerView);
        rl_empty =view.findViewById(R.id.rl_empty);
        rl_empty.setBackgroundResource(R.color.BGClassRoom);
        return null;
    }

    @Override
    public void initData() {

    }
}
