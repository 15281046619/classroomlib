package com.xingwang.classroomlib;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.xingwang.classroom.ClassRoomLibUtils;
import com.xingwang.classroomlib.html.WebViewDelegate;

import java.lang.ref.WeakReference;


public class MainActivity extends AppCompatActivity implements WebViewDelegate {

    // private Html5WebView html5WebView;
    //  private FrameLayout webContainer;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       ClassRoomLibUtils.startListActivity(this,"栏目");

        // html5WebView = new Html5WebView(this);
        // html5WebView =findViewById(R.id.layout_web_container);
        // attachWebContainer();
        //  html5WebView.setWebViewDelegate(this);
        // html5WebView.loadUrl("https://txc-hls-ipc.tplinkcloud.com.cn/social/showPage.html?shareId=27d20ef2cbb14069b7d0fe9b542c6ab8");

    }
    private void attachWebContainer() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //   webContainer.addView(html5WebView, layoutParams);
    }
    static class MyWebViewClient extends WebViewClient {
        private WeakReference<WebView> weakReference;

        private MyWebViewClient(WebView target) {
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
            if (weakReference.get() != null ) {
                view.loadUrl("javascript:$('#header').remove();$('#videoSign').remove();" +
                        " $('#videoOutline').css({ 'width': window.screen.width + 'px' });" +
                        " $('#videoOutline').css({'display':'flex','justifyContent':'center','alignItems':'center'})");
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (weakReference.get() != null ) {

            }
            super.onPageStarted(view, url, favicon);
        }

        /**
         * 错误提示
         */
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (weakReference.get() != null ) {

            }
//            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (weakReference.get() != null ) {

            }
        }
    }
    /**
     * 页面暂停，需要暂停网页的解析
     */
    @Override
    protected void onPause() {
        super.onPause();
        //    html5WebView.onPause();
        //   html5WebView.pauseTimers() ;//小心这个！！！暂停整个 WebView 所有布局、解析、JS。
    }

    @Override
    protected void onResume() {
        super.onResume();
        //    html5WebView.onResume();
        //     html5WebView.resumeTimers();
    }

    /**
     * 页面销毁，需要停止网页的解析，防止内存泄漏
     */
    @Override
    protected void onDestroy() {
/*     html5WebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        html5WebView.clearHistory();
       // webContainer.removeView(html5WebView);
        html5WebView.destroy();*/
        super.onDestroy();
    }



    @Override
    public void onReceiveTitle(WebView view, String title) {

    }

    @Override
    public void onReceiveError(WebView view, int errorCode, String description, String failingUrl) {

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        view.loadUrl("javascript:$('#header').remove();$('#videoSign').remove();");
    }

    @Override
    public void onPageStarted(WebView view, String url) {

    }

    @Override
    public void onReceiverSSLError(WebView view, SslErrorHandler handler, SslError error) {

    }

    @Override
    public void onPageLoading(WebView view, int progress) {

    }

    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {

    }

    @Override
    public void onHideCustomView() {

    }

    @Override
    public void onScroll(int dx, int dy) {

    }
}
