package com.xingwang.classroom.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.WebSettings;
import com.xingwang.classroom.R;
import com.xingwang.classroom.dialog.CenterDefineDialog;
import com.xingwang.classroom.utils.SharedPreferenceUntils;
import com.xingwang.classroom.view.CustomToolbar;
import com.ycbjie.webviewlib.InterWebListener;
import com.ycbjie.webviewlib.WebProgress;
import com.ycbjie.webviewlib.X5WebChromeClient;
import com.ycbjie.webviewlib.X5WebUtils;
import com.ycbjie.webviewlib.X5WebView;


/**
 * Date:2020/7/7
 * Time;13:43
 * author:baiguiqiang
 */
public class LiveWebActivity extends BaseNetActivity {
    private X5WebView webView;
    private X5WebChromeClient x5WebChromeClient;
    private WebProgress progress;

    @Override
    protected int layoutResId() {
        return R.layout.activity_live_web;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = findViewById(R.id.webview);
        ((CustomToolbar)(findViewById(R.id.toolbar))).setNavigationOnClickListener(v -> finish());
        progress = findViewById(R.id.progress1);
        progress.show();
        progress.setColor(this.getResources().getColor(R.color.colorAccentClassRoom));



        if ( webView.getX5WebViewExtension()!=null){
            Bundle data = new Bundle();
            data.putBoolean("standardFullScreen", true);// true表示标准全屏，false表示X5全屏；不设置默认false，
            data.putBoolean("supportLiteWnd", false);// false：关闭小窗；true：开启小窗；不设置默认true，
            data.putInt("DefaultVideoScreen", 1);// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1
            webView.getX5WebViewExtension().invokeMiscMethod("setVideoParams", data);
            SharedPreferenceUntils.saveX5State(this,true);
        }else {
            //  SharedPreferenceUntils.saveX5State(this,false);
            CenterDefineDialog mDialog = CenterDefineDialog.getInstance("x5内核安装失败，播放异常,关闭应用重试");
            mDialog .setCallback(integer -> {
                try {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }catch (Exception e){
                    try {
                        System.exit(0);
                    }catch (Exception e1){
                        e1.printStackTrace();
                    }
                }
            });
            mDialog.showDialog(getSupportFragmentManager());
        }
        setWebViewSetting();
        x5WebChromeClient = webView.getX5WebChromeClient();
        x5WebChromeClient.setWebListener(interWebListener);
        webView.getX5WebViewClient().setWebListener(interWebListener);
        webView.setShowCustomVideo(false);
        webView.loadUrl("http://xielei.test.xw518.com/zyapp.test.xw518.com/public/zhibo");

    }
    private void setWebViewSetting(){
        WebSettings websettings = webView.getSettings();
        websettings.setDomStorageEnabled(true);  // 开启 DOM storage 功能
        websettings.setAppCacheMaxSize(1024*1024*8);
        String appCachePath =getApplicationContext().getCacheDir().getAbsolutePath();
        websettings.setAppCachePath(appCachePath);
        websettings.setAllowFileAccess(true);    // 可以读取文件缓存
        websettings.setAppCacheEnabled(true);    //开启H5(APPCache)缓存功能
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //全屏播放退出全屏
                if (x5WebChromeClient != null && x5WebChromeClient.inCustomView()) {
                    x5WebChromeClient.hideCustomView();
                    return true;
                    //返回网页上一页
                } else if (webView.canGoBack()) {
                    webView.goBack();
                    return true;
                    //退出网页
                } else {
                    handleFinish();
                }
            }
        return super.onKeyDown(keyCode, event);
    }
    public void handleFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
    private InterWebListener interWebListener = new InterWebListener() {
        @Override
        public void hindProgressBar() {
            progress.hide();
        }

        @Override
        public void showErrorView(@X5WebUtils.ErrorType int type) {
            switch (type){
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
            progress.setWebProgress(newProgress);
            String auth ="Yt7daGui/arohoSc7Kzgam07KF+iDO9M66EzpWTxN3TEx0xNj2D1iVbjfAOv6d6qL8Q+u1nYd/ZAOWCUboPvoARJACwvFGC3tNIW8ABySss=";
            if (newProgress>=70){
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript("window.localStorage.setItem('authstrLogin','" + auth + "');",null);
                } else
                    webView.loadUrl("javascript:localStorage.setItem('authstrLogin','"+ auth +"');");

            }
        }

        @Override
        public void showTitle(String title) {

        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webViewDestroy();
    }

    private  void  webViewDestroy(){
        if (x5WebChromeClient!=null)
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
