package com.xingwang.classroom.ui;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xingwang.classroom.ClassRoomLibUtils;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.LiveListAdapter;
import com.xingwang.classroom.bean.LiveListBean;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.utils.NoDoubleClickUtils;
import com.xingwang.classroom.view.CustomToolbar;
import com.xingwang.classroom.view.VpSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Date:2020/6/2
 * Time;10:54
 * author:baiguiqiang
 */
public class LiveListActivity extends BaseNetActivity {

    private CustomToolbar toolbar;
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private int pageSum = 10;
    private List<LiveListBean.DataBean> mData = new ArrayList<>();
    private LiveListAdapter mAdapter;
    private int clickPos;
    @Override
    protected int layoutResId() {
        return R.layout.activity_live_list_classroom;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationBarColor(android.R.color.black);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(() -> getRequestData(Constants.LOAD_DATA_TYPE_INIT));
        getRequestData(Constants.LOAD_DATA_TYPE_INIT);
    }
    private void getRequestData(int loadDataTypeInit){
        requestGet(HttpUrls.URL_LIVE_LISTS(),new ApiParams().with("page",1 ).with("page_num",String.valueOf(pageSum)), LiveListBean.class, new HttpCallBack<LiveListBean>() {

            @Override
            public void onFailure(String message) {
                if (loadDataTypeInit!=Constants.LOAD_DATA_TYPE_MORE)
                swipeRefreshLayout.setRefreshing(false);
                MyToast.myToast(getApplicationContext(),message);
            }

            @Override
            public void onSuccess(LiveListBean lectureListsBean) {
                if (loadDataTypeInit!=Constants.LOAD_DATA_TYPE_MORE){
                    swipeRefreshLayout.setRefreshing(false);
                    mData.clear();
                }
                mData.addAll(lectureListsBean.getData());
                if(lectureListsBean.getData().size()<pageSum){
                    initAdapter(3);
                }else
                    initAdapter(1);
            }
        });
    }
    private void initAdapter(int state){
        if (mAdapter==null) {
            mAdapter = new LiveListAdapter(mData);
            mAdapter.setLoadStateNoNotify(state);
            mAdapter.setOnItemClickListener((view, position) -> {
                if (!NoDoubleClickUtils.isDoubleClick()) {
                    clickPos =position;
                    ClassRoomLibUtils.startForResultLiveDetailActivity(LiveListActivity.this, String.valueOf(mData.get(position).getId()),
                            mData.get(position).getIs_end() == 1, 100);
                }
            });
            recyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.setLoadStateNoNotify(state);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100){
            if (resultCode==100){//正在直播
                mAdapter.notifyDataSetChanged();
            }else if (resultCode==101){//直播结束
                mData.get(clickPos).setIs_end(2);
                mAdapter.notifyDataSetChanged();
            }

        }
    }
}
