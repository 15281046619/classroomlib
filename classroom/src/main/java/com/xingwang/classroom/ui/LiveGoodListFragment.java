package com.xingwang.classroom.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.GoodListAdapter;
import com.xingwang.classroom.bean.GoodListBean;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.view.VpSwipeRefreshLayout;

/**
 * Date:2020/8/13
 * Time;15:24
 * author:baiguiqiang
 */
public class LiveGoodListFragment extends BaseLazyLoadFragment {

    protected RecyclerView recycler_view;
    protected GoodListAdapter goodListAdapter;
    private VpSwipeRefreshLayout swipeRefreshLayout;

    public static LiveGoodListFragment getInstance(String des){
        LiveGoodListFragment mFragment = new LiveGoodListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA,des);
        mFragment.setArguments(bundle);
        return mFragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live_good_list_classroom,container,false);

        recycler_view=view.findViewById(R.id.recycler_view);
        swipeRefreshLayout =view.findViewById(R.id.swipeRefreshLayout);

        return view;
    }

    @Override
    public void initData() {

        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setRefreshing(true);

        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        goodListAdapter=new GoodListAdapter(getActivity());
        recycler_view.setAdapter(goodListAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> getRequestData());

        goodListAdapter.setOnChildItemClickListener(new GoodListAdapter.OnChildItemClickListener() {
            @Override
            public void onClick(GoodListBean.GoodBean goodBean) {
                OrderActivity.getIntent(getContext(),goodBean);
            }
        });

        getRequestData();
    }

    private void getRequestData(){
        requestGet(HttpUrls.URL_GOOD_LISTS,new ApiParams().with("live_id",getArguments().getString(Constants.DATA)), GoodListBean.class, new HttpCallBack<GoodListBean>() {

            @Override
            public void onFailure(String message) {
                swipeRefreshLayout.setRefreshing(false);
                MyToast.myToast(getContext().getApplicationContext(),message);
            }

            @Override
            public void onSuccess(GoodListBean goodListBean) {
                swipeRefreshLayout.setRefreshing(false);
                goodListAdapter.setDatas(goodListBean.getData());
            }
        });
    }
}
