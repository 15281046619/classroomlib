package com.xingwang.classroomlib.html;

import android.net.http.SslError;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by hds on 2018/5/29.
 * Contact me by hds@jglist.com
 * webView 回调代理
 */
public interface WebViewDelegate {
    /**
     * onReceiveTitle
     * @param view
     * @param title
     */
    void onReceiveTitle(WebView view, String title);

    /**
     * onReceiveError
     * @param view
     * @param errorCode
     * @param description
     * @param failingUrl
     */
    void onReceiveError(WebView view, int errorCode, String description, String failingUrl);

    /**
     * onPageFinished
     * @param view
     * @param url
     */
    void onPageFinished(WebView view, String url);

    /**
     * onPageStarted
     * @param view
     * @param url
     */
    void onPageStarted(WebView view, String url);

    /**
     * onReceiverSSLError
     * @param view
     * @param handler
     * @param error
     */
    void onReceiverSSLError(WebView view, SslErrorHandler handler, SslError error);

    /**
     * onPageLoading
     * @param view
     * @param progress
     */
    void onPageLoading(WebView view, int progress);

    /**
     * onShowCustomView
     * @param view
     * @param callback
     */
    void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback);

    /**
     * onHideCustomView
     */
    void onHideCustomView() ;

    /**
     * onScroll
     * @param dx
     * @param dy
     */
    void onScroll(int dx, int dy);
}