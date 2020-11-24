package com.xingwang.classroom.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beautydefinelibrary.BeautyDefine;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.GiftTopsAdapter;
import com.xingwang.classroom.bean.GiftTopsBean;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.view.DividerItemDecoration;
import com.xingwang.classroom.view.VpSwipeRefreshLayout;
import com.xingwang.classroom.view.WrapContentLinearLayoutManager;


import java.util.ArrayList;
import java.util.Objects;

/**
 * Date:2020/8/13
 * Time;15:24
 * author:baiguiqiang
 */
public class LiveGiftTopsFragment extends BaseLazyLoadFragment {

    private VpSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private GiftTopsAdapter mGifTopsAdapter;

    public static LiveGiftTopsFragment getInstance(String id){
        LiveGiftTopsFragment mFragment = new LiveGiftTopsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA,id);
        mFragment.setArguments(bundle);
        return mFragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live_good_list_classroom,container,false);
         swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        recyclerView=view.findViewById(R.id.recycler_view);


        return view;
    }

    @Override
    public void initData() {


        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setRefreshing(true);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), DividerItemDecoration.VERTICAL_LIST));
        mGifTopsAdapter=new GiftTopsAdapter();
        recyclerView.setAdapter(mGifTopsAdapter);
        mGifTopsAdapter.setOnItemClick((position, child, type) -> BeautyDefine.getOpenPageDefine(getActivity()).toPersonal(mGifTopsAdapter.getData().get(position).getUser().getId()));
        swipeRefreshLayout.setOnRefreshListener(() -> getRequestData());
        getRequestData();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mGifTopsAdapter!=null&&!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
            getRequestData();
        }
    }



    public void getRequestData() {
        assert getArguments() != null;
        String id=getArguments().getString(Constants.DATA);
        requestGet(HttpUrls.URL_GIFT_TOPS(),new ApiParams().with("live_id",id), GiftTopsBean.class, new HttpCallBack<GiftTopsBean>() {

            @Override
            public void onFailure(String message) {
                swipeRefreshLayout.setRefreshing(false);
                if (getActivity()!=null)
                    MyToast.myToast(getActivity().getApplicationContext(),message);
            }

            @Override
            public void onSuccess(GiftTopsBean giftTopsBean) {
                swipeRefreshLayout.setRefreshing(false);
                if (giftTopsBean.getData().size()>0) {
                    mGifTopsAdapter.setData((ArrayList<GiftTopsBean.DataBean>) giftTopsBean.getData());
                    mGifTopsAdapter.notifyDataSetChanged();
                }

            }
        });
    }



}
