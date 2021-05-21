package com.xingwang.classroom.ui.live;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.GoodListAdapter;
import com.xingwreslib.beautyreslibrary.BeautyObserver;
import com.xingwreslib.beautyreslibrary.OrderInfo;
import com.xingwreslib.beautyreslibrary.OrderLiveData;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.sku.bean.Sku;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.VpSwipeRefreshLayout;
import com.xingwang.classroom.view.WrapContentLinearLayoutManager;
import com.xinwang.bgqbaselib.base.BaseLazyLoadFragment;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.bean.SkuBean;
import com.xinwang.shoppingcenter.dialog.BottomSkuDialog;
import com.xinwang.shoppingcenter.interfaces.OnClickOkListener;
import com.xinwang.shoppingcenter.ui.ShoppingOrderActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2020/8/13
 * Time;15:24
 * author:baiguiqiang
 */
public class LiveGoodListFragment extends BaseLazyLoadFragment {

    protected RecyclerView recycler_view;
    protected GoodListAdapter goodListAdapter;
    private VpSwipeRefreshLayout swipeRefreshLayout;

    public static LiveGoodListFragment getInstance(String des){
        LiveGoodListFragment mFragment = new LiveGoodListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA,des);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live_good_list_classroom,container,false);

        recycler_view=view.findViewById(R.id.recycler_view);
        swipeRefreshLayout =view.findViewById(R.id.swipeRefreshLayout);

        return view;
    }

    @Override
    public void initData() {

        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setRefreshing(true);

        recycler_view.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        goodListAdapter=new GoodListAdapter(getActivity());
        recycler_view.setAdapter(goodListAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> getRequestData());

        goodListAdapter.setOnChildItemClickListener(goodBean ->requestSkuData(goodBean) );

        getRequestData();
    }

    private void getRequestData(){
        requestGet(HttpUrls.URL_GOOD2_LISTS(),new ApiParams().with("live_id",getArguments().getString(Constants.DATA)), GoodsBean.class, new HttpCallBack<GoodsBean>() {

            @Override
            public void onFailure(String message) {
                swipeRefreshLayout.setRefreshing(false);
                if (getActivity()!=null)
                    MyToast.myToast(getActivity().getApplicationContext(),message);
            }

            @Override
            public void onSuccess(GoodsBean goodListBean) {
                swipeRefreshLayout.setRefreshing(false);
                if (goodListBean.getData().size()>0)
                    goodListAdapter.setDatas(goodListBean.getData());

            }
        });
    }
    private void requestSkuData(GoodsBean.DataBean dataBean) {
        //  BeautyDefine.getOpenPageDefine(getActivity()).progressControl(new OpenPageDefine.ProgressController.Showder("加载中",false));
        requestGet(HttpUrls.URL_GOODS_HOME_SKU_LISTS(),new ApiParams().with("goods_id",dataBean.getId()+""), SkuBean.class, new HttpCallBack<SkuBean>(){

            @Override
            public void onFailure(String message) {
                MyToast.myToast(getActivity(),message);
                //BeautyDefine.getOpenPageDefine(getActivity()).progressControl(new OpenPageDefine.ProgressController.Hider());
            }

            @Override
            public void onSuccess(SkuBean skuBean) {


                showSkuDialog(ShoppingCenterLibUtils.skuToBean(skuBean.getData(),dataBean));
            }
        });
    }
    private void showSkuDialog(List<Sku> skuBean){
        if (skuBean.size()>0&&skuBean.get(0).getId()!=null)
            BottomSkuDialog.getInstance(skuBean,1).setOnClickOkListener(new OnClickOkListener() {
                @Override
                public void onClickOk(Sku sku) {
                    ArrayList<Sku> skus =new ArrayList<>();
                    skus.add(sku);
                    startActivity(new Intent(getActivity(), ShoppingOrderActivity.class).putParcelableArrayListExtra("data",  skus).putExtra("isLive",true));
                }
            }).showDialog(getActivity().getSupportFragmentManager());
        else {
            MyToast.myToast(getContext(),"商品不可购买");
        }
    }


}
