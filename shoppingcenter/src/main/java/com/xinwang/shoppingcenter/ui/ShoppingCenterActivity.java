package com.xinwang.shoppingcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.dialog.CenterDefineDialog;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.sku.bean.Sku;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.adapter.ShoppingCenterAdapter;
import com.xinwang.shoppingcenter.interfaces.AdapterItemClickListener;
import com.xinwang.shoppingcenter.view.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/3/31
 * Time;12:55
 * author:baiguiqiang
 */
public class ShoppingCenterActivity extends BaseNetActivity {
    private RecyclerView recyclerView;
    private CheckBox rbCheck;
    private TextView tvSum,tvBuy;
    private ImageView ivDelete;
    private List<Sku> mData;
    private ShoppingCenterAdapter mAdapter;
    private Double aDoublePrice;
    private int selectSum;
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


        mData=GsonUtils.changeGsonToSafeList( SharedPreferenceUntils.getGoods(this), Sku.class);
        if (mData.size()==0){
            requestFailureShow("暂未添加商品");
        }else {
            ivDelete.setVisibility(View.VISIBLE);
            findViewById(R.id.rl_empty).setVisibility(View.GONE);
            if (mAdapter==null) {
                mAdapter = new ShoppingCenterAdapter(mData);
                mAdapter.setOnItemClickListener((view, position) -> startActivity(new Intent(ShoppingCenterActivity.this, ShoppingDetailActivity.class).putExtra("id", mData.get(position).getGoodId())));
                mAdapter.setOnItemLongClickListener(new BaseLoadMoreAdapter.OnItemLongClickListener() {//不能用lamdbda表达式，主项目会崩溃
                    @Override
                    public void onItemLongClick(View view, int position) {
                        CenterDefineDialog.getInstance("确认删除"+mAdapter.mDatas.get(position).getGoodTitle()+"?").setCallback(integer -> {
                            mAdapter.mDatas.remove(position);
                            rbCheck.setChecked(isAllCheck());
                            mAdapter.saveUpdate(ShoppingCenterActivity.this);
                            if (mAdapter.mDatas.size()==0){
                                requestFailureShow("暂未添加商品");
                            }
                        }).showDialog(getSupportFragmentManager());

                    }
                });
                rbCheck.setChecked(isAllCheck());
                mAdapter.setOnClickListener(new AdapterItemClickListener() {
                    @Override
                    public void onClick(int pos, View view) {
                        rbCheck.setChecked(isAllCheck());
                    }

                    @Override
                    public void add(int pos) {
                        if (mAdapter.mDatas.get(pos).isCheck()) {
                            if (!TextUtils.isEmpty(mAdapter.mDatas.get(pos).getSellingPrice()))
                                aDoublePrice = aDoublePrice + Double.parseDouble(mAdapter.mDatas.get(pos).getSellingPrice());
                            selectSum = selectSum + 1;
                            showTotalPrice(aDoublePrice, selectSum);
                        }
                    }

                    @Override
                    public void sub(int pos) {
                        if (mAdapter.mDatas.get(pos).isCheck()) {
                            if (!TextUtils.isEmpty(mAdapter.mDatas.get(pos).getSellingPrice()))
                                aDoublePrice = aDoublePrice - Double.parseDouble(mAdapter.mDatas.get(pos).getSellingPrice());
                            selectSum = selectSum - 1;
                            showTotalPrice(aDoublePrice, selectSum);
                        }
                    }
                });
                recyclerView.setAdapter(mAdapter);
            }else {
                mAdapter.mDatas =mData;
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /*
     *//**
     * 全选获取取消
     */
    public void allCheck(boolean isCheck){
        Double price=0D;
        int selectSum=0;
        for (int i=0;i<mAdapter.mDatas.size();i++){
            mAdapter.mDatas.get(i).setCheck(isCheck);
            if (isCheck) {
                if (!TextUtils.isEmpty(mAdapter.mDatas.get(i).getSellingPrice()))
                price = price + Double.parseDouble(mAdapter.mDatas.get(i).getSellingPrice())*mAdapter.mDatas.get(i).getAddSum();
                selectSum+=mAdapter.mDatas.get(i).getAddSum();
            }
        }
        showTotalPrice(price,selectSum);
        mAdapter.saveUpdate(this);
    }
    public void showTotalPrice(Double price,int sum){
        this.aDoublePrice =price;
        this.selectSum =sum;
        SpannableString spannableString =new SpannableString("总计:￥"+price);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeClassRoom)),3,spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.6f),3,4,SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        tvSum.setText(spannableString);
        if (sum>0)
            tvBuy.setText("结算("+sum+")");
        else
            tvBuy.setText("结算");
    }

    /**
     * 是不是全选
     * @return
     */
    public boolean isAllCheck(){
        List<Sku> selectSku =new ArrayList<>();
        Double price=0D;
        int selectSum=0;
        for (int i=0;i<mAdapter.mDatas.size();i++){
            if (mAdapter.mDatas.get(i).isCheck()){
                selectSku.add(mAdapter.mDatas.get(i));
                if (!TextUtils.isEmpty(mAdapter.mDatas.get(i).getSellingPrice())){
                    price=price+ Double.parseDouble(mAdapter.mDatas.get(i).getSellingPrice())*mAdapter.mDatas.get(i).getAddSum();
                }
                selectSum+=mAdapter.mDatas.get(i).getAddSum();
            }
        }
        showTotalPrice(price,selectSum);
        return mAdapter.mDatas.size()==selectSku.size();
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
            allCheck(rbCheck.isChecked());
        });
        ivDelete.setOnClickListener(v -> {
            CenterDefineDialog.getInstance("确认清空购物车?").setCallback(integer -> {
                mAdapter.mDatas.clear();
                mAdapter.saveUpdate(this);
                requestFailureShow("暂未添加商品");
            }).showDialog(getSupportFragmentManager());

        });
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectSum>0){
                    ArrayList<Sku> mSelectData = getSelectData();
                    startActivity(new Intent(ShoppingCenterActivity.this,ShoppingOrderActivity.class).putParcelableArrayListExtra("data",mSelectData));
                }
            }
        });
        ((CustomToolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());

    }

    private ArrayList<Sku> getSelectData() {
        ArrayList<Sku> skus =new ArrayList<>();
        for (Sku sku :mAdapter.mDatas){
            if (sku.isCheck())
            skus.add(sku);
        }
        return skus;
    }

    private void initView() {
        recyclerView =findViewById(R.id.recyclerView);
        rbCheck =findViewById(R.id.rbCheck);
        tvSum =findViewById(R.id.tvSum);
        tvBuy = findViewById(R.id.tvBuy);
        ivDelete =findViewById(R.id.ivDelete);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
    }
}
