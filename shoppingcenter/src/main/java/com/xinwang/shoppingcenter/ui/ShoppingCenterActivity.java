package com.xinwang.shoppingcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.adapter.ShoppingCenterAdapter;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.interfaces.AdapterItemClickListener;
import com.xinwang.shoppingcenter.view.WrapContentLinearLayoutManager;

import java.util.List;

/**
 * Date:2021/3/31
 * Time;12:55
 * author:baiguiqiang
 */
public class ShoppingCenterActivity extends BaseNetActivity {
    private RecyclerView recyclerView;
    private CheckBox rbCheck;
    private ImageView ivDelete;
    private List<GoodsBean.DataBean> mData;
    private ShoppingCenterAdapter mAdapter;
    @Override
    protected int layoutResId() {
        return R.layout.activity_shopping_shoppingcenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {


        mData=GsonUtils.changeGsonToSafeList( SharedPreferenceUntils.getGoods(this), GoodsBean.DataBean.class);
        if (mData.size()==0){
            requestFailureShow("暂未添加商品");
        }else {
            ivDelete.setVisibility(View.VISIBLE);
            findViewById(R.id.rl_empty).setVisibility(View.GONE);
            if (mAdapter==null) {
                mAdapter = new ShoppingCenterAdapter(mData);
                mAdapter.setOnItemClickListener((view, position) -> startActivity(new Intent(ShoppingCenterActivity.this, ShoppingDetailActivity.class).putExtra("id", mData.get(position).getId())));
                mAdapter.setOnItemLongClickListener(new BaseLoadMoreAdapter.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(View view, int position) {
                        mAdapter.mDatas.remove(position);
                        mAdapter.saveUpdate(ShoppingCenterActivity.this);
                        if (mAdapter.mDatas.size()==0){
                            requestFailureShow("暂未添加商品");
                        }
                    }
                });
                rbCheck.setChecked(mAdapter.isAllCheck());
                mAdapter.setOnClickListener(new AdapterItemClickListener() {
                    @Override
                    public void onClick(int pos, View view) {
                        rbCheck.setChecked(mAdapter.isAllCheck());
                    }

                    @Override
                    public void add(int pos) {

                    }
                });
                recyclerView.setAdapter(mAdapter);
            }else {
                mAdapter.mDatas =mData;
                mAdapter.notifyDataSetChanged();
            }
        }
    }



    private void requestFailureShow(String error){

        TextView tvMsg = findViewById(R.id.tv_msg);
        tvMsg.setText(error);
        CustomProgressBar progressbar = findViewById(R.id.progressbar);
        progressbar .setVisibility(View.GONE);
        findViewById(R.id.rl_empty).setVisibility(View.VISIBLE);
        ivDelete.setVisibility(View.GONE);
    }
    private void initListener() {
        rbCheck.setOnClickListener(v -> {
            mAdapter.allCheck(this,rbCheck.isChecked());
        });
        ivDelete.setOnClickListener(v -> {
            mAdapter.mDatas.clear();
            mAdapter.saveUpdate(this);
            requestFailureShow("暂未添加商品");
        });
        findViewById(R.id.tvBuy).setOnClickListener(v -> CommentUtils.jumpWebBrowser(ShoppingCenterActivity.this, HttpUrls.URL_CHAT));
        ((CustomToolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());

    }

    private void initView() {
        recyclerView =findViewById(R.id.recyclerView);
        rbCheck =findViewById(R.id.rbCheck);
        ivDelete =findViewById(R.id.ivDelete);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
    }
}
