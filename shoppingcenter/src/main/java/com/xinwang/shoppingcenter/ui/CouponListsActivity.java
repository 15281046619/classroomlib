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
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.VpSwipeRefreshLayout;
import com.xinwang.bgqbaselib.view.loadmore.EndlessRecyclerOnScrollListener;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.adapter.CouponListAdapter;
import com.xinwang.shoppingcenter.adapter.ShoppingHomeAdapter;
import com.xinwang.shoppingcenter.bean.CouponBean;
import com.xinwang.shoppingcenter.interfaces.AdapterItemClickListener;
import com.xinwang.shoppingcenter.view.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/4/15
 * Time;13:28
 * author:baiguiqiang
 */
public class CouponListsActivity extends BaseNetActivity {
    private int curPage =1;
    private int pageSum = 10;
    private RecyclerView recyclerView;
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private TextView tvSelect;
    private CouponListAdapter mAdapter;
    private boolean isRequesting =false;
    private List<CouponBean.DataBean.CouponsBean> mData =new ArrayList<>();
    @Override
    protected int layoutResId() {
        return R.layout.activity_coupon_list_shoppingcenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();

        initListener();
    }
    private void initCoupon(int lodeType) {
        isRequesting =true;
        requestGet(HttpUrls.URL_COUPON_LISTS(), new ApiParams().with("type", 2).with("page", curPage).with("page_num", pageSum),
                CouponBean.class, new HttpCallBack<CouponBean>() {
                    @Override
                    public void onFailure(String message) {
                        swipeRefreshLayout.setRefreshing(false);
                        MyToast.myToast(CouponListsActivity.this,message);
                        if (lodeType==Constants.LOAD_DATA_TYPE_INIT) {
                            requestFailureShow(message);
                        }
                        isRequesting =false;
                    }

                    @Override
                    public void onSuccess(CouponBean commonEntity) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (lodeType!=Constants.LOAD_DATA_TYPE_MORE){
                            mData.clear();
                            if (lodeType==Constants.LOAD_DATA_TYPE_INIT&&(commonEntity.getData()==null||commonEntity.getData().getCoupons().size()==0)){
                                requestFailureShow(getString(R.string.no_data_ClassRoom));
                            }else {
                                findViewById(R.id.rl_empty).setVisibility(View.GONE);
                            }
                        }
                        if (commonEntity.getData()!=null) {
                            mData.addAll(commonEntity.getData().getCoupons());
                            curPage++;
                            if (commonEntity.getData().getCoupons().size() < pageSum) {
                                initAdapter(3);
                            } else {
                                initAdapter(1);
                            }
                        }
                        isRequesting =false;
                    }
                });
    }

    private void initAdapter(int state ) {
        if (mAdapter==null) {
            mAdapter = new CouponListAdapter(mData);
            mAdapter.curPos=getIntent().getIntExtra("pos",-1);
            if ( mAdapter.curPos!=-1){
                tvSelect.setText("已选择1张");
            }
            mAdapter.setOnItemClickListener(new BaseLoadMoreAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (mAdapter.curPos==position) {
                        mAdapter.curPos = -1;//取消
                        tvSelect.setText("");
                    }else {
                        mAdapter.curPos = position;
                        tvSelect.setText("已选择1张");
                    }
                    mAdapter.notifyDataSetChanged();
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
                initCoupon(Constants.LOAD_DATA_TYPE_INIT);
            });
    }
    private void initListener() {
        findViewById(R.id.toolbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tvBuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.curPos!=-1)//选中后
                    setResult(100,new Intent().putExtra("data",mAdapter.mDatas.get(mAdapter.curPos)).putExtra("pos",mAdapter.curPos));
                else
                    setResult(100);
                finish();
            }
        });
    }
    private void initData() {
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage=1;
                initCoupon(Constants.LOAD_DATA_TYPE_INIT);
            }
        });

        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (mData.size()>0&&!isRequesting)
                    initCoupon(Constants.LOAD_DATA_TYPE_MORE);

            }
        });
        initCoupon(Constants.LOAD_DATA_TYPE_INIT);

    }
    private void initView() {
        recyclerView= findViewById(R.id.recyclerView);
        tvSelect= findViewById(R.id.tvSelect);
        swipeRefreshLayout= findViewById(R.id.swipeRefreshLayout);
    }
}
