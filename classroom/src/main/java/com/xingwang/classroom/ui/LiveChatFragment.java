package com.xingwang.classroom.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beautydefinelibrary.BeautyDefine;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.DetailAdapter;
import com.xingwang.classroom.adapter.LiveChatAdapter;
import com.xingwang.classroom.bean.LiveChatListBean;
import com.xingwang.classroom.bean.OnlineCountBean;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Date:2020/8/13
 * Time;15:24
 * author:baiguiqiang
 */
public class LiveChatFragment extends BaseLazyLoadFragment {
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private List<LiveChatListBean.DataBean.ItemsBean> mData =new ArrayList<>();
    private LiveChatAdapter mAdapter;
    private int maxSum =10;
    public static LiveChatFragment getInstance(String id,String fixedStr){
        LiveChatFragment mFragment = new LiveChatFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        bundle.putString("fixedStr",fixedStr);//置顶内容 ''为不置顶
        mFragment.setArguments(bundle);
        return mFragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live_chat,container,false);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (mData.size()>0)
            requestData(Constants.LOAD_DATA_TYPE_MORE,mData.get(0).getId());
        });

        return view;
    }

    @Override
    public void initData() {
        requestData(Constants.LOAD_DATA_TYPE_INIT,Integer.MAX_VALUE);
    }
    public void initAdapter(int sum){
        if (mAdapter==null) {
            mAdapter = new LiveChatAdapter(mData, getActivity());
            mAdapter.setLoadState(2);
            recyclerView.setAdapter(mAdapter);
            recyclerView.post(() -> {
                //设置第一条可见的消息
                // 如果ListView的刷新还没有完成，直接就调用setSelection，就会导致无效。
                recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
            });

        }else {
            mAdapter.notifyDataSetChanged();
            if (sum>0)
            recyclerView.post(() -> {
                //设置第一条可见的消息
                // 如果ListView的刷新还没有完成，直接就调用setSelection，就会导致无效。
                recyclerView.scrollToPosition(sum);
            });
        }
        swipeRefreshLayout.setRefreshing(false);

    }

    private void requestData(int loadDataTypeInit,int preId) {
        requestGet(HttpUrls.URL_LIVE_CHAT_LISTS(),new ApiParams().with("live_id",getArguments().getString("id")).with("num",maxSum).with("pre_id",preId),
                LiveChatListBean.class, new HttpCallBack<LiveChatListBean>() {

                    @Override
                    public void onFailure(String message) {
                        if (loadDataTypeInit!=Constants.LOAD_DATA_TYPE_INIT){
                            MyToast.myToast(getActivity(),message);
                        }
                    }

                    @Override
                    public void onSuccess(LiveChatListBean mVodListBean) {
                        Collections.reverse(mVodListBean.getData().getItems());//排序倒叙
                        if (loadDataTypeInit==Constants.LOAD_DATA_TYPE_INIT){
                            mData = mVodListBean.getData().getItems();
                        }else {
                            if (loadDataTypeInit==Constants.LOAD_DATA_TYPE_REFRESH)
                                mData.clear();
                            mData.addAll(0,mVodListBean.getData().getItems());
                        }
                        initAdapter(mVodListBean.getData().getItems().size());
                    }
                });
    }
}
