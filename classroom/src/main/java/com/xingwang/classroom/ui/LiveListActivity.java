package com.xingwang.classroom.ui;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.xingwang.classroom.R;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.view.CustomToolbar;
import com.xingwang.classroom.view.VpSwipeRefreshLayout;


/**
 * Date:2020/6/2
 * Time;10:54
 * author:baiguiqiang
 */
public class LiveListActivity extends BaseNetActivity {

    private CustomToolbar toolbar;
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationBarColor(android.R.color.black);
        initView();
        initData();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void initData() {
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(() -> getRequestData(Constants.LOAD_DATA_TYPE_INIT));
        getRequestData(Constants.LOAD_DATA_TYPE_INIT);
    }
    private void getRequestData(int type){
       /*requestGet(HttpUrls.URL_LIVE_LISTS,new ApiParams().with("page",1 ).with("page_num","10"), ADGroupBean.class, new HttpCallBack<ADGroupBean>() {

            @Override
            public void onFailure(String message) {
               swipeRefreshLayout.setRefreshing(false);
               MyToast.myToast(getApplicationContext(),message);
           }

            @Override
            public void onSuccess(ADGroupBean lectureListsBean) {

            }
        });*/
    }


    @Override
    protected int layoutResId() {
        return R.layout.activity_live_list;
    }
}
