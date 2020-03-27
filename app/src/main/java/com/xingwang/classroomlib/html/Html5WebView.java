package com.xingwang.classroomlib.html;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.http.SslError;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import com.xingwang.classroomlib.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *
 * 自定义webView
 */
@SuppressLint("SetJavaScriptEnabled")
public class Html5WebView extends WebView {

    private ProgressBar progressbar;
    private HashMap<String,String> titleMap;
    private ArrayList<String> titleList;
    public Html5WebView(Context context) {
        super(context);
        titleMap = new HashMap<>();
        titleList = new ArrayList<>();
        init();
    }

    public void setWebViewDelegate(WebViewDelegate webViewDelegate) {
        this.webViewDelegate = webViewDelegate;
    }

    private WebViewDelegate webViewDelegate;
    public Html5WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Html5WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        progressbar = new ProgressBar(getContext(), null,
                android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                6, 0, 0));
        Drawable[] layers = new Drawable[2];
        ColorDrawable background = new ColorDrawable(Color.GRAY);
        ColorDrawable progress = new ColorDrawable(ContextCompat.getColor(getContext(), R.color.colorAccent));
        ClipDrawable clip = new ClipDrawable(progress, Gravity.LEFT,
                ClipDrawable.HORIZONTAL);
        layers[0] = background;
        layers[1] = clip;
        LayerDrawable layer = new LayerDrawable(layers);
        layer.setId(0, android.R.id.background);
        layer.setId(1, android.R.id.progress);
        progressbar.setProgressDrawable(layer);
        addView(progressbar);
        setVerticalScrollBarEnabled(false);
        WebSettings mWebSettings = getSettings();
        mWebSettings.setSupportZoom(false);//支持缩放，默认为true。是下面那个的前提
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setBlockNetworkImage(false);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDomStorageEnabled(true);
      //  mWebSettings.setUseWideViewPort(true);//将图片调整到适合webview的大小
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setPluginState(WebSettings.PluginState.ON);
        mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebSettings.setAllowFileAccess(true);
        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion>Build.VERSION_CODES.KITKAT){
//            mWebSettings.setUseWideViewPort(true);
        }
        if (currentApiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(this, true);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        setWebChromeClient(new MyWebChromeClient(this));
        setWebViewClient(new MyWebViewClient(this));
        setDrawingCacheEnabled(false);
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
    }

    @Override
    public void goBack() {
        super.goBack();
        if (!titleMap.isEmpty()&&!titleList.isEmpty()&&webViewDelegate!=null){
            titleList.remove(getUrl());
            if (titleList.size()>0){
                webViewDelegate.onReceiveTitle(this,titleMap.get(titleList.get(titleList.size()-1)));
            }
        }
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        if (webViewDelegate!=null){
            webViewDelegate.onScroll(l - oldl, t - oldt);
        }

    }

    static class MyWebChromeClient extends WebChromeClient{
        private WeakReference<Html5WebView> weakReference;
        private MyWebChromeClient(Html5WebView target){
            weakReference = new WeakReference<>(target);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (weakReference.get()!=null){
                if (weakReference.get().webViewDelegate!=null){
                    weakReference.get().webViewDelegate.onPageLoading(view,newProgress);
                }
                if (newProgress == 100) {
                    weakReference.get().progressbar.setVisibility(GONE);
                } else {
                    weakReference.get().progressbar.setVisibility(VISIBLE);
                    weakReference.get().progressbar.setProgress(newProgress);
                }
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            result.confirm();
            return true;
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
        }

        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
        }

        @Override
        public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (weakReference.get()!=null){
                weakReference.get().titleMap.put(view.getUrl(),title);
                weakReference.get().titleList.add(view.getUrl());
                if (weakReference.get().webViewDelegate!=null){
                    weakReference.get().webViewDelegate.onReceiveTitle(view,title);
                }
            }

        }
        // 全屏
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if (weakReference.get().webViewDelegate!=null){
                weakReference.get().webViewDelegate.onShowCustomView(view,callback);
            }
        }

        @Override
        public void onHideCustomView() {
            if (weakReference.get().webViewDelegate!=null){
                weakReference.get().webViewDelegate.onHideCustomView();
            }
            super.onHideCustomView();
        }
    }
    static class MyWebViewClient extends WebViewClient {
        private WeakReference<Html5WebView> weakReference;
        private MyWebViewClient(Html5WebView target){
            weakReference = new WeakReference<>(target);
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
        public void onPageFinished(WebView view, String url) {
            if (weakReference.get()!=null && weakReference.get().webViewDelegate!=null){
                weakReference.get().webViewDelegate.onPageFinished(view,url);
            }

            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (weakReference.get()!=null && weakReference.get().webViewDelegate!=null){
                weakReference.get().webViewDelegate.onPageStarted(view,url);
            }
            super.onPageStarted(view, url, favicon);
        }

        /**
         * 错误提示
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (weakReference.get()!=null && weakReference.get().webViewDelegate!=null){
                weakReference.get().webViewDelegate.onReceiveError(view,errorCode,description,failingUrl);
            }
//            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view,  SslErrorHandler handler, SslError error) {
            if (weakReference.get()!=null && weakReference.get().webViewDelegate!=null){
                weakReference.get().webViewDelegate.onReceiverSSLError(view,handler,error);
            }
        }
    }


}