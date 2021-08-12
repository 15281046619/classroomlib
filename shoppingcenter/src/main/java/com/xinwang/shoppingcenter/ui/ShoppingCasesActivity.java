package com.xinwang.shoppingcenter.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.bgqbaselib.view.CustomWebView;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.bean.CaseBean;

/**
 * Date:2021/7/28
 * Time;10:15
 * author:baiguiqiang
 */
public class ShoppingCasesActivity extends BaseNetActivity {
    private CustomWebView webview;
    @Override
    protected int layoutResId() {
        return R.layout.activity_shopping_cases_shoppingcenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initListener();
        initData();
    }

    private void initData() {
        requestGet(HttpUrls.URL_GOODS_CASES_DETAIL(),new ApiParams().with("id",getIntent().getStringExtra("id")), CaseBean.class, new HttpCallBack<CaseBean>() {

            @Override
            public void onFailure(String message) {
                requestFailureShow(message);
                MyToast.myToast(getApplicationContext(),message);
            }

            @Override
            public void onSuccess(CaseBean caseBean) {
                findViewById(R.id.rl_empty).setVisibility(View.GONE);
                webview.loadData(caseBean.getData().getBody());
            }
        });
    }
    private void requestFailureShow(String error){
        TextView tvMsg = findViewById(R.id.tv_msg);
        tvMsg.setText(error);
        CustomProgressBar progressbar = findViewById(R.id.progressbar);
        progressbar .setVisibility(View.GONE);
        if (!error.equals(getString(R.string.no_data_ClassRoom)))
            findViewById(R.id.rl_empty).setOnClickListener(v -> {
                progressbar.setVisibility(View.VISIBLE);
                tvMsg.setText("加载中...");
                initData();
            });
    }
    private void initListener() {
        ((CustomToolbar) findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViews() {
        webview =findViewById(R.id.webview);
    }
}
