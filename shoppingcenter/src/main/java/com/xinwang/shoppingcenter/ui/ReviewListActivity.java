package com.xinwang.shoppingcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.bgqbaselib.view.VpSwipeRefreshLayout;
import com.xinwang.bgqbaselib.view.loadmore.EndlessRecyclerOnScrollListener;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.adapter.ShoppingReviewAdapter;
import com.xinwang.shoppingcenter.bean.ReviewListBean;
import com.xinwang.shoppingcenter.view.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/6/7
 * Time;10:04
 * author:baiguiqiang
 */
public class ReviewListActivity extends BaseNetActivity {
    private int curPage =1;
    private int pageSum = 10;
    private boolean isRequesting =false;
    private RecyclerView recyclerView;
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private List<ReviewListBean.DataBean> mData =new ArrayList<>();
    private ShoppingReviewAdapter mAdapter;
    private String mId;
    @Override
    protected int layoutResId() {
        return R.layout.activity_reviecw_list_shoppingcentert;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        mId =getIntent().getStringExtra("id");
        List<ReviewListBean.DataBean> mIntentData = (List<ReviewListBean.DataBean>) getIntent().getSerializableExtra("data");
        if (mIntentData==null||mIntentData.size()==0){
            requestFailureShow(getString(R.string.no_data_ClassRoom));
        }else {
            findViewById(R.id.rl_empty).setVisibility(View.GONE);
            mData.addAll(mIntentData);
            curPage++;
            if (mIntentData.size() < pageSum) {
                initAdapter(3);
            } else {
                initAdapter(1);
            }
        }
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage=1;
                initReview(Constants.LOAD_DATA_TYPE_INIT);
            }
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (mData.size()>0&&!isRequesting)
                    initReview(Constants.LOAD_DATA_TYPE_MORE);

            }
        });


    }

    private void initReview(int lodeType) {
        isRequesting =true;
        requestGet(HttpUrls.URL_GOODS_REVIEW_LISTS(),new ApiParams().with("goods_id",mId).with("page",curPage+"").with("page_num",pageSum+""), ReviewListBean.class, new HttpCallBack<ReviewListBean>() {

            @Override
            public void onFailure(String message) {
                swipeRefreshLayout.setRefreshing(false);
                MyToast.myToast(ReviewListActivity.this,message);
                if (lodeType==Constants.LOAD_DATA_TYPE_INIT) {
                    requestFailureShow(message);
                }
                isRequesting =false;
            }

            @Override
            public void onSuccess(ReviewListBean categoryBean) {
                swipeRefreshLayout.setRefreshing(false);
                if (lodeType!=Constants.LOAD_DATA_TYPE_MORE){
                    mData.clear();
                    if (lodeType==Constants.LOAD_DATA_TYPE_INIT&&(categoryBean.getData().size()==0)){
                        requestFailureShow(getString(R.string.no_data_ClassRoom));
                    }else {
                        findViewById(R.id.rl_empty).setVisibility(View.GONE);
                    }
                }
                if (categoryBean.getData()!=null) {
                    mData.addAll(categoryBean.getData());
                    curPage++;
                    if (categoryBean.getData().size() < pageSum) {
                        initAdapter(3);
                    } else {
                        initAdapter(1);
                    }
                }
                isRequesting =false;
            }
        });
    }

    private void initAdapter(int state) {

        if (mAdapter==null) {
            mAdapter = new ShoppingReviewAdapter(this,mData,true);
            mAdapter.setOnItemClickListener(new BaseLoadMoreAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    startActivity(new Intent(ReviewListActivity.this,ReviewDetailActivity.class).putExtra("data",mData.get(position)));
                }
            });
            mAdapter.setLoadStateNoNotify(state);
            recyclerView.setAdapter(mAdapter);

        }else {
            mAdapter.setLoadStateNoNotify(state);
            mAdapter.notifyDataSetChanged();
        }

    }

    private void requestFailureShow(String error){

        TextView tvMsg = findViewById(R.id.tv_msg);
        tvMsg.setText(error);
        CustomProgressBar progressbar = findViewById(R.id.progressbar);
        progressbar .setVisibility(View.GONE);
        if (!error.equals(getString(R.string.no_data_ClassRoom)))
            findViewById(R.id.rl_empty).setOnClickListener(v -> {
                progressbar.setVisibility(View.VISIBLE);
                tvMsg.setText("加载中...");
                initReview(Constants.LOAD_DATA_TYPE_INIT);
            });
    }
    private void initListener() {
        ((CustomToolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        recyclerView =findViewById(R.id.recyclerView);
        swipeRefreshLayout =findViewById(R.id.swipeRefreshLayout);
    }
}
