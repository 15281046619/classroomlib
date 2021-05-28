package com.xinwang.bgqbaselib.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.beautydefinelibrary.BeautyDefine;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;

import com.ycbjie.webviewlib.base.X5WebChromeClient;
import com.ycbjie.webviewlib.client.JsX5WebViewClient;
import com.ycbjie.webviewlib.inter.InterWebListener;
import com.ycbjie.webviewlib.inter.VideoWebListener;
import com.ycbjie.webviewlib.utils.X5WebUtils;
import com.ycbjie.webviewlib.view.X5WebView;

import java.util.ArrayList;


/**
 * Date:2021/3/31
 * Time;10:11
 * author:baiguiqiang
 */
public class CustomWebView extends X5WebView {
    private X5WebChromeClient x5WebChromeClient;
    private boolean isLoadUrl =false;
    private boolean isSettingHeight =true;
    private OnWebListener onWebListener;
    public CustomWebView(Context arg0) {
        super(arg0,null);
    }

    public CustomWebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        init();
    }
    public void setOnWebListener(OnWebListener onWebListener){
        this.onWebListener =onWebListener;
    }
    private void init(){
        if (getX5WebViewExtension()!=null){
            Bundle data = new Bundle();
            //data.putBoolean("standardFullScreen", false);// true表示标准全屏，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen",1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
            SharedPreferenceUntils.saveX5State(getContext(),true);
        }
        MyWebViewClient x5WebViewClient = new MyWebViewClient(this, getContext());
        setWebViewClient(x5WebViewClient);

        x5WebViewClient.setWebListener(interWebListener);
        setWebViewSetting();
        addJavascriptInterface(new JavaScriptInterface(getContext()),"javaInterFace");
        x5WebChromeClient = getX5WebChromeClient();
        x5WebChromeClient.setWebListener(interWebListener);
        if (x5WebChromeClient!=null)
            x5WebChromeClient.setVideoWebListener(new VideoWebListener() {

                @Override
                public void showVideoFullView() {

                }

                @Override
                public void hindVideoFullView() {

                }

                @Override
                public void showWebView() {

                }

                @Override
                public void hindWebView() {

                }
            });

    }
    public void loadData(String htmlContent){
        loadData(CommentUtils.getWebNewData( htmlContent), "text/html;charset=utf-8", "utf-8");
    }
    public void loadUrl(String url,boolean isSettingHeight){
        this.isSettingHeight = isSettingHeight;
        isLoadUrl =true;
        super.loadUrl(url);
    }

    /**
     *activity onkeyDown 调用
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onViewKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //全屏播放退出全屏
            if (x5WebChromeClient!=null && x5WebChromeClient.inCustomView()) {
                x5WebChromeClient.hideCustomView();
                return true;
                //返回网页上一页
            } else if (pageCanGoBack()) {
                //退出网页
                return pageGoBack();
            } else {
                ((Activity) getContext()).finish();
            }
        }
        return false;
    }
    class MyWebViewClient extends JsX5WebViewClient {


        /**
         * 构造方法
         *
         * @param webView 需要传进来webview
         * @param context 上下文
         */
        public MyWebViewClient(X5WebView webView, Context context) {
            super(webView, context);
        }

        /**
         * 点击页面的某条链接进行跳转的话，会启动系统的默认浏览器进行加载，
         * 调出了我们本身的应用 因此，要在shouldOverrideUrlLoading方法中
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                view.loadUrl(url);
                return false;
            }
            return true;
        }

        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
            super.onReceivedError(webView, i, s, s1);
        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            super.onReceivedError(webView, webResourceRequest, webResourceError);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (isLoadUrl)
                addImageClickListner();
            super.onPageFinished(view, url);
        }
    }
    private InterWebListener interWebListener = new InterWebListener() {
        @Override
        public void hindProgressBar() {
            if (onWebListener!=null)
                onWebListener.hindProgressBar();
            //progress.hide();
        }

        @Override
        public void showErrorView(@X5WebUtils.ErrorType int type) {
            switch (type) {
                //没有网络
                case X5WebUtils.ErrorMode.NO_NET:
                    break;
                //404，网页无法打开
                case X5WebUtils.ErrorMode.STATE_404:

                    break;
                //onReceivedError，请求网络出现error
                case X5WebUtils.ErrorMode.RECEIVED_ERROR:

                    break;
                //在加载资源时通知主机应用程序发生SSL错误
                case X5WebUtils.ErrorMode.SSL_ERROR:

                    break;
                default:
                    break;
            }
        }

        @Override
        public void startProgress(int newProgress) {
            if (onWebListener!=null)
                onWebListener.startProgress(newProgress);

        }

        @Override
        public void showTitle(String title) {
        }

        @Override
        public void onPageFinished(String url) {

        }
    };

    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        loadUrl("javascript:(function(){var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.javaInterFace.openImage(this.src);  " +
                "    }  " +
                "} " +
                "javaInterFace.resize(document.body.getBoundingClientRect().height)" +//加载完成通过接口设置webview高度
                "})()");
    }
    public  class JavaScriptInterface {
        private Context context;
        public JavaScriptInterface(Context context) {
            this.context = context;
        }
        //点击图片回调方法
        //必须添加注解,否则无法响应
        @JavascriptInterface()
        public void openImage(String img) {//统一跳转图片浏览器
          /*  if (onClickImgListener!=null){
                onClickImgListener.onclickImg(img);
            }*/
            ArrayList<String> mLists = new ArrayList<>();
            mLists.add(img);
            BeautyDefine.getImagePreviewDefine((Activity) context).showImagePreview(mLists, 0);
        }
        @JavascriptInterface()
        public void resize(final float height) {
            if (isSettingHeight)
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getParent() instanceof LinearLayout)
                        setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
                    else if (getParent() instanceof RelativeLayout)
                        setLayoutParams(new RelativeLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
                    setVisibility(View.VISIBLE);

                }
            });
        }
    }
    private void setWebViewSetting(){
        WebSettings websettings = getSettings();
        websettings.setDomStorageEnabled(true);  // 开启 DOM storage 功能
        websettings.setAppCacheMaxSize(1024*1024*8);
        String appCachePath =getContext().getCacheDir().getAbsolutePath();
        websettings.setAppCachePath(appCachePath);
        websettings.setAllowFileAccess(true);    // 可以读取文件缓存
        websettings.setAppCacheEnabled(true);    //开启H5(APPCache)缓存功能

        websettings.setDefaultTextEncodingName("utf-8");
        websettings.setBuiltInZoomControls(false);
        websettings.setSupportZoom(false);
        websettings.setDisplayZoomControls(false);


    }



    /**
     * ondestroy调用
     */
    public void onDestroy() {
        if (x5WebChromeClient != null) {
            //有音频播放的web页面的销毁逻辑
            //在关闭了Activity时，如果Webview的音乐或视频，还在播放。就必须销毁Webview
            //但是注意：webview调用destory时,webview仍绑定在Activity上
            //这是由于自定义webview构建时传入了该Activity的context对象
            //因此需要先从父容器中移除webview,然后再销毁webview:
            ViewGroup parent = (ViewGroup) this.getParent();
            if (parent != null) {
                parent.removeView(this);
            }
            removeAllViews();
            destroy();
            removeJavascriptInterface("imagelistner");
        }
    }
    public interface  OnWebListener{
        void hindProgressBar();
        void startProgress(int progress);
    }
}
