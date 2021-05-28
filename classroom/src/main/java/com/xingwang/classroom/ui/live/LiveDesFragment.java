package com.xingwang.classroom.ui.live;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.WebSettings;
import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.LiveDetailBean;
import com.xinwang.bgqbaselib.base.BaseLazyLoadFragment;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.view.CustomWebView;
import com.xinwang.bgqbaselib.view.VpSwipeRefreshLayout;
import com.ycbjie.webviewlib.wv.WvWebView;

/**
 * Date:2020/8/13
 * Time;15:24
 * author:baiguiqiang
 */
public class LiveDesFragment extends BaseLazyLoadFragment {

    protected CustomWebView webView;
    protected String htmlText;
    private VpSwipeRefreshLayout swipeRefreshLayout;

    public static LiveDesFragment getInstance(String des,int id){
        LiveDesFragment mFragment = new LiveDesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA,des);
        bundle.putInt("id",id);
        mFragment.setArguments(bundle);
        return mFragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live_des_classroom,container,false);

        webView=view.findViewById(R.id.web_live_des);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDetailContent();
            }
        });
        return view;
    }
    private void getDetailContent(){
        if (getArguments()!=null)
        requestGet(HttpUrls.URL_LIVE_DETAIL(),new ApiParams().with("id",getArguments().getInt("id")),
                LiveDetailBean.class, new HttpCallBack<LiveDetailBean>() {

                    @Override
                    public void onFailure(String message) {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(LiveDetailBean liveDetailBean) {
                        loadData(liveDetailBean.getData().getLive().getBody());
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }
    @Override
    public void initData() {
        htmlText=getArguments().getString(Constants.DATA);
     //   setWebViewSetting();
        //loadData(htmlText);
        webView.loadData(htmlText);
    }
    private void loadData(String content){
        content = content.replace("<img", "<img style=\"max-width:100%;height:auto\"");
        webView.loadData(content, "text/html;charset=utf-8", "utf-8");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.onDestroy();
    }


}
