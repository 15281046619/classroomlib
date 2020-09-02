package com.xingwang.essay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.ShareResultCallBack;
import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.essay.base.BaseActivity;
import com.xingwang.essay.bean.Essay;
import com.xingwang.swip.title.ActionItem;
import com.xingwang.swip.title.TitlePopup;
import com.xingwang.swip.title.TopTitleView;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.HttpUtil;
import com.xingwang.swip.utils.NoDoubleClickUtils;
import com.xingwreslib.beautyreslibrary.ArticleFavoriteInfo;
import com.xingwreslib.beautyreslibrary.ArticleFavoriteLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EssayWebviewActivity extends BaseActivity {

    protected TopTitleView title;

    protected ProgressBar progressBar;
    protected WebView webView;
    protected LinearLayout line_load_error;

    private Essay essay;
   // private String essayId;
    private List<String> imgList=new ArrayList<>();

    private boolean isCollect=false;//是否收藏 true-收藏 false-未收藏

    private HashMap<String,String> params=new HashMap<>();

    public static Intent getIntent(Context context, Essay essay) {
        Intent intent = new Intent(context, EssayWebviewActivity.class);
        intent.putExtra(Constants.INTENT_DATA, essay);
        return intent;
    }

    public static void getIntentForResult(Activity context, Essay essay, int requestCode,int resultCode) {
        Intent intent = new Intent(context, EssayWebviewActivity.class);
        intent.putExtra(Constants.INTENT_DATA, essay);
        intent.putExtra(Constants.INTENT_DATA1, resultCode);
        context.startActivityForResult(intent,requestCode);
    }
    @Override
    public int getLayoutId() {
        return R.layout.essay_activity_essay_webview;
    }

    @Override
    public void initData() {

        Intent intent=getIntent();

        essay= (Essay)intent.getSerializableExtra(Constants.INTENT_DATA);

        title.setTitleText("文章");
        if (EmptyUtils.isNotEmpty(essay)){//文章详情传入
            isCollect();

            title.addPopData(new ActionItem(getBaseContext(),"分享",R.drawable.reslib_ic_share_material));

            title.setOnItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
                @Override
                public void onItemClick(ActionItem item, int position) {

                    switch (position){
                        case 0://收藏
                            if (NoDoubleClickUtils.isDoubleClick()){
                                return;
                            }
                            if (isCollect){
                                collect(Constants.ESSAY_UNCOLLECT);
                            }else {
                                collect(Constants.ESSAY_COLLECT);
                            }
                            break;
                        case 1://分享
                            BeautyDefine.getShareDefine(EssayWebviewActivity.this).share(Constants.ESSAY_SHARE+essay.getId(), (ArrayList<String>) imgList, essay.getTitle(), essay.getTitle(), new ShareResultCallBack() {
                                @Override
                                public void onSucceed() {
                                    ToastUtils.showShortSafe("分享成功");
                                }

                                @Override
                                public void onFailure(String s) {
                                    ToastUtils.showShortSafe(s);
                                }
                            });
                            break;
                    }
                }
            });

            title.setRightFristClick(false);

            title.setRightFristImg(R.mipmap.essay_title_more_img, new TopTitleView.OnRightFirstClickListener() {
                @Override
                public void onRightFirstClickListener(View v) {
                    title.titlePopup.show(v);
                }
            });

            if (EmptyUtils.isNotEmpty(essay.getImgList())){
                imgList.add(essay.getImgList().get(0));
            }else {
                imgList.add(BeautyDefine.getUrlConfigDefine().getAppIconUrl());
            }
        }else {//uri
            Uri uri=intent.getData();
            if (EmptyUtils.isEmpty(uri)){
                ToastUtils.showShortSafe("数据错误！");
                this.finish();
            }
            essay=new Essay();
            essay.setTitle("链接分享");
            essay.setUrl(uri.getQueryParameter("url"));
        }

        checkNetWork();

        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    if (progressBar!=null)
                         progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            //覆写shouldOverrideUrlLoading实现内部显示网页
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  TODO 自动生成的方法存根
                if(url == null) return false;
                if(url.startsWith("http:")||url.startsWith("https:")){
                    view.loadUrl(url);
                    return false;
                }
                return false;
            }
        });

        line_load_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkNetWork();
            }
        });
    }

    @Override
    protected void initView() {

        title = (TopTitleView) findViewById(R.id.title);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (WebView) findViewById(R.id.webview);
        line_load_error = (LinearLayout) findViewById(R.id.line_load_error);

        WebSettings webseting = webView.getSettings();
        webseting.setSupportZoom(true);
        webseting.setUseWideViewPort(true);
        webseting.setJavaScriptEnabled(true);
        webseting.setBuiltInZoomControls(true);
        webseting.setLoadWithOverviewMode(true);
        webseting.setDisplayZoomControls(false);
        webseting.setJavaScriptCanOpenWindowsAutomatically(false);
    }

    private void checkNetWork(){
        if (NetworkUtils.isConnected()){
            webView.loadUrl(essay.getUrl());
            progressBar.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);
            line_load_error.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            webView.setVisibility(View.GONE);
            line_load_error.setVisibility(View.VISIBLE);
            ToastUtils.showShortSafe("连接失败，请检查网络！");
        }
    }

    //判断是否收藏
    private void isCollect(){
        params.clear();
        params.put("type","article");
        params.put("relation_id",essay.getId());
        HttpUtil.get(Constants.ESSAY_IS_COLLECT, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
                title.setRightFristClick(false);
            }

            @Override
            public void onSuccess(String json) {
                title.setRightFristClick(true);
                Log.i("Web","iscollect-->"+json);
                if (json.equals("0")){//此时未收藏
                    isCollect=false;
                    title.addPopData(new ActionItem(getBaseContext(),"未收藏",R.drawable.reslib_ic_star_border));
                    return;
                }
                isCollect=true;
                title.addPopData(new ActionItem(getBaseContext(),"已收藏",R.drawable.reslib_ic_star));
            }
        });
    }

    //收藏
    private void collect(String url){
        params.clear();
        params.put("type","article");
        params.put("relation_id",essay.getId());
        HttpUtil.post(url, params, new HttpUtil.HttpCallBack() {
            @Override
            public void onFailure(String message) {
                ToastUtils.showShortSafe(message);
            }

            @Override
            public void onSuccess(String json) {

                if (url.equals(Constants.ESSAY_COLLECT)){//收藏
                    isCollect=true;
                    title.titlePopup.cleanAction("未收藏");
                    title.addPopData(new ActionItem(getBaseContext(),"已收藏",R.drawable.reslib_ic_star));
                    ToastUtils.showShortSafe("收藏成功");
                   // setResult(RESULT_CANCELED);

                }else if (url.equals(Constants.ESSAY_UNCOLLECT)){
                    isCollect=false;
                    title.titlePopup.cleanAction("已收藏");
                    title.addPopData(new ActionItem(getBaseContext(),"未收藏",R.drawable.reslib_ic_star_border));
                    ToastUtils.showShortSafe("取消收藏");
                    //setResult(getIntent().getIntExtra(Constants.INTENT_DATA1,100));
                }
                ArticleFavoriteLiveData.getInstance().notifyInfoChanged(new ArticleFavoriteInfo(Long.parseLong(essay.getId()),
                        isCollect, -1));
            }
        });
    }

    //设置返回键动作（防止按返回键直接退出程序)
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if(keyCode== KeyEvent.KEYCODE_BACK) {
            if(webView.canGoBack()) {//当webview不是处于第一页面时，返回上一个页面
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BeautyDefine.getShareDefine(this).onActivityResultHanlder(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
