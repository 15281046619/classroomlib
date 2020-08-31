package com.xingwang.classroom.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebSettings;
import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.GoodListBean;
import com.xingwang.classroom.bean.LiveInfoBean;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.MyToast;
import com.ycbjie.webviewlib.WvWebView;
import com.ycbjie.webviewlib.X5WebUtils;
import com.ycbjie.webviewlib.X5WebView;
import com.zzhoujay.richtext.RichText;

/**
 * Date:2020/8/13
 * Time;15:24
 * author:baiguiqiang
 */
public class LiveDesFragment extends BaseLazyLoadFragment {

    protected WvWebView web_live_des;
    protected String htmlText;

    public static LiveDesFragment getInstance(String des){
        LiveDesFragment mFragment = new LiveDesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA,des);
        mFragment.setArguments(bundle);
        return mFragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live_des,container,false);

        web_live_des=view.findViewById(R.id.web_live_des);

        return view;
    }

    @Override
    public void initData() {
        htmlText=getArguments().getString(Constants.DATA);

        WebSettings webseting= web_live_des.getSettings();
        webseting.setSupportZoom(true);
        webseting.setUseWideViewPort(true);
        webseting.setBuiltInZoomControls(true);
        webseting.setLoadWithOverviewMode(true);
        webseting.setDisplayZoomControls(false);
        webseting.setJavaScriptCanOpenWindowsAutomatically(false);

        web_live_des.loadData(htmlText, "text/html;charset=utf-8", "utf-8");
    }
}
