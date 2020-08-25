package com.xingwang.classroomlib;


import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;



import com.xingwang.classroom.ClassRoomLibUtils;

import com.xingwang.classroom.ui.ClassRoomHomeActivity;
import com.xingwang.classroom.ui.LiveListActivity;
import com.xingwang.classroom.ui.LiveWebActivity;
import com.xingwang.classroom.utils.LogUtil;
import com.xingwang.classroomlib.html.WebViewDelegate;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class MainActivity extends AppCompatActivity implements WebViewDelegate {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClassRoomLibUtils.startListActivity(this,"栏目");


        try {
           // ClassRoomLibUtils.startActivityForUri(this,"xingw://com.xingw.zyapp/openpage/webbrowser?url=http://zyapp.app.xw518.com/zhibo/#/index?id=224");
           // ClassRoomLibUtils.startWebActivity(this,"http://zyapp.app.xw518.com/zhibo/#/index?id=221",false,"测试");
          //  ClassRoomLibUtils.startWebActivity(this,URLDecoder.decode("http%3a%2f%2fzyapp.app.xw518.com%2fzhibo%2f%23%2findex%3fid%3d224", "UTF-8"),false,"测试");
            /*String url = new String("http%3a%2f%2fzyapp.app.xw518.com%2fzhibo%2f%23%2findex%3fid%3d224".getBytes(), "UTF-8");
            url = URLDecoder.decode(url, "UTF-8");
            */
            LogUtil.i(URLDecoder.decode("http://zyapp.app.xw518.com/zhibo/#/index?id=224", "UTF-8"));
            /*Uri uri = Uri.parse("classroom://"+getApplicationInfo().packageName+".zbdetail?id=224");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
             startActivity(intent);*/
           // ClassRoomLibUtils.startWebActivity(this,url,false,"测试");

            //ClassRoomLibUtils.startWebActivity(this,"http%3a%2f%2fzyapp.app.xw518.com%2fpage%2fshare_article%3fid%3d1230",false,"测试");

            //LogUtil.i(URLEncoder.encode("http://zyapp.app.xw518.com/zhibo/#/index?id=224", "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
