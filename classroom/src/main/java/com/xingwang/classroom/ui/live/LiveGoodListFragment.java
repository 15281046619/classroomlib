package com.xingwang.classroom.ui.live;

import android.os.Bundle;
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
import com.xingwang.classroom.ui.BaseLazyLoadFragment;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.view.VpSwipeRefreshLayout;
import com.xingwang.classroom.view.WrapContentLinearLayoutManager;

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

        recycler_view.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        goodListAdapter=new GoodListAdapter(getActivity());
        recycler_view.setAdapter(goodListAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> getRequestData());

        goodListAdapter.setOnChildItemClickListener(goodBean -> LiveOrderActivity.getIntent(getContext(),goodBean));

        getRequestData();
    }

    private void getRequestData(){
        requestGet(HttpUrls.URL_GOOD_LISTS(),new ApiParams().with("live_id",getArguments().getString(Constants.DATA)), GoodListBean.class, new HttpCallBack<GoodListBean>() {

            @Override
            public void onFailure(String message) {
                swipeRefreshLayout.setRefreshing(false);
                if (getActivity()!=null)
                MyToast.myToast(getActivity().getApplicationContext(),message);
            }

            @Override
            public void onSuccess(GoodListBean goodListBean) {
                swipeRefreshLayout.setRefreshing(false);
                if (goodListBean.getData().size()>0)
                    goodListAdapter.setDatas(goodListBean.getData());

            }
        });
    }
}
