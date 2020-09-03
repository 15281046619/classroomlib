package com.xingwang.classroom.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.WebSettings;
import com.xingwang.classroom.R;
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

        webView.loadData(htmlText, "text/html;charset=utf-8", "utf-8");
    }
    private void setWebViewSetting(){
        WebSettings websettings = webView.getSettings();
        websettings.setDomStorageEnabled(true);  // 开启 DOM storage 功能
        websettings.setAppCacheMaxSize(1024*1024*8);
        String appCachePath =getActivity().getApplicationContext().getCacheDir().getAbsolutePath();
        websettings.setAppCachePath(appCachePath);
        websettings.setAllowFileAccess(true);    // 可以读取文件缓存
        websettings.setAppCacheEnabled(true);    //开启H5(APPCache)缓存功能
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
