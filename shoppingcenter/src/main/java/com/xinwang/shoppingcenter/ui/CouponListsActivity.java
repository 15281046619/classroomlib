package com.xinwang.shoppingcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.sku.bean.Sku;
import com.xinwang.bgqbaselib.utils.AsyncTaskUtils;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.TimeUtil;
import com.xinwang.bgqbaselib.utils.asynctask.IDoInBackground;
import com.xinwang.bgqbaselib.utils.asynctask.IPostExecute;
import com.xinwang.bgqbaselib.utils.asynctask.IPublishProgress;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 优惠劵列表
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
    private TextView tvTip;
    private CouponListAdapter mAdapter;
    private boolean isRequesting =false;
    private List<Sku> mSKu=new ArrayList<>();
    private int mPrice;//不包含allow_coupon=0得订单价格
    private boolean isInclude= false;//该订单是否包含不能使用优惠劵的商品
    private AsyncTaskUtils.Builder<List<CouponBean.DataBean.CouponsBean>, Void, List<CouponBean.DataBean.CouponsBean>> mAsyncTask;
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
                        createAsyTask(lodeType,commonEntity);
                    }
                });
    }
    private void createAsyTask(int lodeType,CouponBean commonEntity) {
        mAsyncTask = AsyncTaskUtils.<List<CouponBean.DataBean.CouponsBean>, Void, List<CouponBean.DataBean.CouponsBean>>newBuilder()
                .setDoInBackground(new IDoInBackground<List<CouponBean.DataBean.CouponsBean>, Void, List<CouponBean.DataBean.CouponsBean>>() {
                    @Override
                    public List<CouponBean.DataBean.CouponsBean> doInBackground(IPublishProgress<Void> publishProgress, List<CouponBean.DataBean.CouponsBean>... lists) {
                        if (lists[0]!=null) {

                            List<CouponBean.DataBean.CouponsBean> mList = getNoUseArrayList(lists[0]);
                            Collections.sort(mList, new Comparator<CouponBean.DataBean.CouponsBean>() {
                                @Override
                                public int compare(CouponBean.DataBean.CouponsBean o1, CouponBean.DataBean.CouponsBean o2) {
                                    if (TextUtils.isEmpty(o1.getNoUseCause())){
                                        return -1;
                                    }else if (TextUtils.isEmpty(o2.getNoUseCause())){
                                        return 1;
                                    }else {
                                        return 0;
                                    }
                                }
                            });
                            return mList;
                        }
                        else
                            return null;
                    }
                })
                .setPostExecute(new IPostExecute<List<CouponBean.DataBean.CouponsBean>>() {
                    @Override
                    public void onPostExecute(List<CouponBean.DataBean.CouponsBean> couponsBeans) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (lodeType!=Constants.LOAD_DATA_TYPE_MORE){
                            mData.clear();
                            if (lodeType==Constants.LOAD_DATA_TYPE_INIT&&(couponsBeans==null||commonEntity.getData().getCoupons().size()==0)){
                                requestFailureShow(getString(R.string.no_data_ClassRoom));
                            }else {
                                findViewById(R.id.rl_empty).setVisibility(View.GONE);
                            }
                        }
                        if (couponsBeans!=null) {
                            mData.addAll(couponsBeans);
                            curPage++;
                            if (couponsBeans.size() < pageSum) {
                                initAdapter(3);
                            } else {
                                initAdapter(1);
                            }
                        }
                        isRequesting =false;
                    }
                });
        mAsyncTask.start(commonEntity.getData().getCoupons());
    }

    private void countPrice() {
        mPrice =0;
        List<Sku> mCurSku =new ArrayList<>();
        for (int i=0;i<mSKu.size();i++){

            if (mSKu.get(i).getAllow_coupon()==1){
                mPrice  = mPrice + mSKu.get(i).getAddSum()*mSKu.get(i).getSellingPrice();
                mCurSku.add(mSKu.get(i));
            }else {
                isInclude =true;
            }
        }
        mSKu =mCurSku;
    }

    private List<CouponBean.DataBean.CouponsBean> getNoUseArrayList(List<CouponBean.DataBean.CouponsBean> commonEntity){
        List<CouponBean.DataBean.CouponsBean> mData =new ArrayList<>();
        for (int i=0;i<commonEntity.size();i++){//优惠劵
            String[] str =getCouponPrice(commonEntity.get(i));
            if (mPrice==0) {
                commonEntity.get(i).setNoUseCause("该订单中所有商品不能使用优惠劵");
            }else {
                if (str[0].equals("限定商品使用")){
                    commonEntity.get(i).setNoUseCause("限定商品使用");
                }else {
                    commonEntity.get(i).setNoUseCause(getNoUseCause(commonEntity.get(i),Integer.parseInt(str[1]),str[0]));
                }
            }
            mData.add( commonEntity.get(i));
        }
        return mData;
    }

    private String[] getCouponPrice(CouponBean.DataBean.CouponsBean couponsBean) {
        int mCouponPrice =mPrice;
        String[] mStr =new String[]{};
        if (!TextUtils.isEmpty(couponsBean.getSku_ids())){
            String[] mList = couponsBean.getSku_ids().split(",");
            mCouponPrice =0;
            for (int i=0;i<mSKu.size();i++){
                if (isContain(mList,mSKu.get(i).getId())){
                    mCouponPrice=mCouponPrice +mSKu.get(i).getAddSum()*mSKu.get(i).getSellingPrice();
                }
            }
            if (mCouponPrice ==0){
                mStr[0] ="限定商品使用";
            }else {
                mStr[0]="商品";
            }
        }else {
            if (!TextUtils.isEmpty(couponsBean.getWithout_sku_ids())){
                String[] mList = couponsBean.getWithout_sku_ids().split(",");
                for (int i=0;i<mSKu.size();i++){
                    if (isContain(mList,mSKu.get(i).getId())){
                        mCouponPrice =mCouponPrice - mSKu.get(i).getAddSum()*mSKu.get(i).getSellingPrice();
                    }
                }
                if (mCouponPrice==mPrice){
                    mStr[0]="限定商品使用";
                }else {
                    mStr[0]="商品";
                }
            }else {
                mStr[0] ="";
            }

        }
        mStr[1] =mCouponPrice+"";
        return mStr;
    }


    private String getNoUseCause(CouponBean.DataBean.CouponsBean couponsBean,int couponPrice,String str){
        if (couponPrice>=couponsBean.getMin_money()) {
            long curTime = System.currentTimeMillis()/1000;
            if (couponsBean.getExpire_from()==0||couponsBean.getExpire_from()<=curTime){
                if (couponsBean.getExpire_at()==0||couponsBean.getExpire_at()>=curTime) {
                    if (couponPrice>couponsBean.getFee()) {
                        return "";
                    }else {
                        return "订单不足"+ CountUtil.changeF2Y(couponsBean.getFee());
                    }
                }else {
                    return "截止时间："+TimeUtil.getYMDHMS1(couponsBean.getExpire_at()+"");
                }
            }else {
                return "使用时间："+TimeUtil.getYMDHMS1(couponsBean.getExpire_from()+"");
            }

        }else {
            return "限定"+str+"订单满"+ CountUtil.changeF2Y(couponsBean.getMin_money());
        }
    }

    private void initAdapter(int state ) {
        if (mAdapter==null) {
            if(isInclude) {
                tvTip.setText("温馨提示：该订单中包含了不可使用优惠劵商品");
                tvTip.setVisibility(View.VISIBLE);
            }else {
                tvTip.setVisibility(View.GONE);
            }
            mAdapter = new CouponListAdapter(mData);
            mAdapter.curPos=getIntent().getIntExtra("pos",-1);
            if ( mAdapter.curPos!=-1){
                tvSelect.setText("已选择1张");
            }
            mAdapter.setOnItemClickListener(new BaseLoadMoreAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (TextUtils.isEmpty(mAdapter.mDatas.get(position).getNoUseCause())){
                        clickSuccess(position);
                    }
                }
            });
            mAdapter.setLoadStateNoNotify(state);
            recyclerView.setAdapter(mAdapter);

        }else {
            mAdapter.setLoadStateNoNotify(state);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 改优惠劵是否可在改订单商品中使用

     * @return
     */
    private String isGoodOrSkuId(CouponBean.DataBean.CouponsBean couponsBean){
        String[] goodIds;
        String[] skuIds;
        if (TextUtils.isEmpty(couponsBean.getGoods_ids()))
            goodIds=new String[]{};
        else
            goodIds =couponsBean.getGoods_ids().split(",");
        if (TextUtils.isEmpty(couponsBean.getSku_ids()))
            skuIds =new String[]{};
        else
            skuIds =couponsBean.getSku_ids().split(",");

        if (goodIds.length==0){
            if (skuIds.length==0){
                return "";
            }else {
                for (int i=0;i<mSKu.size();i++){
                    if (isContain(skuIds,mSKu.get(i).getId() + "")){
                        return "";
                    }
                }
                return "不满足限定规格";

            }
        }else {
            for (int i=0;i<mSKu.size();i++){
                if (isContain(goodIds,mSKu.get(i).getGoodId()+"")&&(skuIds.length==0||isContain(skuIds,mSKu.get(i).getId()))){
                    return "";
                }
            }
            return "不满足限定商品";

        }
    }
    private boolean isContain(String[] Ids,String id){
        for (int i=0;i<Ids.length;i++){
            if (Ids[i].equals(id)){
                return true;
            }
        }
        return false;
    }

    private void clickSuccess(int position){
        if (mAdapter.curPos == position) {
            mAdapter.curPos = -1;//取消
            tvSelect.setText("");
        } else {
            mAdapter.curPos = position;
            tvSelect.setText("已选择1张");
        }
        mAdapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAsyncTask != null) {
            mAsyncTask.cancel();
        }
    }

    private void initData() {
        mSKu = getIntent().getParcelableArrayListExtra("data");
        countPrice();
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
        tvTip= findViewById(R.id.tvTip);
        swipeRefreshLayout= findViewById(R.id.swipeRefreshLayout);
    }
}
