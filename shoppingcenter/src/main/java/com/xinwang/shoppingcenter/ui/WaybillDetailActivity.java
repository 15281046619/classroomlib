package com.xinwang.shoppingcenter.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.CommonEntity;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.LogUtil;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.bean.SkuBean;
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
    private ImageView icCove;
    private CircularImage ivWaybillSrc;
    private TextView tvTitle,tvPrice,number,tvSku,tvWaybillName;
    private LinearLayout llRoot;
    private int waybillId;
    private WaybillDetailBean waybillDetailBean;
    @Override
    protected int layoutResId() {
        return R.layout.activity_waybill_detail_shoppingcenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();
        initListener();
    }

    private void initListener() {
        findViewById(R.id.tvCopy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (waybillDetailBean!=null)
                copy(waybillDetailBean.getData().getWaybill_no());
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
               requestWaybillList();
            }
        });
    }

    private void requestWaybillList() {
        ApiParams mApiParams = new ApiParams().with("waybill_no", waybillDetailBean.getData().getWaybill_no());
        if (!TextUtils.isEmpty(waybillDetailBean.getData().getType())) {
            mApiParams.with("type", waybillDetailBean.getData().getType());
        }

        requestGet(HttpUrls.URL_WAYBILL_INFO(),mApiParams , WaybillInfoBean.class, new HttpCallBack<WaybillInfoBean>() {
            @Override
            public void onFailure(String message) {
                requestFailureShow(message);
            }

            @Override
            public void onSuccess(WaybillInfoBean waybillInfoBean) {
                if (waybillInfoBean.getData().getData().size()>0){
                    findViewById(R.id.rl_empty).setVisibility(View.GONE);
                    initShow(waybillInfoBean);
                }else {
                    requestFailureShow(getString(R.string.no_data_ClassRoom));
                }
            }
        });
    }

    private void initShow(WaybillInfoBean waybillInfoBean) {
        GlideUtils.loadAvatar(TextUtils.isEmpty(waybillDetailBean.getData().getItems().get(0).getSku().getCover())?waybillDetailBean.getData().getItems().get(0).getGoods().getCover()
                :waybillDetailBean.getData().getItems().get(0).getSku().getCover(),R.color.BGPressedClassRoom,icCove);
       tvSku.setText(getSkus(waybillDetailBean.getData().getItems().get(0).getGoods(),waybillDetailBean.getData().getItems().get(0).getSku()));
      number.setText("×"+waybillDetailBean.getData().getItems().get(0).getNum());
       tvTitle.setText(waybillDetailBean.getData().getItems().get(0).getGoods().getTitle());
       tvPrice.setText(ShoppingCenterLibUtils.getPriceSpannable("￥"+ CountUtil.changeF2Y(waybillDetailBean.getData().getItems().get(0).getSku().getPrice())));
        tvWaybillName.setText(waybillInfoBean.getData().getExpTextName()+" "+waybillInfoBean.getData().getMailNo());
        GlideUtils.loadAvatar(waybillInfoBean.getData().getLogo(),ivWaybillSrc);
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
        findViewById(R.id.tvCall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone(waybillInfoBean.getData().getTel());
            }
        });
    }
    private void setContent(String desc,TextView mContactNone) {
        SpannableString mStyledText = new SpannableString(desc);
        String str = CommentUtils.getPhone(desc);
        //   mStyledText.setSpan(new ForegroundColorSpan(Color.BLUE), 7, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Pattern mPattern = Pattern.compile(str);
        Matcher mMatcher = mPattern.matcher(desc);
        boolean isPhone =false;
        while (mMatcher.find()) {
            ClickableSpan what = new ClickableSpan() {
                @Override
                public void onClick(View view) {
                    callPhone(str);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);//去掉下划线
                    ds.setColor(getResources().getColor(R.color.themeClassRoom));//设置字体颜色

                }
            };
            mStyledText.setSpan(what, mMatcher.start(), mMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mContactNone.setText(mStyledText);
            mContactNone.setMovementMethod(LinkMovementMethod.getInstance());
            mContactNone.setHighlightColor(getResources().getColor(android.R.color.transparent));
            isPhone =true;
        }
        if (!isPhone){
            mContactNone.setText(desc);
        }
    }

    private String getSkus(GoodsBean.DataBean dataBeans, SkuBean.DataBean skuBean){
        StringBuffer stringBuffer =new StringBuffer();
        for (int j = 0; j < dataBeans.getSkus().length; j++) {
            switch (j) {
                case 0:
                    stringBuffer.append(skuBean.getSku0()).append(" ");
                    break;
                case 1:
                    stringBuffer.append(skuBean.getSku1()).append(" ");
                    break;
                case 2:
                    stringBuffer.append(skuBean.getSku2()).append(" ");
                    break;
                case 3:
                    stringBuffer.append(skuBean.getSku3()).append(" ");
                    break;
                case 4:
                    stringBuffer.append(skuBean.getSku4()).append(" ");
                    break;
                case 5:
                    stringBuffer.append(skuBean.getSku5()).append(" ");
                    break;
                case 6:
                    stringBuffer.append(skuBean.getSku6()).append(" ");
                    break;
                case 7:
                    stringBuffer.append(skuBean.getSku7()).append(" ");
                    break;
                case 8:
                    stringBuffer.append(skuBean.getSku8()).append(" ");
                    break;
                case 9:
                    stringBuffer.append(skuBean.getSku9()).append(" ");
                    break;
            }

        }
        return stringBuffer.toString();
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
        icCove = findViewById(R.id.icCove);
        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        llRoot = findViewById(R.id.llRoot);

        tvSku = findViewById(R.id.tvSku);
        number = findViewById(R.id.number);
        tvWaybillName = findViewById(R.id.tvWaybillName);
        ivWaybillSrc = findViewById(R.id.ivWaybillSrc);

    }
}
