package com.xingwang.circle.fragment;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.circle.CardDetailActivity;
import com.xingwang.circle.R;
import com.xingwang.circle.adapter.CardAdapter;
import com.xingwang.circle.base.BaseLazyLoadFragment;
import com.xingwang.circle.bean.Card;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.StateFrameLayout;
import com.xingwang.swip.SwipeRefreshAdapterView;
import com.xingwang.swip.SwipeRefreshListView;
import com.xingwang.swip.utils.HttpUtil;
import com.xingwang.swip.utils.JsonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 文章列表
 */
public class CardListFragment extends BaseLazyLoadFragment {
    private static final String ARG_TITLE = "param";
    private static final String ARG_ID = "param1";

    public static final String MAX_NUM="999999";

    private View view;
    private StateFrameLayout status_layout;
    private SwipeRefreshListView list_card;

    private CardAdapter cardAdapter;
    private List<Card> allEssayList=new ArrayList<>();//帖子集合

    private String title;
    private String forum_id;//版块id
    private HashMap<String,String> params=new HashMap<>();

    private String forwardId="0";//第一篇文章的id
    private String backId="999999";//最后一篇文章的id

    public CardListFragment() {
        // Required empty public constructor
    }

    public static CardListFragment newInstance(String title,String forum_id) {
        CardListFragment fragment = new CardListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_ID, forum_id);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.circle_fragment_essay_list,container,false);
        status_layout =view.findViewById(R.id.status_layout);
        list_card =view.findViewById(R.id.list_card);

        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void initData() {

        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            forum_id = getArguments().getString(ARG_ID);
        }

        list_card.autoRefresh(new int[]{R.color.reslib_colorAccent});

        list_card.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CardDetailActivity.getIntent(getContext(),allEssayList.get(position).getId());
            }
        });

        //下拉刷新
        list_card.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRefreshData();
        }
        });

        //上拉加载
        list_card.setOnListLoadListener(new SwipeRefreshAdapterView.OnListLoadListener() {
            @Override
            public void onListLoad() {
              loadMoreData();
            }
        });

        getFirstData();

        cardAdapter=new CardAdapter(getActivity(),allEssayList);
        list_card.setAdapter(cardAdapter);

    }

    /**第一次获取数据*/
    private void getFirstData(){
        params.clear();
        params.put("forum_id",forum_id);
        params.put("category",title);
        params.put("backward_div_id",MAX_NUM);
        params.put("num","10");

        HttpUtil.get(Constants.CARD_LIST, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                list_card.setRefreshing(false);
            }

            @Override
            public void onSuccess(String json) {

                allEssayList.clear();
                list_card.setRefreshing(false);
                List<Card> tempList= JsonUtils.jsonToList(json,Card.class);
                if (EmptyUtils.isNotEmpty(tempList)){
                    status_layout.normal();
                    allEssayList.addAll(tempList);
                    addData();
                }else {
                    status_layout.empty();
                }
            }
        });
    }

    /**获取最新数据*/
    private void getRefreshData(){

        params.clear();
        params.put("forum_id",forum_id);
        params.put("category",title);
        params.put("forward_div_id",forwardId);
        params.put("num","10");

        HttpUtil.get(Constants.CARD_LIST, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                list_card.setRefreshing(false);
            }

            @Override
            public void onSuccess(String json) {
                list_card.setRefreshing(false);
                List<Card> tempList=JsonUtils.jsonToList(json,Card.class);

                if (EmptyUtils.isEmpty(tempList)){
                    ToastUtils.showShortSafe("暂无新数据!");
                    return;
                }

                Collections.reverse(tempList);
                allEssayList.addAll(0,tempList);

                addData();
            }
        });
    }

    /**加载更多数据*/
    private void loadMoreData(){

        params.clear();
        params.put("forum_id",forum_id);
        params.put("category",title);
        params.put("backward_div_id",backId);
        params.put("num","10");

        HttpUtil.get(Constants.CARD_LIST, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                list_card.setLoading(false);
            }

            @Override
            public void onSuccess(String json) {

                list_card.setLoading(false);

                List<Card> tempList=JsonUtils.jsonToList(json,Card.class);

                if (EmptyUtils.isEmpty(tempList)){
                    ToastUtils.showShortSafe("无更多数据!");
                    return;
                }
                allEssayList.addAll(tempList);
                if (EmptyUtils.isNotEmpty(allEssayList)){
                    backId=String.valueOf(allEssayList.get(allEssayList.size()-1).getId());
                }else {
                    backId=MAX_NUM;
                }
                cardAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addData(){
        if (EmptyUtils.isNotEmpty(allEssayList)){
            forwardId=String.valueOf(allEssayList.get(0).getId());
            backId=String.valueOf(allEssayList.get(allEssayList.size()-1).getId());
        }else {
            backId=MAX_NUM;
        }

        if (EmptyUtils.isEmpty(allEssayList)){
            status_layout.empty();
        }else {
            status_layout.normal();
        }
        cardAdapter.notifyDataSetChanged();
    }

    /***
     * 获取客户的预览消息
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEventBus(String resultCode) {
        if (resultCode.equals(title)){
            if (EmptyUtils.isEmpty(allEssayList)){//此时无帖子
                Log.i("CardList","getFirstData");
                getFirstData();
            }else {
                Log.i("CardList","getRefreshData");
                list_card.setRefreshing(true);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        allEssayList.clear();
    }
}
