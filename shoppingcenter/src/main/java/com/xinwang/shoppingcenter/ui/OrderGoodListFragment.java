package com.xinwang.shoppingcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.xingwreslib.beautyreslibrary.BeautyObserver;
import com.xingwreslib.beautyreslibrary.OrderInfo;
import com.xingwreslib.beautyreslibrary.OrderLiveData;
import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.base.BaseLazyLoadFragment;
import com.xinwang.bgqbaselib.dialog.CenterDefineDialog;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.CommonEntity;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.HttpUtil;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.loadmore.EndlessRecyclerOnScrollListener;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.adapter.ShoppingGoodOrderListAdapter;
import com.xinwang.shoppingcenter.adapter.ShoppingOrderListAdapter;
import com.xinwang.shoppingcenter.bean.OrderGoodBean;
import com.xinwang.shoppingcenter.bean.OrderListBean;
import com.xinwang.shoppingcenter.interfaces.OrderButtonListener;
import com.xinwang.shoppingcenter.view.WrapContentLinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Date:2021/4/23
 * Time;9:21
 * author:baiguiqiang
 */
public class OrderGoodListFragment extends BaseLazyLoadFragment {
    private  View view;
    private RecyclerView recyclerView;
    private int pageNum =10;
    private int curPage =1;
    private RelativeLayout rl_empty;
    private String pay_state;
    private String q;
    private List<OrderListBean.DataBean.OrdersBean.ItemsBean> mData = new ArrayList<>();
    private ShoppingGoodOrderListAdapter mAdapter;
    public static OrderGoodListFragment getInstance(String q, String pay_state){
        OrderGoodListFragment orderLitFragment =new OrderGoodListFragment();
        Bundle bundle =new Bundle();
        if (q!=null)
            bundle.putString("q",q);
        if (pay_state!=null)
            bundle.putString("pay_state",pay_state);
        orderLitFragment.setArguments(bundle);
        return orderLitFragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_productlist_shoppingcenter,container,false);
        recyclerView =view.findViewById(R.id.recyclerView);
        rl_empty =view.findViewById(R.id.rl_empty);
        rl_empty.setBackgroundResource(R.color.BGClassRoom);
        recyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        return view;
    }
    private void requestFailureShow(String error){
        TextView tvMsg = view.findViewById(R.id.tv_msg);
        tvMsg.setText(error);
        CustomProgressBar progressbar = view.findViewById(R.id.progressbar);
        progressbar .setVisibility(View.GONE);
        if (!error.equals(getString(R.string.no_data_ClassRoom)))
            view.findViewById(R.id.rl_empty).setOnClickListener(v -> {
                progressbar.setVisibility(View.VISIBLE);
                tvMsg.setText("加载中...");
                goRequestData(Constants.LOAD_DATA_TYPE_INIT);
            });
    }


    boolean isRequesting =false;
    private void goRequestData(int loadDataTypeInit){
        goRequestData(new HashMap<>(),loadDataTypeInit);
    }
    private void goRequestData(HashMap<String, Object> stringObjectHashMap,int loadDataTypeInit) {
        if (!pay_state.equals(Constants.PAY_STATE_ALL+"")){
          if (pay_state.equals(Constants.PAY_STATE_YES+"")){//待发货商品
              stringObjectHashMap.put("waybill_state","1");
              stringObjectHashMap.put("pay_state","2");
              stringObjectHashMap.put("refund_state","1");//未退款
              stringObjectHashMap.put("review_state","1");
          }else if (pay_state.equals("3")){//待收货商品
              stringObjectHashMap.put("waybill_state","2");
              stringObjectHashMap.put("pay_state","2");
              stringObjectHashMap.put("refund_state","1");//未退款
              stringObjectHashMap.put("review_state","1");
          }
        }
        stringObjectHashMap.put("page",curPage);
        stringObjectHashMap.put("page_num",pageNum);
        isRequesting =true;
        HttpUtil.cancelTag(this);
        requestGet(HttpUrls.URL_USER_ITEM_LISTS(),stringObjectHashMap, OrderGoodBean.class, new HttpCallBack<OrderGoodBean>() {
            @Override
            public void onFailure(String message) {
                if (loadDataTypeInit!= Constants.LOAD_DATA_TYPE_MORE){
                    stopRefreshAnimation();
                    if (loadDataTypeInit==Constants.LOAD_DATA_TYPE_INIT){
                        requestFailureShow(message);
                    }
                }
                isRequesting =false;
                MyToast.myToast(getContext(),message);
            }

            @Override
            public void onSuccess(OrderGoodBean orderGoodBean) {
               if (loadDataTypeInit!=Constants.LOAD_DATA_TYPE_MORE){
                    stopRefreshAnimation();
                    mData.clear();
                    if (loadDataTypeInit==Constants.LOAD_DATA_TYPE_INIT&&orderGoodBean.getData().getItems().size()==0){
                        requestFailureShow(getString(R.string.no_data_ClassRoom));
                    }else {
                        rl_empty.setVisibility(View.GONE);
                    }
                }

                mData.addAll(orderGoodBean.getData().getItems());
                curPage++;
                if(orderGoodBean.getData().getItems().size()<pageNum){
                    initAdapter(3);
                }else {
                    initAdapter(1);
                }
                isRequesting =false;
            }
        });
    }

    private void stopRefreshAnimation() {
        BeautyDefine.getOpenPageDefine(getActivity()).progressControl(new OpenPageDefine.ProgressController.Hider());
    }

    @Override
    public void initData() {
        pay_state = getArguments().getString("pay_state");
        q =getArguments().getString("q");
        initListener();
        goRequestData(Constants.LOAD_DATA_TYPE_INIT);
        OrderLiveData.getInstance().beautyObserveForever(new BeautyObserver<OrderInfo>() {//收到状态列表刷新
            @Override
            public void beautyOnChanged(@Nullable OrderInfo o) {
                recyclerView.scrollToPosition(0);
                curPage=1;
                goRequestData(Constants.LOAD_DATA_TYPE_REFRESH);
            }
        });

    }
    private void initListener(){
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (mData.size()>0&&!isRequesting)
                    goRequestData(Constants.LOAD_DATA_TYPE_MORE);

            }
        });
    }
    private void initAdapter(int state){
        if (mAdapter==null) {
            mAdapter = new ShoppingGoodOrderListAdapter(mData);

            mAdapter.setOnItemClickListener(new BaseLoadMoreAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    startActivity(new Intent(getActivity(),OrderDetailActivity.class)
                            .putExtra("id",mAdapter.mDatas.get(position).getOrder_id()+"")
                    .putExtra("GoodId",mAdapter.mDatas.get(position).getId()+""));
                }
            });
            mAdapter.setLoadStateNoNotify(state);
            recyclerView.setAdapter(mAdapter);

        }else {
            mAdapter.setLoadStateNoNotify(state);
            mAdapter.notifyDataSetChanged();
        }
    }




}
