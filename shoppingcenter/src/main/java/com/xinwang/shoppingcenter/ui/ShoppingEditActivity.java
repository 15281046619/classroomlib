package com.xinwang.shoppingcenter.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.dialog.CenterDefineDialog;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.sku.bean.Sku;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.MyToast;

import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.adapter.ShoppingEditAdapter;
import com.xinwang.shoppingcenter.bean.CharBodyBean;
import com.xinwang.shoppingcenter.bean.NumberBean;
import com.xinwang.shoppingcenter.interfaces.AdapterItemClickListener;
import com.xinwang.shoppingcenter.view.WrapContentLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品编辑页面
 * Date:2021/3/31
 * Time;12:55
 * author:baiguiqiang
 */
public class ShoppingEditActivity extends BaseNetActivity {
    private RecyclerView recyclerView;

    private TextView tvSum;
    private TextView tvBuy;
    private ImageView ivDelete;
    private List<Sku> mData;
    private ShoppingEditAdapter mAdapter;
    private int aDoublePrice;
    private int selectSum;

    @Override
    protected int layoutResId() {
        return R.layout.activity_shopping_edit_shoppingcenter;
    }
    public static Intent getIntent(Context context, String body, int resultCode){
        Intent intent = new Intent(context, ShoppingEditActivity.class);
        intent.putExtra("body", body);
        intent.putExtra("resultCode",resultCode);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();

    }

    private void initData() {
        mData=GsonUtils.changeGsonToSafeList(getIntent().getStringExtra("body"), Sku.class);
        if (mData.size()==0){
            requestFailureShow("暂未添加商品");
        }else {
            ivDelete.setVisibility(View.VISIBLE);
            findViewById(R.id.rl_empty).setVisibility(View.GONE);
            if (mAdapter==null) {
                mAdapter = new ShoppingEditAdapter(mData);
                mAdapter.setOnItemLongClickListener(new BaseLoadMoreAdapter.OnItemLongClickListener() {//不能用lamdbda表达式，主项目会崩溃
                    @Override
                    public void onItemLongClick(View view, int position) {
                        CenterDefineDialog.getInstance("确认删除"+mAdapter.mDatas.get(position).getGoodTitle()+"?").setCallback(integer -> {
                            if (mAdapter.mDatas.get(position).getSellingPrice()!=0)
                                aDoublePrice =aDoublePrice - mAdapter.mDatas.get(position).getSellingPrice()*mAdapter.mDatas.get(position).getAddSum();
                            selectSum = selectSum - mAdapter.mDatas.get(position).getAddSum();
                            showTotalPrice(aDoublePrice, selectSum);
                            mAdapter.mDatas.remove(position);
                            mAdapter.saveUpdate(ShoppingEditActivity.this);
                            if (mAdapter.mDatas.size()==0){
                                requestFailureShow("暂未添加商品");
                            }
                        }).showDialog(getSupportFragmentManager());
                    }
                });

                mAdapter.setOnClickListener(new AdapterItemClickListener() {
                    @Override
                    public void onClick(int pos, View view) {

                    }

                    @Override
                    public void add(int pos) {

                            if (mAdapter.mDatas.get(pos).getSellingPrice()!=0)
                                aDoublePrice = aDoublePrice +mAdapter.mDatas.get(pos).getSellingPrice();
                            selectSum = selectSum + 1;
                            showTotalPrice(aDoublePrice, selectSum);

                    }

                    @Override
                    public void sub(int pos) {

                            if (mAdapter.mDatas.get(pos).getSellingPrice()!=0)
                                aDoublePrice =aDoublePrice - mAdapter.mDatas.get(pos).getSellingPrice();
                            selectSum = selectSum - 1;
                            showTotalPrice(aDoublePrice, selectSum);

                    }

                    @Override
                    public void setNumber(int pos, int sum, int price) {
                            aDoublePrice = aDoublePrice +price;
                            selectSum = selectSum + sum;
                            showTotalPrice(aDoublePrice, selectSum);

                    }


                });

                recyclerView.setAdapter(mAdapter);
                initPriceAndSum();
            }else {
                mAdapter.mDatas =mData;
                mAdapter.notifyDataSetChanged();
            }

        }
    }


    public void showTotalPrice(int price,int sum){
        this.aDoublePrice =price;
        this.selectSum =sum;
        SpannableString spannableString =new SpannableString("总计:￥"+CountUtil.changeF2Y(price));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeClassRoom)),3,spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.6f),3,4,SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        if (spannableString.toString().indexOf(".")!=-1&&(spannableString.toString().indexOf(".")!=spannableString.length()-1)){
            spannableString.setSpan(new RelativeSizeSpan(0.6f),spannableString.toString().indexOf("."),spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        tvSum.setText(spannableString);
        if (sum>0)
            tvBuy.setText("完成("+sum+")");
        else
            tvBuy.setText("完成");
    }
    public void initPriceAndSum(){
        int price=0;
        int selectSum=0;
        for (int i=0;i<mAdapter.mDatas.size();i++){

            if (mAdapter.mDatas.get(i).getSellingPrice()!=0){
                price=price+mAdapter.mDatas.get(i).getAddSum()*mAdapter.mDatas.get(i).getSellingPrice();
            }
            selectSum+=mAdapter.mDatas.get(i).getAddSum();

        }
        showTotalPrice(price,selectSum);
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

        ivDelete.setOnClickListener(v -> {
            selectSum =0;
            aDoublePrice=0;
            showTotalPrice(aDoublePrice, selectSum);
            mAdapter.mDatas.clear();
            mAdapter.saveUpdate(this);
            requestFailureShow("暂未添加商品");
        });
        tvBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectSum>0){
                    setResult(getIntent().getIntExtra("resultCode",0),new Intent().putExtra("body",getChartBody()));
                    finish();
                }else {
                    MyToast.myToast(ShoppingEditActivity.this,"请选择后提交");
                }
            }
        });
        ((CustomToolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
        findViewById(R.id.ivAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ShoppingEditActivity.this,ShoppingAddActivity.class),100);
            }
        });
    }


    private String getChartBody() {
        ArrayList<Sku> skus =getSelectData();
        int sum=0;
        StringBuffer title=new StringBuffer();
        for (int i=0;i<skus.size();i++){
            sum+=skus.get(i).getAddSum();
            if (i==0){
                title.append(skus.get(i).getGoodTitle());
            }else {
                if (i==1) {
                    if (title.indexOf(skus.get(i).getGoodTitle())==-1) {
                        title.append("、");
                        title.append(skus.get(i).getGoodTitle());
                    }
                }else if (i==2){
                    title.append("...");
                }
            }
        }
        return GsonUtils.createGsonString(new CharBodyBean(title+"共"+sum+"件商品",GsonUtils.createGsonString(skus)));

    }
    private  ArrayList<Sku> getSelectData() {
        return (ArrayList<Sku>) mAdapter.mDatas; }

    private void initView() {
        recyclerView =findViewById(R.id.recyclerView);
        tvSum =findViewById(R.id.tvSum);
        tvBuy = findViewById(R.id.tvBuy);
        ivDelete =findViewById(R.id.ivDelete);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==100&&requestCode==100){
            ArrayList<Sku> mSku = data.getParcelableArrayListExtra("data");
            for (int j=0;j<mSku.size();j++){
                Sku  mBean =mSku.get(j);
                int lookPos= -1;//查找pos
                for (int i=0;i<mAdapter.mDatas.size();i++){
                    if (mAdapter.mDatas.get(i).getId()==null){//sku id为null 没有规格
                        if (mBean.getId()==null&&(mAdapter.mDatas.get(i).getGoodId() == mBean.getGoodId())){
                            lookPos = i;
                            break;
                        }
                    }else {
                        if ((mAdapter.mDatas.get(i).getId().equals(mBean.getId())) && (mAdapter.mDatas.get(i).getGoodId() == mBean.getGoodId())) {
                            lookPos = i;
                            break;
                        }
                    }
                }
                if (lookPos==-1) {
                    if (mBean.getSellingPrice()!=0)
                        aDoublePrice = aDoublePrice +mBean.getSellingPrice()*mBean.getAddSum();
                    selectSum = selectSum + mBean.getAddSum();
                    mAdapter.mDatas.add(0, mBean);
                }else {
                    if(mAdapter.mDatas.get(lookPos).getAddSum()+mBean.getAddSum()>mBean.getStockQuantity()) {
                        return;
                    }else {
                        if (mAdapter.mDatas.get(lookPos).getAddSum() + mBean.getAddSum() >mAdapter.mDatas.get(lookPos).getMaxBugSum()) {

                            return;
                        }else {
                            if (mBean.getSellingPrice()!=0)
                                aDoublePrice = aDoublePrice +mBean.getSellingPrice()*mBean.getAddSum();
                            selectSum = selectSum + mBean.getAddSum();
                            mBean.setAddSum(mBean.getAddSum()+ mAdapter.mDatas.get(lookPos).getAddSum());
                            mAdapter.mDatas.set(lookPos,mBean);
                        }
                    }
                }
            }
            showTotalPrice(aDoublePrice, selectSum);
            mAdapter.notifyDataSetChanged();
            findViewById(R.id.rl_empty).setVisibility(View.GONE);
            ivDelete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
