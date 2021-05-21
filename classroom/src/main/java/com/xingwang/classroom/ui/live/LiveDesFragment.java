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
import com.xinwang.bgqbaselib.view.VpSwipeRefreshLayout;
import com.ycbjie.webviewlib.WvWebView;


/**
 * Date:2020/8/13
 * Time;15:24
 * author:baiguiqiang
 */
public class LiveDesFragment extends BaseLazyLoadFragment {

    protected WvWebView webView;
    protected String htmlText;
    private VpSwipeRefreshLayout swipeRefreshLayout;
    private   String appCachePath;
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
        setWebViewSetting();
        loadData(htmlText);
    }
    private void loadData(String content){
        content = content.replace("<img", "<img style=\"max-width:100%;height:auto\"");
        webView.loadData(content, "text/html;charset=utf-8", "utf-8");
    }

    private void setWebViewSetting(){
        appCachePath =getActivity().getApplicationContext().getCacheDir().getAbsolutePath();
        WebSettings websettings = webView.getSettings();
        websettings.setDomStorageEnabled(true);  // 开启 DOM storage 功能
        websettings.setAppCacheMaxSize(1024*1024*8);

        websettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        websettings.setAppCachePath(appCachePath);
        websettings.setAllowFileAccess(true);    // 可以读取文件缓存
        websettings.setAppCacheEnabled(true);    //开启H5(APPCache)缓存功能
   /*     websettings.setUseWideViewPort(true);//关键点这两个属性要配合使用，根据自动适配屏幕大小
        websettings.setLoadWithOverviewMode(true);*/


        /**

         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：

         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放

         */
      // websettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webViewDestroy();
    }

    private  void  webViewDestroy(){

            //有音频播放的web页面的销毁逻辑
            //在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
            //但是注意：webview调用destory时,webview仍绑定在Activity上
            //这是由于自定义webview构建时传入了该Activity的context对象
            //因此需要先从父容器中移除webview,然后再销毁webview:
            if (webView != null) {
                ViewGroup parent = (ViewGroup) webView.getParent();
                if (parent != null) {
                    parent.removeView(webView);
                }
                webView.removeAllViews();
                webView.destroy();
                webView = null;
            }
    }
}
