package com.xingwang.classroomlib;


import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


import com.xingwang.classroom.ClassRoomLibUtils;
import com.xingwang.classroom.ui.live.LiveListActivity;
import com.xingwang.classroomlib.html.WebViewDelegate;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.CommonEntity;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.shoppingcenter.ui.ShoppingHomeActivity;

import static com.xinwang.bgqbaselib.http.HttpUrls.URL_HOST;
import static com.xinwang.bgqbaselib.http.HttpUrls.URL_NAME;


public class MainActivity extends BaseNetActivity implements WebViewDelegate {


    @Override
    protected int layoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   Log.d("TAG","ArrayList："+(System.currentTimeMillis()-cur));
        // ClassRoomLibUtils.startListActivity(this,"栏目");


   /*     try {
            ClassRoomLibUtils.startActivityForUri(this,"xingw://com.xingw.zyapp/openpage/webbrowser?url=http://zyapp.app.xw518.com/zhibo/#/index?id=224");
            //ClassRoomLibUtils.startWebActivity(this,"http://zyapp.app.xw518.com/zhibo/#/index?id=221",false,"测试");
          //  ClassRoomLibUtils.startWebActivity(this,URLDecoder.decode("http%3a%2f%2fzyapp.app.xw518.com%2fzhibo%2f%23%2findex%3fid%3d224", "UTF-8"),false,"测试");
            *//*String url = new String("http%3a%2f%2fzyapp.app.xw518.com%2fzhibo%2f%23%2findex%3fid%3d224".getBytes(), "UTF-8");
            url = URLDecoder.decode(url, "UTF-8");
            *//*
            LogUtil.i(URLDecoder.decode("http://zyapp.app.xw518.com/zhibo/#/index?id=224", "UTF-8"));

           // ClassRoomLibUtils.startWebActivity(this,url,false,"测试");

            //ClassRoomLibUtils.startWebActivity(this,"http%3a%2f%2fzyapp.app.xw518.com%2fpage%2fshare_article%3fid%3d1230",false,"测试");

            //LogUtil.i(URLEncoder.encode("http://zyapp.app.xw518.com/zhibo/#/index?id=224", "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        // ClassRoomLibUtils.startWebActivity(this,"http%3a%2f%2fzyapp.app.xw518.com%2fpage%2fshare_article%3fid%3d1230",false,"测试");

        // startActivity(new Intent(this, LiveWebActivity.class));
   //     Uri uri = Uri.parse("classroom://com.xingw.zyapp.zblist");
      /*  Uri uri = Uri.parse("classroom://com.xingwang.classroomlib.zbdetail?id=224&is_end=0");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);*/
       // Uri uri = Uri.parse("classroom://com.xingwang.classroomlib.pldetail?div_id=482&lecture_id=51&bid=0");

        Uri uri = Uri.parse("classroom://"+getPackageName()+".spdetail?id=5");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
    //    startActivity(intent);
       getAuthStr("18202839759");
    }
        private void getAuthStr(String phone){
            requestGet(URL_HOST + URL_NAME + "user/base/auth/get-authstr", new ApiParams().with("phone", phone).with("code", "99889988"), CommonEntity.class, new HttpCallBack<CommonEntity>() {
                @Override
                public void onFailure(String message) {

                }

                @Override
                public void onSuccess(CommonEntity commonEntity) {

                }
            });
        }

    public void onClick(View view){
        String tag =(String) view.getTag();
        switch (tag){
            case "1":
               // startActivity( new Intent(Intent.ACTION_VIEW,Uri.parse("circle://"+getPackageName()+".host.card?id=56")));
                ClassRoomLibUtils.startHistoryPriceActivity(this,"1");
                break;
            case "2":
                startActivity( new Intent(Intent.ACTION_VIEW,Uri.parse("essay://"+getPackageName()+".host.adessay?url=http://zyapp.test.xw518.com/article/859")));
                break;
          case "3":
                startActivity(new Intent(this, ShoppingHomeActivity.class));
                break;
              case "4":
                //startActivity( new Intent(this, WebViewActivity.class).putExtra("title","ces").putExtra("url","http://oss.xw518app.xw518.com/2021/02/18/091026_91db52b2ade14a2bb9e4e2b76629578a5833zy.mp4"));
                break;
            /*case "5":
               GroupListActivity.getIntent(MainActivity.this);
                break;*/
            case "6":
                ClassRoomLibUtils.startListActivity(this,"栏目");
                break;
            case "7":
                startActivity(new Intent(this, LiveListActivity.class));
                break;
        }

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
