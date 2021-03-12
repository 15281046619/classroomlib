package com.xingwang.classroom.ui;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;


import com.xingwang.classroom.ClassRoomLibUtils;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.LiveListAdapter;
import com.xingwang.classroom.bean.LiveCategoryBean;
import com.xingwang.classroom.bean.LiveListBean;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.utils.NoDoubleClickUtils;
import com.xingwang.classroom.view.CustomToolbar;
import com.xingwang.classroom.view.VpSwipeRefreshLayout;
import com.xingwang.classroom.view.WrapContentLinearLayoutManager;
import com.xingwang.classroom.view.loadmore.EndlessRecyclerOnScrollListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Date:2020/6/2
 * Time;10:54
 * author:baiguiqiang
 */
public class LiveListActivity extends BaseNetActivity {
    private int page =1;
    private CustomToolbar toolbar;
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private int pageSum = 10;
    private List<LiveListBean.DataBean> mData = new ArrayList<>();
    private LiveListAdapter mAdapter;
    private int clickPos;
    private boolean isRequesting =false;
    @Override
    protected int layoutResId() {
        return R.layout.activity_live_list_classroom;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            clickPos=  savedInstanceState.getInt("clickPos");
            mData = (List<LiveListBean.DataBean>) savedInstanceState.getSerializable("mData");
        }
        //if (!isStartLaunch()) {
            setNavigationBarColor(android.R.color.black);
            initView();
            initData();
            initListener();
      //  }
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (mData.size()>0&&!isRequesting)
                    getRequestData(Constants.LOAD_DATA_TYPE_MORE);

            }
        });
    }

    private void initData() {
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(() -> getRequestData(Constants.LOAD_DATA_TYPE_INIT));
        getCategory();
        //getRequestData(Constants.LOAD_DATA_TYPE_INIT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("clickPos",clickPos);
        outState.putSerializable("mData", (Serializable) mData);
        super.onSaveInstanceState(outState);

    }

    private int categoryId =-1;
    private void getCategory() {

        requestGet(HttpUrls.URL_LIVE_CATEGORY(),new ApiParams(), LiveCategoryBean.class, new HttpCallBack<LiveCategoryBean>() {

            @Override
            public void onFailure(String message) {
                swipeRefreshLayout.setRefreshing(false);
                MyToast.myToast(getApplicationContext(),message);
            }

            @Override
            public void onSuccess(LiveCategoryBean liveCategoryBean) {
                switch (HttpUrls.URL_TYPE){
                    case ClassRoomLibUtils.TYPE_ZY:
                        findCategoryId(liveCategoryBean.getData(),"猪");
                        break;
                    case ClassRoomLibUtils.TYPE_JQ:
                        findCategoryId(liveCategoryBean.getData(),"禽");
                        break;
                    case ClassRoomLibUtils.TYPE_NY:
                        findCategoryId(liveCategoryBean.getData(),"牛羊");
                        break;
                    case ClassRoomLibUtils.TYPE_SC:
                        findCategoryId(liveCategoryBean.getData(),"水产");
                        break;


                }
                getRequestData(Constants.LOAD_DATA_TYPE_INIT);
            }
        });
    }
    private void findCategoryId(List<LiveCategoryBean.DataBean> data, String name){
        for(int i=0;i<data.size();i++){
            if (data.get(i).getTitle().contains(name)){
                categoryId =data.get(i).getId();
                break;
            }
        }
    }
    private void getRequestData(int loadDataTypeInit){
        if (loadDataTypeInit!=Constants.LOAD_DATA_TYPE_MORE){
            page=1;
        }
        ApiParams mApiParams = new ApiParams().with("page", page).with("page_num", String.valueOf(pageSum));
        if (categoryId!=-1){
            mApiParams.with("category_id",categoryId);
        }
        isRequesting =true;
        requestGet(HttpUrls.URL_LIVE_LISTS(),mApiParams, LiveListBean.class, new HttpCallBack<LiveListBean>() {

            @Override
            public void onFailure(String message) {
                if (loadDataTypeInit!=Constants.LOAD_DATA_TYPE_MORE)
                    swipeRefreshLayout.setRefreshing(false);
                MyToast.myToast(getApplicationContext(),message);
                isRequesting= false;
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
                isRequesting= false;
                page++;
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
        if (requestCode==100&&mAdapter!=null&&mData!=null&&mData.size()>clickPos){
            if (resultCode==100){//正在直播
                mAdapter.notifyDataSetChanged();
            }else if (resultCode==101){//直播结束
                mData.get(clickPos).setIs_end(2);
                mAdapter.notifyDataSetChanged();
            }else if (resultCode==102){//移除该直播间
                mData.remove(clickPos);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
