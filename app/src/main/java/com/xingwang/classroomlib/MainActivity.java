package com.xingwang.classroomlib;


import android.content.Intent;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;



import com.xingwang.classroom.ClassRoomLibUtils;

import com.xingwang.classroom.ui.LiveListActivity;
import com.xingwang.classroom.ui.LiveWebActivity;
import com.xingwang.classroomlib.html.WebViewDelegate;




public class MainActivity extends AppCompatActivity implements WebViewDelegate {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClassRoomLibUtils.startListActivity(this,"栏目");
       // startActivity(new Intent(this, LiveWebActivity.class));
        startActivity(new Intent(this, LiveListActivity.class));

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
