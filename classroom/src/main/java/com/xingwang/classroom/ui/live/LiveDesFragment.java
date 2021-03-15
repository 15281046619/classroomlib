package com.xingwang.classroom.ui.live;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xingwang.classroom.R;
import com.xingwang.classroom.ui.BaseLazyLoadFragment;
import com.xingwang.classroom.utils.Constants;
import com.ycbjie.webviewlib.WvWebView;

/**
 * Date:2020/8/13
 * Time;15:24
 * author:baiguiqiang
 */
public class LiveDesFragment extends BaseLazyLoadFragment {

    protected WvWebView webView;
    protected String htmlText;

    public static LiveDesFragment getInstance(String des){
        LiveDesFragment mFragment = new LiveDesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA,des);
        mFragment.setArguments(bundle);
        return mFragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live_des_classroom,container,false);

        webView=view.findViewById(R.id.web_live_des);

        return view;
    }

    @Override
    public void initData() {
        htmlText=getArguments().getString(Constants.DATA);
        setWebViewSetting();
        htmlText = htmlText.replace("<img", "<img style=\"max-width:100%;height:auto\"");
        webView.loadData(htmlText, "text/html;charset=utf-8", "utf-8");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                return true;
            }
        });
    }
    private void setWebViewSetting(){
        WebSettings websettings = webView.getSettings();
        websettings.setDomStorageEnabled(true);  // 开启 DOM storage 功能
        websettings.setAppCacheMaxSize(1024*1024*8);
        String appCachePath =getActivity().getApplicationContext().getCacheDir().getAbsolutePath();
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
