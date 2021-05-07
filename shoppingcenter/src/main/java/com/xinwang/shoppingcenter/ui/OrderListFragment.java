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
import com.xinwang.shoppingcenter.adapter.ShoppingOrderListAdapter;
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
public class OrderListFragment extends BaseLazyLoadFragment {
    private  View view;
    private RecyclerView recyclerView;
    private int pageNum =10;
    private int curPage =1;
    private RelativeLayout rl_empty;
    private String pay_state;
    private String q;
    private List<OrderListBean.DataBean.OrdersBean> mData = new ArrayList<>();
    private ShoppingOrderListAdapter mAdapter;
    private BeautyObserver beautyObserver =new BeautyObserver<OrderInfo>() {//收到状态列表刷新
        @Override
        public void beautyOnChanged(@Nullable OrderInfo o) {
            recyclerView.scrollToPosition(0);
            curPage=1;
            goRequestData(Constants.LOAD_DATA_TYPE_INIT);
        }
    };
    public static OrderListFragment getInstance(String q, String pay_state){
        OrderListFragment orderLitFragment =new OrderListFragment();
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
        rl_empty.setVisibility(View.VISIBLE);
        if (!error.equals(getString(R.string.no_data_ClassRoom)))
            rl_empty.setOnClickListener(v -> {
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
            stringObjectHashMap.put("pay_state",pay_state);
            if (pay_state.equals(Constants.PAY_STATE_NO+"")){//待支付加入
                stringObjectHashMap.put("cancel_state","1");
            }
        }
        stringObjectHashMap.put("page",curPage);
        stringObjectHashMap.put("page_num",pageNum);
        isRequesting =true;
        HttpUtil.cancelTag(this);
        requestGet(HttpUrls.URL_USER_ORDER_LISTS(),stringObjectHashMap, OrderListBean.class, new HttpCallBack<OrderListBean>() {
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
            public void onSuccess(OrderListBean orderListBean) {
                if (loadDataTypeInit!=Constants.LOAD_DATA_TYPE_MORE){ stopRefreshAnimation();
                    mData.clear();
                    if (loadDataTypeInit==Constants.LOAD_DATA_TYPE_INIT&&orderListBean.getData().getOrders().size()==0){
                        requestFailureShow(getString(R.string.no_data_ClassRoom));
                    }else {
                        rl_empty.setVisibility(View.GONE);
                    }
                }

                mData.addAll(orderListBean.getData().getOrders());
                curPage++;
                if(orderListBean.getData().getOrders().size()<pageNum){
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
        OrderLiveData.getInstance().beautyObserveNonStickyForever(beautyObserver);

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
            mAdapter = new ShoppingOrderListAdapter(mData);
            mAdapter.setOnClickButtonListener(new OrderButtonListener() {
                @Override
                public void onCancel(int pos) {
                    goCancelOrder(pos);
                }

                @Override
                public void onPay(String title,int pos) {
                      BeautyDefine.getCashierDeskDefine((AppCompatActivity) getActivity()).
                              jumpCashierPage(mAdapter.mDatas.get(pos).getPrice()>0?
                                              CountUtil.changeF2Y(mAdapter.mDatas.get(pos).getPrice()):"0",
                            title,mAdapter.mDatas.get(pos).getId()+"",null);
                }
            });
            mAdapter.setOnItemClickListener(new BaseLoadMoreAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    startActivity(new Intent(getActivity(),OrderDetailActivity.class).putExtra("id",mAdapter.mDatas.get(position).getId()+""));
                }
            });
            mAdapter.setLoadStateNoNotify(state);
            recyclerView.setAdapter(mAdapter);

        }else {
            mAdapter.setLoadStateNoNotify(state);
            mAdapter.notifyDataSetChanged();
        }
    }


    private void goCancelOrder(int pos){
        CenterDefineDialog.getInstance("确认取消该订单嘛？").setCallback(new CenterDefineDialog.Callback1<Integer>() {
            @Override
            public void run(Integer integer) {
                requestCancelOrder(pos);
            }
        }).showDialog(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
    }
    private void requestCancelOrder(int pos){
        BeautyDefine.getOpenPageDefine(getActivity()).progressControl(new OpenPageDefine.ProgressController.Showder("加载中",false));
        requestPost(HttpUrls.URL_ORDER_CANCEL(), new ApiParams().with("order_id", mAdapter.mDatas.get(pos).getId()+""), CommonEntity.class, new HttpCallBack<CommonEntity>() {
            @Override
            public void onFailure(String message) {
                MyToast.myToast(getActivity(),message);
                BeautyDefine.getOpenPageDefine(getActivity()).progressControl(new OpenPageDefine.ProgressController.Hider());
            }

            @Override
            public void onSuccess(CommonEntity commonEntity) {
                int orderId = mAdapter.mDatas.get(pos).getId();
                MyToast.myToast(getActivity(),"取消成功");
                if (pay_state.equals(Constants.PAY_STATE_ALL+"")){//全部
                    OrderListBean.DataBean.OrdersBean mBean = mAdapter.mDatas.get(pos);
                    mBean.setCancel_state(2);
                    mAdapter.mDatas.set(pos,mBean);
                    mAdapter.notifyDataSetChanged();
                }else if (pay_state.equals(Constants.PAY_STATE_NO+"")) {

                    mAdapter.mDatas.remove(pos);
                    mAdapter.notifyDataSetChanged();
                }

                OrderLiveData.getInstance().notifyInfoChanged(new OrderInfo(orderId, Constants.PAY_STATE_CANCEL));//广播
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OrderLiveData.getInstance().beautyObserveNonStickyForeverRemove(beautyObserver);
    }
}
