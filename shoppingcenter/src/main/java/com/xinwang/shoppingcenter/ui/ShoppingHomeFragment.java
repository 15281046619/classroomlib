package com.xinwang.shoppingcenter.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.FlowLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.xingwreslib.beautyreslibrary.BeautyObserver;
import com.xingwreslib.beautyreslibrary.OrderInfo;
import com.xingwreslib.beautyreslibrary.OrderLiveData;
import com.xinwang.bgqbaselib.base.BaseLazyLoadFragment;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.shoppingcenter.bean.Sku;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.FragmentUtils;
import com.xinwang.bgqbaselib.utils.GlideImageLoader;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.bgqbaselib.view.VpSwipeRefreshLayout;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.ADGroupBean;
import com.xinwang.shoppingcenter.bean.FragmentUpdateBean;
import com.xinwang.shoppingcenter.bean.NumberBean;
import com.xinwang.shoppingcenter.bean.OrderListBean;
import com.xinwang.shoppingcenter.interfaces.FragmentStateListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * Date:2021/3/15
 * Time;13:17
 * author:baiguiqiang
 */
public class ShoppingHomeFragment  extends BaseLazyLoadFragment {

    private VpSwipeRefreshLayout swipeRefreshLayout;

    private RelativeLayout rlSearch;
    private AppBarLayout appBar;
    private Banner banner;
    private FlowLayout flowLayout;
    private TextView tvAllShopping,tvNumber,icOrderNumber;
    private LinearLayout llRoot;
    private RelativeLayout rlHot;
    private CustomToolbar toolbar;
    private ImageView ivShoppingCenter,icOrderList;
    private List<ADGroupBean.DataBean> mTitleImages =new ArrayList<>();
    private ProductListsFragment mFragment;
    private int number =0;
    private int orderNoPayNumber =0;
    private FragmentStateListener fragmentStateListener;
    private BeautyObserver beautyObserver= new BeautyObserver<OrderInfo>() {
        @Override
        public void beautyOnChanged(@Nullable OrderInfo o) {
            if (o.getPayState()==Constants.PAY_STATE_NO){//下单成功移除了选中的商品 未支付
                orderNoPayNumber +=1;
            }else {//已支付
                orderNoPayNumber -=1;
            }
            showOrderNumber();
        }
    };



    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_shopping_home_shoppingcenter,container,false);
        EventBus.getDefault().register(this);
        swipeRefreshLayout =view.findViewById(R.id.swipeRefreshLayout);

        rlHot =view.findViewById(R.id.rlHot);
        tvNumber =view.findViewById(R.id.tvNumber);
        icOrderNumber =view.findViewById(R.id.icOrderNumber);
        appBar =view.findViewById(R.id.app_bar_layout);
        llRoot =view.findViewById(R.id.llRoot);
        rlSearch =view.findViewById(R.id.rlSearch);
        flowLayout =view.findViewById(R.id.flowLayout);
        toolbar =view.findViewById(R.id.toolbar);
        banner =view.findViewById(R.id.banner);
        tvAllShopping =view.findViewById(R.id.tvAllShopping);
        ivShoppingCenter =view.findViewById(R.id.ivShoppingCenter);
        icOrderList =view.findViewById(R.id.icOrderList);
        initSettingAppBarListener();

        return view;
    }

    private void initSettingAppBarListener() {
        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            int offset = Math.abs(verticalOffset); //目的是将负数转换为绝对正数；
            swipeRefreshLayout.setEnabled(offset == 0);
        });
    }
    @Subscribe()
    public void updateDataSuccess(FragmentUpdateBean fragmentUpdateBean){
        if (swipeRefreshLayout!=null&&swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    @Subscribe()
    public void updateNumber(NumberBean numberBean){
        number =number+numberBean.getSum();
        showNumber();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        OrderLiveData.getInstance().beautyObserveNonStickyForeverRemove(beautyObserver);
    }

    @Override
    public void initData() {
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);

        swipeRefreshLayout.setRefreshing(true);
        initToolbar();
        initBannerHeight();
        initListener();
        requestAd();
        initSearchShow();
        initNumber();
        initOrderNumber();
    }



    private void initNumber() {
        number = GsonUtils.changeGsonToSafeList( SharedPreferenceUntils.getGoods(getActivity()), Sku.class).size();
        showNumber();
    }

    private void showNumber(){
        if (number<=99) {
            if (number<0){
                tvNumber.setText("0");
            }else {
                tvNumber.setText(number+"");
            }
        }else {
            tvNumber.setText("99");
        }
    }
    private void initToolbar() {

        if (getActivity()!=null&&!(getActivity() instanceof ShoppingHomeActivity)){
            toolbar.getNavigationIcon().mutate().setAlpha(0);//必须加mutate，不然修改后全局toolbar都将更改
        }else
            toolbar.setNavigationOnClickListener(v -> getActivity().finish());
    }

    /**
     * 订单数目
     */
    private void initOrderNumber() {
        OrderLiveData.getInstance().beautyObserveNonStickyForever(beautyObserver);
        requestGet(HttpUrls.URL_USER_ORDER_LISTS(), new ApiParams().with("pay_state", Constants.PAY_STATE_NO).with("cancel_state","1").with("page", 1).with("page_num", 1), OrderListBean.class, new HttpCallBack<OrderListBean>() {
            @Override
            public void onFailure(String message) {
                MyToast.myToast(getActivity(),message);
            }

            @Override
            public void onSuccess(OrderListBean orderListBean) {
                orderNoPayNumber = orderListBean.getData().getTotal();
                showOrderNumber();
            }
        });
    }

    private void showOrderNumber(){
        if (orderNoPayNumber<=99) {
            if (orderNoPayNumber<=0){
                icOrderNumber.setText("0");
                icOrderNumber.setVisibility(View.GONE);
            }else {
                icOrderNumber.setVisibility(View.VISIBLE);
                icOrderNumber.setText(orderNoPayNumber+"");
            }
        }else {
            icOrderNumber.setVisibility(View.VISIBLE);
            icOrderNumber.setText("99");
        }
    }
    private void requestAd() {
        requestGet(HttpUrls.URL_AD_LISTS(),new ApiParams().with("group", "商品推荐"), ADGroupBean.class, new HttpCallBack<ADGroupBean>(){

            @Override
            public void onFailure(String message) {
                MyToast.myToast(getActivity(),message);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(ADGroupBean adGroupBean) {
                showTitleImages(adGroupBean.getData());

                if (mFragment==null) {
                    mFragment = ProductListsFragment.getInstance(null, null,true);
                    mFragment.setFragmentStateListener(new FragmentStateListener() {
                        @Override
                        public void fragmentInitViewSuccess(AppBarLayout appBarLayout, RecyclerView recyclerView) {
                            if (fragmentStateListener!=null){
                                fragmentStateListener.fragmentInitViewSuccess(appBar,recyclerView);
                            }
                        }
                    });
                    FragmentUtils.showChildFragment(ShoppingHomeFragment.this, R.id.flameLayout, mFragment);
                } else
                    mFragment.onActivitySendFragment(new HashMap<String,Object>());

            }
        } );
    }

    private void initSearchShow(){
        List<String> mList = new ArrayList(Arrays.asList(ShoppingCenterLibUtils.getHotSearch(getContext())));
        mList.add(0,"热门搜索:");
        flowLayout.removeAllViews();
        flowLayout.setSingleLine(true);
        for (int i = 0; i<(Math.min(mList.size(), 6)); i++){
            TextView tv = new TextView(getActivity());
            tv.setTextColor(ContextCompat.getColor(getContext(),android.R.color.white));
            tv.setText(mList.get(i));
            int finalI = i;
            tv.setOnClickListener(v -> {
                if (finalI !=0){
                    jumpSearchActivity(mList.get(finalI));
                }
            });
            flowLayout.addView(tv);
        }
        /**
         * 为了适配不同手机宽度显示不同flowlayout view数目
         */
        int flowLayoutWidth = CommentUtils.getScreenWidth(Objects.requireNonNull(getActivity()))-2*CommentUtils.dip2px(Objects.requireNonNull(getContext()),15);
        flowLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                flowLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int totalViewWidth=0;
                List<View> mViews= new ArrayList<>();
                for (int i=0;i<flowLayout.getChildCount();i++){
                    totalViewWidth+=flowLayout.getChildAt(i).getMeasuredWidth()+CommentUtils.dip2px(Objects.requireNonNull(getContext()),15);
                    if (totalViewWidth>flowLayoutWidth){
                        mViews.add(flowLayout.getChildAt(i));
                    }
                }
                for (View view:mViews)
                    flowLayout.removeView(view);
            }
        });
    }
    private void initListener() {
        tvAllShopping.setOnClickListener(v -> startActivity(new Intent(getActivity(),CategoryListsActivity.class)));
        rlSearch.setOnClickListener(v -> jumpSearchActivity());
        swipeRefreshLayout.setOnRefreshListener(() -> requestAd());
        ivShoppingCenter.setOnClickListener(v -> startActivity(new Intent(getActivity(),ShoppingCenterActivity.class)));
        icOrderList.setOnClickListener(v -> startActivity(new Intent(getActivity(),OrderListActivity.class).putExtra("state", Constants.PAY_STATE_NO+"")));//跳转未支付列表
        banner.setOnBannerListener(position -> {
            if(mTitleImages!=null) {
                try {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BeautyDefine.getCommonDefine().getPageUri(mTitleImages.get(position).getPage(),
                            mTitleImages.get(position).getParameter())));
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void initBannerHeight() {
        int appBarLayoutHeight = (CommentUtils.getScreenWidth(Objects.requireNonNull(getActivity()))-2*CommentUtils.dip2px(getActivity(),15)) * 18 / 43;
        banner.getLayoutParams().height = (int) appBarLayoutHeight;

    }
    void showTitleImages(List<ADGroupBean.DataBean> mTitleImages){
        if (mTitleImages==null) {
            this.mTitleImages =new ArrayList<>();
        }else
            this.mTitleImages = mTitleImages;

        banner.setImages(getTitleImages()).setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
                .setBannerTitles(getTitleDes()).setImageLoader(new GlideImageLoader()).setDelayTime(3000).start();
        llRoot.setVisibility(View.VISIBLE);
        rlHot.setVisibility(View.VISIBLE);

    }


    private List<String> getTitleImages() {
        List<String> mLists =new ArrayList<>();
        for (ADGroupBean.DataBean mBean:mTitleImages){
            mLists.add(mBean.getPic());
        }
        return mLists;
    }
    private List<String> getTitleDes() {
        List<String> mLists =new ArrayList<>();
        for (ADGroupBean.DataBean mBean:mTitleImages){
            //  mLists.add(mBean.getTitle()); 2020/11/10 去掉标题
            mLists.add("");
        }
        return mLists;
    }
    @Override
    public void onResume() {
        super.onResume();
        banner.startAutoPlay();
    }

    @Override
    public void onPause() {
        super.onPause();
        banner.stopAutoPlay();
    }
    private void jumpSearchActivity(){
        jumpSearchActivity(null);
    }

    /**
     * @param searchWord 搜索词
     */
    private void jumpSearchActivity(String searchWord){
        if(getActivity()!=null) {
            Intent intent = new Intent(getActivity(), ShoppingSearchActivity.class);
            if (searchWord != null) {
                intent.putExtra("search", searchWord);
            }
            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                ActivityCompat.startActivity(getActivity(), intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), rlSearch, "shoppingSearch").toBundle());
            } else {
                startActivity(intent);
            }
            getActivity().overridePendingTransition(0, 0);
        }
    }

    /**
     * 滑动到顶部部
     */
    public void scrollViewTop(){
        if (mFragment!=null) {
            mFragment.scrollTop();
            appBar.setExpanded(true, true);
        }
    }

    /**
     * 获取appbarlayout 与 recy 控件
     * @param fragmentStateListener
     */
    public void setFragmentStateListener(FragmentStateListener fragmentStateListener){
        this.fragmentStateListener = fragmentStateListener;
    }

}
