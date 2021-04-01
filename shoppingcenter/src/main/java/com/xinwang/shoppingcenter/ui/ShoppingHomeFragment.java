package com.xinwang.shoppingcenter.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.internal.FlowLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.xinwang.bgqbaselib.base.BaseLazyLoadFragment;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.CommonEntity;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.FragmentUtils;
import com.xinwang.bgqbaselib.utils.GlideImageLoader;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.bgqbaselib.view.VpSwipeRefreshLayout;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.ADGroupBean;
import com.xinwang.shoppingcenter.bean.FragmentUpdateBean;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;


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
    private TextView tvAllShopping;
    private LinearLayout llRoot;
    private RelativeLayout rlHot;
    private CustomToolbar toolbar;
    private ImageView ivShoppingCenter;
    private List<ADGroupBean.DataBean> mTitleImages;
    private ProductListsFragment mFragment;
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_shopping_home_shoppingcenter,container,false);
        EventBus.getDefault().register(this);
        swipeRefreshLayout =view.findViewById(R.id.swipeRefreshLayout);
        rlHot =view.findViewById(R.id.rlHot);
        appBar =view.findViewById(R.id.app_bar_layout);
        llRoot =view.findViewById(R.id.llRoot);
        rlSearch =view.findViewById(R.id.rlSearch);
        flowLayout =view.findViewById(R.id.flowLayout);
        toolbar =view.findViewById(R.id.toolbar);
        banner =view.findViewById(R.id.banner);
        tvAllShopping =view.findViewById(R.id.tvAllShopping);
        ivShoppingCenter =view.findViewById(R.id.ivShoppingCenter);
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
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


    }

    private void initToolbar() {

        if (getActivity()!=null&&!(getActivity() instanceof ShoppingHomeActivity)){
            toolbar.getNavigationIcon().mutate().setAlpha(0);//必须加mutate，不然修改后全局toolbar都将更改
        }else
            toolbar.setNavigationOnClickListener(v -> getActivity().finish());
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
           /*     if (mFragment!=null)//如果创建过，就清空 免得不断刷新出现内存溢出
                    getChildFragmentManager().beginTransaction().remove(mFragment).commitAllowingStateLoss();*/
                if (mFragment==null) {
                    mFragment = ProductListsFragment.getInstance(null, null);
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
        Intent intent = new Intent(getActivity(), ShoppingSearchActivity.class);
        if (searchWord!=null){
            intent.putExtra("search",searchWord);
        }
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            ActivityCompat.startActivity(getActivity(),intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), rlSearch, "shoppingSearch").toBundle());
        } else {
            startActivity(intent);
        }
        getActivity().overridePendingTransition(0,0);
    }



}
