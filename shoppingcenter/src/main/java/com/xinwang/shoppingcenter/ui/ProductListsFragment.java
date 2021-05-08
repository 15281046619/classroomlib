package com.xinwang.shoppingcenter.ui;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.base.BaseLazyLoadFragment;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.sku.bean.Sku;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.HttpUtil;
import com.xinwang.bgqbaselib.utils.LogUtil;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.StaggeredDividerItemDecoration;
import com.xinwang.bgqbaselib.view.loadmore.EndlessRecyclerOnScrollListener;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.adapter.ShoppingHomeAdapter;
import com.xinwang.shoppingcenter.bean.FragmentUpdateBean;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.bean.SkuBean;
import com.xinwang.shoppingcenter.dialog.BottomSkuDialog;
import com.xinwang.shoppingcenter.interfaces.ActivitySendFragmentListener;
import com.xinwang.shoppingcenter.interfaces.AdapterItemClickListener;
import com.xinwang.shoppingcenter.view.WrapContentStaggeredGridLayoutManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 公共产品
 * Date:2021/3/22
 * Time;14:25
 * author:baiguiqiang
 */
public class ProductListsFragment extends BaseLazyLoadFragment implements ActivitySendFragmentListener {
    private ShoppingHomeAdapter mAdapter;
    private int pageNum =10;
    private int curPage =1;
    private RelativeLayout rl_empty;
    private String q;
    private String category_id;
    private List<GoodsBean.DataBean> mData = new ArrayList<>();
    private RecyclerView recyclerView;
    private  View view;
    /**
     * 获得单列
     * @param q  不用搜索 为null
     * @param category_id  不用搜索 为null
     * @return
     */
    public static ProductListsFragment getInstance(String q,String category_id){
        ProductListsFragment productListsFragment =new ProductListsFragment();
        Bundle bundle =new Bundle();
        if (q!=null)
            bundle.putString("q",q);
        if (category_id!=null)
            bundle.putString("category_id",category_id);
        productListsFragment.setArguments(bundle);
        return productListsFragment;
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_productlist_shoppingcenter,container,false);
        recyclerView =view.findViewById(R.id.recyclerView);
        rl_empty =view.findViewById(R.id.rl_empty);
        rl_empty.setBackgroundResource(R.color.BGClassRoom);
        //垂直方向的2列
        WrapContentStaggeredGridLayoutManager layoutManager = new WrapContentStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //防止Item切换
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new StaggeredDividerItemDecoration(getContext(), 10,2));

        return view;
    }
    private void requestFailureShow(String error){

        TextView tvMsg = view.findViewById(R.id.tv_msg);
        tvMsg.setText(error);
        CustomProgressBar progressbar = view.findViewById(R.id.progressbar);
        progressbar .setVisibility(View.GONE);
        if (!error.equals(getString(R.string.no_data_ClassRoom)))
            rl_empty.setOnClickListener(v -> {
                progressbar.setVisibility(View.VISIBLE);
                tvMsg.setText("加载中...");
                goRequestData(Constants.LOAD_DATA_TYPE_INIT);
            });
    }
    @Override
    public void initData() {
        category_id = getArguments().getString("category_id");
        q =getArguments().getString("q");
        initListener();
        goRequestData(Constants.LOAD_DATA_TYPE_INIT);


    }

    private void initListener() {
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (mData.size()>0&&!isRequesting)
                    goRequestData(Constants.LOAD_DATA_TYPE_MORE);

            }
        });

    }

    boolean isRequesting =false;
    private void goRequestData(int loadDataTypeInit){
        goRequestData(new HashMap<>(),loadDataTypeInit);
    }
    private void goRequestData(HashMap<String, Object> stringObjectHashMap,int loadDataTypeInit) {

        if (q!=null)
            stringObjectHashMap.put("q",q);
        if (category_id!=null)
            stringObjectHashMap.put("category_id",category_id);
        stringObjectHashMap.put("page",curPage);
        stringObjectHashMap.put("page_num",pageNum);
        isRequesting =true;
        HttpUtil.cancelTag(this);
        requestGet(HttpUrls.URL_GOODS_HOME_LISTS(),stringObjectHashMap, GoodsBean.class, new HttpCallBack<GoodsBean>() {
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
            public void onSuccess(GoodsBean goodsBean) {
                if (loadDataTypeInit!=Constants.LOAD_DATA_TYPE_MORE){
                    stopRefreshAnimation();
                    mData.clear();
                    if (loadDataTypeInit==Constants.LOAD_DATA_TYPE_INIT&&goodsBean.getData().size()==0){
                        requestFailureShow(getString(R.string.no_data_ClassRoom));
                    }else {
                        rl_empty.setVisibility(View.GONE);
                    }
                }

                mData.addAll(goodsBean.getData());
                curPage++;
                if(goodsBean.getData().size()<pageNum){
                    initAdapter(3,loadDataTypeInit);
                }else {
                    initAdapter(1,loadDataTypeInit);
                }
                isRequesting =false;
            }
        });
    }

    private void stopRefreshAnimation() {
        EventBus.getDefault().post(new FragmentUpdateBean());//通知刷新结束 一般是停止activity里面刷新控件
    }

    private void initAdapter(int state,int loadDataTypeInit){
        if (mAdapter==null||loadDataTypeInit!=Constants.LOAD_DATA_TYPE_MORE) {
            if (loadDataTypeInit==Constants.LOAD_DATA_TYPE_REFRESH&&mAdapter!=null){
                recyclerView.scrollToPosition(0);//滑动到头部刷新防止出现空白
                mAdapter.setLoadStateNoNotify(state);
                mAdapter.notifyDataSetChanged();
            }else {
                /**
                 * item直接10dp 两边边距30dp 加上四边阴影 2dp
                 */
                mAdapter = new ShoppingHomeAdapter(mData, (CommentUtils.getScreenWidth(getActivity()) - CommentUtils.dip2px(getContext(), 48)) / 2);
                mAdapter.setOnClickListener(new AdapterItemClickListener() {
                    @Override
                    public void onClick(int pos, View view) {

                    }

                    @Override
                    public void add(int pos) {
                        // ShoppingCenterLibUtils.addShoppingCenter(getActivity(),mData.get(pos));
                        requestSkuData(pos);
                    }

                    @Override
                    public void sub(int pos) {

                    }
                });
                mAdapter.setOnItemClickListener((view, position) -> jumpDetailActivity(mData.get(position),view));
                mAdapter.setLoadStateNoNotify(state);
                mAdapter.setStaggeredGridLayoutManager(true);
                recyclerView.setAdapter(mAdapter);
            }
        }else {
            mAdapter.setLoadStateNoNotify(state);
            mAdapter.notifyItemInserted(mAdapter.getItemCount()-1);
        }
    }

    /**
     * 获取sku
     * @param pos
     */
    private void requestSkuData(int pos) {
        //  BeautyDefine.getOpenPageDefine(getActivity()).progressControl(new OpenPageDefine.ProgressController.Showder("加载中",false));
        requestGet(HttpUrls.URL_GOODS_HOME_SKU_LISTS(),new ApiParams().with("goods_id",mData.get(pos).getId()), SkuBean.class, new HttpCallBack<SkuBean>(){

            @Override
            public void onFailure(String message) {
                MyToast.myToast(getActivity(),message);
                //BeautyDefine.getOpenPageDefine(getActivity()).progressControl(new OpenPageDefine.ProgressController.Hider());
            }

            @Override
            public void onSuccess(SkuBean skuBean) {

                // BeautyDefine.getOpenPageDefine(getActivity()).progressControl(new OpenPageDefine.ProgressController.Hider());
                showSkuDialog(ShoppingCenterLibUtils.skuToBean(skuBean.getData(),mData.get(pos)));
            }
        });
    }
    private void showSkuDialog(List<Sku> skuBean){
        if (skuBean!=null&&skuBean.size()>0&&skuBean.get(0).getId()!=null)
            BottomSkuDialog.getInstance(skuBean,0).showDialog(getActivity().getSupportFragmentManager());
        else {
            if (skuBean.size()>0)
            ShoppingCenterLibUtils.addShoppingCenter(getActivity(), skuBean.get(0));
        }
    }
    private void jumpDetailActivity(GoodsBean.DataBean mGoodsBean,View view){
        Intent intent = new Intent(getActivity(), ShoppingDetailActivity.class);
        if (mGoodsBean!=null){
            intent.putExtra("id",mGoodsBean.getId());
        }
    /*    if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            ActivityCompat.startActivity(getActivity(),intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "goodsIcon").toBundle());
        } else {*/
        startActivity(intent);
        /*}*/
    }

    @Override
    public void onActivitySendFragment(Object object) {
        curPage=1;
        if (object instanceof HashMap) {
            HashMap<String,Object> map = (HashMap<String, Object>) object;

            goRequestData(map,Constants.LOAD_DATA_TYPE_REFRESH);
        }else {
            goRequestData(new HashMap(),Constants.LOAD_DATA_TYPE_REFRESH);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    //滑动到顶部
    public void scrollTop(){
        recyclerView.scrollToPosition(0);
    }

}
