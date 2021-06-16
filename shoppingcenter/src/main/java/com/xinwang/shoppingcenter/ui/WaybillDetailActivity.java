package com.xinwang.shoppingcenter.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.adapter.WayBillGoodListViewPagerAdapter;
import com.xinwang.shoppingcenter.bean.WaybillDetailBean;
import com.xinwang.shoppingcenter.bean.WaybillInfoBean;
import com.xinwang.shoppingcenter.view.CircularImage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date:2021/5/6
 * Time;9:24
 * author:baiguiqiang
 */
public class WaybillDetailActivity extends BaseNetActivity {

    private CircularImage ivWaybillSrc;
    private TextView tvWaybillName;
    private ViewPager viewPager;
    private LinearLayout llRoot;
    private int waybillId;
    private WaybillDetailBean waybillDetailBean;
    private LinearLayout llWaybillSum;
    private String[] mWaybillNos;
    private int curPosition=0;//当前物流号序列
    private TextView tvCopy,tvCall;
    @Override
    protected int layoutResId() {
        return R.layout.activity_waybill_detail_shoppingcenter;
    }

    /**
     *
     * @param context 必传
     * @param waybillId 必传
     * @param goodId 不传为0
     *
     * @return
     */
    public static Intent getInstance(Context context,int waybillId,int goodId){
        Intent intent =new Intent(context, WaybillDetailActivity.class);
        intent.putExtra("id",waybillId);
        if (goodId!=0){
            intent.putExtra("goodId",goodId);
        }

        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();
        initListener();
    }

    private void initListener() {
        findViewById(R.id.ivMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (waybillDetailBean!=null){
                    ShoppingCenterLibUtils.startOrderDetailActivity(WaybillDetailActivity.this,waybillDetailBean.getData().getItems().get(0).getOrder_id()+"");
                }
            }
        });
        ((CustomToolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
    }
    //复制
    private void copy(String data) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）,其他的还有
        // newHtmlText、
        // newIntent、
        // newUri、
        // newRawUri
        ClipData clipData = ClipData.newPlainText(null, data);
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
        MyToast.myToast(this,"已复制");

    }

    private void initData() {
        waybillId = getIntent().getIntExtra("id",0);
        if (waybillId!=0) {
            requestWaybillDetail();
        }else {
            requestFailureShow("没有相关物流信息");
        }
    }

    private void requestWaybillDetail() {
        requestGet(HttpUrls.URL_WAYBILL_DETAIL(), new ApiParams().with("id", waybillId), WaybillDetailBean.class, new HttpCallBack<WaybillDetailBean>() {
            @Override
            public void onFailure(String message) {
                requestFailureShow(message);
            }

            @Override
            public void onSuccess(WaybillDetailBean waybillDetailBean) {
                WaybillDetailActivity.this.waybillDetailBean =waybillDetailBean;
                mWaybillNos = waybillDetailBean.getData().getWaybill_no().split(",");
                if (mWaybillNos.length>1){
                    for (int i=0;i<mWaybillNos.length;i++){
                        TextView textView =new TextView(WaybillDetailActivity.this);
                        textView.setPadding(CommentUtils.dip2px(WaybillDetailActivity.this,20),CommentUtils.dip2px(WaybillDetailActivity.this,10),
                                CommentUtils.dip2px(WaybillDetailActivity.this,20),CommentUtils.dip2px(WaybillDetailActivity.this,10));
                        textView.setText("物流"+(i+1));
                        if (curPosition==i){
                            textView.setTextColor(ContextCompat.getColor(WaybillDetailActivity.this,R.color.themeClassRoom));
                        }else {
                            textView.setTextColor(ContextCompat.getColor(WaybillDetailActivity.this,R.color.textColorClassRoom));
                        }
                        int finalI = i;
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((TextView)llWaybillSum.getChildAt(curPosition)).setTextColor(ContextCompat.getColor(WaybillDetailActivity.this,R.color.textColorClassRoom));
                                ((TextView)llWaybillSum.getChildAt(finalI)).setTextColor(ContextCompat.getColor(WaybillDetailActivity.this,R.color.themeClassRoom));
                                curPosition = finalI;
                                requestWaybillList(false);
                            }
                        });
                        llWaybillSum.addView(textView);
                    }
                }
                requestWaybillList(true);
            }
        });
    }

    private void requestWaybillList(boolean isFrist) {
        ApiParams mApiParams = new ApiParams().with("waybill_no", mWaybillNos[curPosition]);
        if (!TextUtils.isEmpty(waybillDetailBean.getData().getType())) {
            mApiParams.with("type", waybillDetailBean.getData().getType());
        }
        if (!isFrist)
        BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("加载中",false));
        requestGet(HttpUrls.URL_WAYBILL_INFO(),mApiParams , WaybillInfoBean.class, new HttpCallBack<WaybillInfoBean>() {
            @Override
            public void onFailure(String message) {
                requestFailureShow(message);
                if (!isFrist)
                BeautyDefine.getOpenPageDefine(WaybillDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
            }

            @Override
            public void onSuccess(WaybillInfoBean waybillInfoBean) {
                if (!isFrist)
                    BeautyDefine.getOpenPageDefine(WaybillDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                if (waybillInfoBean.getData()!=null&&waybillInfoBean.getData().getData()!=null&&waybillInfoBean.getData().getData().size()>0){
                    findViewById(R.id.rl_empty).setVisibility(View.GONE);
                    if (isFrist){
                        initDetail();
                    }
                    initShow(waybillInfoBean);
                }else {
                    requestFailureShow(getString(R.string.no_data_ClassRoom));
                }
            }
        });
    }
    private void initDetail(){
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(new WayBillGoodListViewPagerAdapter(this,waybillDetailBean.getData().getItems()));
        setViewPagerPos();

    }

    private void setViewPagerPos() {
        int gooId= getIntent().getIntExtra("goodId",0);

        if (gooId!=0){
            for (int i=0;i<waybillDetailBean.getData().getItems().size();i++){
                if (waybillDetailBean.getData().getItems().get(i).getId()==gooId){
                    viewPager.setCurrentItem(i);
                    break;
                }
            }
        }
    }

    private void initShow(WaybillInfoBean waybillInfoBean) {
        tvWaybillName.setText(waybillInfoBean.getData().getExpTextName()+" "+waybillInfoBean.getData().getMailNo());
        GlideUtils.loadAvatar(waybillInfoBean.getData().getLogo(),ivWaybillSrc);
        llRoot.removeAllViews();
        for (int i=0;i<waybillInfoBean.getData().getData().size();i++){
            View view =LayoutInflater.from(this).inflate(R.layout.item_waybill_detail_shoppingcenter,llRoot,false);
            TextView tvTime= view.findViewById(R.id.tvTime);
            TextView tvContent = view.findViewById(R.id.tvContent);
            tvTime.setText(waybillInfoBean.getData().getData().get(i).getTime());
            setContent(waybillInfoBean.getData().getData().get(i).getContext(),tvContent);
            if (i==0){
                view.findViewById(R.id.view1).setVisibility(View.INVISIBLE);
            }else if (i==waybillInfoBean.getData().getData().size()-1){
                view.findViewById(R.id.view2).setVisibility(View.INVISIBLE);
            }
            llRoot.addView(view);
        }
        tvCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(waybillInfoBean.getData().getTel());
            }
        });
        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (waybillInfoBean.getData()!=null)
                    copy(waybillInfoBean.getData().getMailNo());
            }
        });
    }
    private void setContent(String desc,TextView mContactNone) {
        SpannableString mStyledText = new SpannableString(desc);
        Pattern mPattern = Pattern.compile("((1|861)(3|4|5|7|8)\\d{9}$*)|((0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?))");

        Matcher mMatcher = mPattern.matcher(desc);
        boolean isPhone =false;
        while (mMatcher.find()) {
            String str= mMatcher.group();
            ClickableSpan what = new ClickableSpan() {
                @Override
                public void onClick(View view) {

                    if (!TextUtils.isEmpty(str))
                        callPhone(str);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);//去掉下划线
                    ds.setColor(getResources().getColor(R.color.themeClassRoom));//设置字体颜色

                }
            };
            if (mMatcher.start()<mMatcher.end()) {
                mStyledText.setSpan(what, mMatcher.start(), mMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }

            isPhone =true;
        }
        if (!isPhone){
            mContactNone.setText(desc);
        }else {
            mContactNone.setText(mStyledText);
            mContactNone.setMovementMethod(LinkMovementMethod.getInstance());
            mContactNone.setHighlightColor(getResources().getColor(android.R.color.transparent));
        }
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
                requestWaybillDetail();
            });
    }

    public void callPhone(String phoneNum){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    private void initViews() {

        llRoot = findViewById(R.id.llRoot);
        tvCopy = findViewById(R.id.tvCopy);
        tvCall = findViewById(R.id.tvCall);
        llWaybillSum = findViewById(R.id.llWaybillSum);
        tvWaybillName = findViewById(R.id.tvWaybillName);
        ivWaybillSrc = findViewById(R.id.ivWaybillSrc);
        viewPager = findViewById(R.id.viewPager);

    }
}
