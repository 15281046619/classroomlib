package com.xingwang.essay.fragment;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xingwang.essay.R;
import com.xingwang.essay.adapter.EssayListAdapter;
import com.xingwang.essay.base.BaseLazyLoadFragment;
import com.xingwang.essay.bean.Essay;
import com.xingwang.swip.StateFrameLayout;
import com.xingwang.swip.SwipeRefreshAdapterView;
import com.xingwang.swip.SwipeRefreshListView;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.HttpUtil;
import com.xingwang.swip.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 文章列表
 */
public class EssayListFragment extends BaseLazyLoadFragment implements OnRefreshListener, OnLoadMoreListener {
    private static final String ARG_PARAM = "param";

    public static final String MAX_NUM="999999";

    private View view;
    protected SmartRefreshLayout swipe_essay;
    //protected VpSwipeRefreshLayout swipe_essay;
    protected MaterialHeader swipe_header;
    protected RecyclerView recycler_essay;

    protected TextView tv_all_empty;

    private EssayListAdapter essayListAdapter;
    private List<Essay> allEssayList=new ArrayList<>();//全部文章集

    private String title_id;
    private HashMap<String,String> params=new HashMap<>();

    private String forwardId="0";//第一篇文章的id
    private String backId="999999";//最后一篇文章的id

    public EssayListFragment() {
        // Required empty public constructor
    }

    public static EssayListFragment newInstance(String param) {
        EssayListFragment fragment = new EssayListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.essay_fragment_essay_list,container,false);
        swipe_essay=view.findViewById(R.id.swipe_essay);
        swipe_header=view.findViewById(R.id.swipe_header);
        recycler_essay=view.findViewById(R.id.recycler_essay);
        tv_all_empty=view.findViewById(R.id.tv_all_empty);

        return view;
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            title_id = getArguments().getString(ARG_PARAM);
        }

        swipe_header.setColorSchemeResources(R.color.reslib_colorAccent);
        swipe_essay.setEnableAutoLoadMore(false);
        swipe_essay.setOnLoadMoreListener(this);
        swipe_essay.setOnRefreshListener(this);

        getFirstData();

        essayListAdapter=new EssayListAdapter(getActivity(),allEssayList);
        recycler_essay.setLayoutManager(new LinearLayoutManager(getActivity()));

        recycler_essay.setAdapter(essayListAdapter);

    }

    public void setTitle(String id){
        title_id=id;
        getFirstData();
    }

    /**第一次获取数据*/
    private void getFirstData(){

        if (swipe_essay!=null){
            swipe_essay.autoRefresh();
        }
        params.clear();
        params.put("category_id",title_id);
        params.put("backward_div_id",MAX_NUM);
        params.put("num","10");

        HttpUtil.get(Constants.ESSAY_LIST, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                if (swipe_essay!=null){
                    swipe_essay.finishRefresh();
                }
            }

            @Override
            public void onSuccess(String json) {

                allEssayList.clear();
                if (swipe_essay!=null){
                    swipe_essay.finishRefresh();
                }
                List<Essay> tempList=JsonUtils.jsonToList(json,Essay.class);

                allEssayList.addAll(0,tempList);

                if (EmptyUtils.isEmpty(allEssayList)){
                    tv_all_empty.setVisibility(View.VISIBLE);
                    swipe_essay.setVisibility(View.GONE);
                    return;
                }

                tv_all_empty.setVisibility(View.GONE);
                swipe_essay.setVisibility(View.VISIBLE);

                forwardId=allEssayList.get(0).getId();
                backId=allEssayList.get(allEssayList.size()-1).getId();
                essayListAdapter.notifyDataSetChanged();
            }
        });
    }

    /**获取最新数据*/
    private void getRefreshData(){

        params.clear();
        params.put("category_id",title_id);
        params.put("forward_div_id",forwardId);
        params.put("num","10");

        HttpUtil.get(Constants.ESSAY_LIST, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                swipe_essay.finishRefresh();
            }

            @Override
            public void onSuccess(String json) {
                swipe_essay.finishRefresh();
                List<Essay> tempList=JsonUtils.jsonToList(json,Essay.class);
                Collections.reverse(tempList);
                if (EmptyUtils.isEmpty(tempList)){
                    ToastUtils.showShortSafe("暂无新数据!");
                    return;
                }

                allEssayList.addAll(0,tempList);

                forwardId=String.valueOf(allEssayList.get(0).getId());
                backId=String.valueOf(allEssayList.get(allEssayList.size()-1).getId());

                essayListAdapter.notifyDataSetChanged();
            }
        });
    }


    /**加载更多数据*/
    private void loadMoreData(){

        params.clear();
        params.put("category_id",title_id);
        params.put("backward_div_id",backId);
        params.put("num","10");

        HttpUtil.get(Constants.ESSAY_LIST, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                swipe_essay.finishLoadMore();
            }

            @Override
            public void onSuccess(String json) {
                swipe_essay.finishLoadMore();

                List<Essay> tempList=JsonUtils.jsonToList(json,Essay.class);

                if (EmptyUtils.isEmpty(tempList)){
                    ToastUtils.showShortSafe("无更多数据!");
                    return;
                }
                allEssayList.addAll(tempList);
                backId=String.valueOf(allEssayList.get(allEssayList.size()-1).getId());

                essayListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        allEssayList.clear();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMoreData();
            }
        }, 1000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refreshLayout.getLayout().postDelayed(new Runnable() {
            @Override
            public void run() {
                getRefreshData();
            }
        }, 1000);
    }
}
