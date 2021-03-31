package com.xinwang.shoppingcenter.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.FlowLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xinwang.bgqbaselib.base.BaseLazyLoadFragment;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.adapter.SearchHistoryAdapter;
import com.xinwang.shoppingcenter.view.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date:2021/3/23
 * Time;13:22
 * author:baiguiqiang
 */
public class ShoppingSearchHistoryFragment extends BaseLazyLoadFragment {
    private FlowLayout flowLayout;
    private RecyclerView recyclerView;
    private TextView tvHot;
    private ShoppingSearchActivity mActivity;
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_shopping_search_history,container,false);
        flowLayout =view.findViewById(R.id.flowLayout);
        tvHot =view.findViewById(R.id.tvHot);
        recyclerView =view.findViewById(R.id.recyclerView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void initData() {
        mActivity = (ShoppingSearchActivity) getActivity();
        iniHotSearch();
        initHistorySearch();
    }

    private void initHistorySearch() {
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getContext()));
        recyclerView.setAdapter(new SearchHistoryAdapter(mActivity.mSearchData, position -> {
            mActivity.showFragment(1,mActivity.mSearchData.get(position));
        }));

    }

    /**
     *热门搜索
     */
    private void iniHotSearch(){
        List<String> mList = new ArrayList(Arrays.asList(ShoppingCenterLibUtils.getHotSearch(getContext())));
        flowLayout.removeAllViews();
        if (mList.size()==0){
            tvHot.setVisibility(View.GONE);
        }else {
            tvHot.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i<(mList.size()); i++){
            TextView tv = new TextView(getActivity());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
            tv.setText(mList.get(i));
            tv.setBackgroundResource(R.drawable.shape_hot_search_item_bg_shoppingcenter);
            tv.setPadding(20,10,20,10);
            int finalI = i;
            tv.setOnClickListener(v -> {
                mActivity.showFragment(1,mList.get(finalI));
            });
            flowLayout.addView(tv);
        }
    }
}
