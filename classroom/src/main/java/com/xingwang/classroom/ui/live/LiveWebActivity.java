package com.xingwang.classroom.ui.live;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.KeyEvent;
import android.view.View;
import com.xingwang.classroom.R;


import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.view.CustomWebView;

import com.ycbjie.webviewlib.widget.WebProgress;


/**
 * Date:2020/7/7
 * Time;13:43
 * author:baiguiqiang
 */
public class LiveWebActivity extends BaseNetActivity {
    private CustomWebView webView;
    private WebProgress progress;
    private CustomToolbar customToolbar;
    private FloatingActionButton fab;
    @Override
    protected int layoutResId() {
        return R.layout.activity_live_web_classroom;
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = findViewById(R.id.webview);
        customToolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        fab.setVisibility(getIntent().getBooleanExtra("isProduct",false)? View.VISIBLE:View.GONE);
        customToolbar.setNavigationOnClickListener(v -> finish());
        customToolbar.setText(getIntent().getStringExtra("title"));
        progress = findViewById(R.id.progress1);
        progress.show();
        progress.setColor(this.getResources().getColor(R.color.colorAccentClassRoom));



        webView.loadUrl(getIntent().getStringExtra("url"),false);
        webView.setOnWebListener(new CustomWebView.OnWebListener() {
            @Override
            public void hindProgressBar() {
                progress.hide();
            }

            @Override
            public void startProgress(int newProgress) {
                progress.setWebProgress(newProgress);
            }
        });
        fab.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(this,Class.forName("com.mainlibrary.home.mine.mine.privatechat.PrivateChatActivity"));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TEXT,getIntent().getStringExtra("url"));
                startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (webView!=null)
            return webView.onViewKeyDown(keyCode,event);
        else
            return super.onKeyDown(keyCode, event);
    }
    public void handleFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView!=null)
            webView.onDestroy();
    }


}
