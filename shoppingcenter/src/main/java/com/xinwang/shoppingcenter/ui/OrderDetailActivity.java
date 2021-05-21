package com.xinwang.shoppingcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.xingwreslib.beautyreslibrary.BeautyObserver;
import com.xingwreslib.beautyreslibrary.OrderInfo;
import com.xingwreslib.beautyreslibrary.OrderLiveData;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.dialog.CenterDefineDialog;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.CommonEntity;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.bean.OrderBean;
import com.xinwang.shoppingcenter.bean.SkuBean;


/**
 * Date:2021/4/29
 * Time;15:02
 * author:baiguiqiang
 */
public class OrderDetailActivity extends BaseNetActivity {
    private TextView tvAddress,tvPhone,tvName,tvPrice,etRemarks,tvPay,tvCancel,tvExpress,tvCoupon,tvProductPrice,tvAdminPrice;
    private LinearLayout llContent;
    private OrderBean orderBean;
    private int totalPrice=0;
    private String goodId;//为null订单详情 ，不为null订单商品详情
    //private int goodPrice;
    private BeautyObserver beautyObserver =new BeautyObserver<OrderInfo>() {//收到状态列表刷新
        @Override
        public void beautyOnChanged(@Nullable OrderInfo o) {
            if (o.getPayState()==Constants.PAY_STATE_YES&&orderBean!=null&&orderBean.getData().getId()==o.getOrderId()){
                finish();
            }
        }
    };
    @Override
    protected int layoutResId() {
        return R.layout.activity_order_detail_shoppingcenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodId = getIntent().getStringExtra("GoodId");
        initView();
        initListener();
        initRequest();
        OrderLiveData.getInstance().beautyObserveNonStickyForever(beautyObserver);
    }

    private void initRequest() {
        requestGet(HttpUrls.URL_ORDER_DETAIL(), new ApiParams().with("order_id", getIntent().getStringExtra("id")), OrderBean.class, new HttpCallBack<OrderBean>() {
            @Override
            public void onFailure(String message) {
                requestFailureShow(message);
            }

            @Override
            public void onSuccess(OrderBean orderBean) {
                findViewById(R.id.rl_empty).setVisibility(View.GONE);
                OrderDetailActivity.this.orderBean =orderBean;
                initShow();
            }
        });
    }

    private void initShow() {
        tvAddress.setText(orderBean.getData().getAddress());
        tvName.setText(orderBean.getData().getNickname());
        tvPhone.setText(orderBean.getData().getTel());
        etRemarks.setText(orderBean.getData().getTips());
        if (orderBean.getData().getPost_price()==0){
            tvExpress.setTextColor(ContextCompat.getColor(this,R.color.textColorClassRoom));
            tvExpress.setText("+0");
        }else {
            tvExpress.setTextColor(ContextCompat.getColor(this,R.color.red));
            tvExpress.setText("+"+CountUtil.changeF2Y(orderBean.getData().getPost_price()));
        }

        tvCoupon.setText(getCouponPrice());
        initProduceList();
        tvProductPrice.setText(CountUtil.changeF2Y(totalPrice));
        if (orderBean.getData().getPay_state()== Constants.PAY_STATE_NO&&orderBean.getData().getCancel_state()==1){
            tvPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BeautyDefine.getCashierDeskDefine(OrderDetailActivity.this).
                            jumpCashierPage(orderBean.getData().getPrice()>0?
                                            CountUtil.changeF2Y(orderBean.getData().getPrice()):"0",
                                    getPayTitle(),orderBean.getData().getId()+"",null);
                }
            });
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goCancelOrder();
                }
            });
        }else if (goodPos!=-1&&orderBean.getData().getItems().get(goodPos).getWaybill_id()!=0) {
            tvPay.setText("查看物流");
            tvPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(OrderDetailActivity.this,WaybillDetailActivity.class).putExtra("id",orderBean.getData().getItems().get(goodPos).getWaybill_id()));
                }
            });
            tvCancel.setVisibility(View.GONE);
        }else {
            tvPay.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
        }

        //  if (goodId==null) {
        initPrice(orderBean.getData().getPrice());
        if (orderBean.getData().getReduction_price()!=0){
            tvAdminPrice.setText("-"+CountUtil.changeF2Y(orderBean.getData().getReduction_price()));
            findViewById(R.id.llAdminPrice).setVisibility(View.VISIBLE);
            findViewById(R.id.viewLine1).setVisibility(View.VISIBLE);
        }else {
            findViewById(R.id.llAdminPrice).setVisibility(View.GONE);
            findViewById(R.id.viewLine1).setVisibility(View.GONE);
        }
       /* }else {
            initPrice(goodPrice);
        }*/
    }
    private String getCouponPrice(){
        int price=0;
        if (orderBean.getData().getCoupons()!=null)
            for (int i=0;i<orderBean.getData().getCoupons().size();i++){
                price += orderBean.getData().getCoupons().get(i).getFee();
            }
        return "-"+CountUtil.changeF2Y(price);
    }
    private String getPayTitle(){
        StringBuffer title=new StringBuffer();
        int number =0;
        for(int j=0;j<orderBean.getData().getItems().size();j++){
            number +=orderBean.getData().getItems().get(j).getNum();
            if (j==0){
                title.append(orderBean.getData().getItems().get(j).getGoods().getTitle());
            }else {
                if (j==1) {
                    if (title.indexOf(orderBean.getData().getItems().get(j).getGoods().getTitle())==-1) {
                        title.append("、");
                        title.append(orderBean.getData().getItems().get(j).getGoods().getTitle());
                    }
                }else if (j==2){
                    title.append("...");
                }
            }
        }
        return title+" 共"+number+"件商品";
    }
    private void goCancelOrder(){
        CenterDefineDialog.getInstance("确认取消该订单嘛？").setCallback(new CenterDefineDialog.Callback1<Integer>() {
            @Override
            public void run(Integer integer) {
                requestCancelOrder();
            }
        }).showDialog(getSupportFragmentManager());
    }
    private void requestCancelOrder(){
        BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("加载中",false));
        requestPost(HttpUrls.URL_ORDER_CANCEL(), new ApiParams().with("order_id", orderBean.getData().getId()+""), CommonEntity.class, new HttpCallBack<CommonEntity>() {
            @Override
            public void onFailure(String message) {
                MyToast.myToast(OrderDetailActivity.this,message);
                BeautyDefine.getOpenPageDefine(OrderDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
            }

            @Override
            public void onSuccess(CommonEntity commonEntity) {
                BeautyDefine.getOpenPageDefine(OrderDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                MyToast.myToast(OrderDetailActivity.this,"取消成功");
                OrderLiveData.getInstance().notifyInfoChanged(new OrderInfo(orderBean.getData().getId(), Constants.PAY_STATE_CANCEL));//广播
                finish();
            }
        });
    }

    private void initPrice(int mTotal){
        SpannableString spannableString =new SpannableString("总计:￥"+(mTotal>0? CountUtil.changeF2Y(mTotal):0));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeClassRoom)),3,spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.6f),3,4,SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        if (spannableString.toString().indexOf(".")!=-1&&(spannableString.toString().indexOf(".")!=spannableString.length()-1)){
            spannableString.setSpan(new RelativeSizeSpan(0.6f),spannableString.toString().indexOf("."),spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        tvPrice.setText(spannableString);
    }
    private int goodPos=-1;
    private void initProduceList() {
        llContent.removeAllViews();
        goodPos=-1;
        int dip10 = CommentUtils.dip2px(this, 10);
        totalPrice =0;
        for (int i=0;i<orderBean.getData().getItems().size();i++){

            View itemView = LayoutInflater.from(this).inflate(R.layout.layout_order_item_shoppingcenter,llContent,false);
            ImageView icCove =itemView.findViewById(R.id.icCove);
            TextView tvTitle =itemView.findViewById(R.id.tvTitle);
            TextView tvSum =itemView.findViewById(R.id.tvSum);
            TextView tvPrice =itemView.findViewById(R.id.tvPrice);
            TextView tvSku =itemView.findViewById(R.id.tvSku);
            tvTitle.setText(orderBean.getData().getItems().get(i).getGoods().getTitle());
            tvSum.setText("×"+orderBean.getData().getItems().get(i).getNum());
            totalPrice =totalPrice +orderBean.getData().getItems().get(i).getNum()*orderBean.getData().getItems().get(i).getSku().getPrice();
            String sku = getSkus( orderBean.getData().getItems().get(i).getGoods(), orderBean.getData().getItems().get(i).getSku());
            int finalI = i;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrderDetailActivity.this, ShoppingDetailActivity.class);

                    intent.putExtra("id",orderBean.getData().getItems().get(finalI).getGoods().getId());
                    startActivity(intent);
                }
            });
            if (!TextUtils.isEmpty(sku)) {
                tvSku.setText(sku);
                tvSku.setVisibility(View.VISIBLE);
            }else {
                tvSku.setVisibility(View.INVISIBLE);
            }

            if (orderBean.getData().getItems().get(i).getSku().getPrice()==0)
                tvPrice.setText("");
            else
                tvPrice.setText(ShoppingCenterLibUtils.getPriceSpannable("￥"+CountUtil.changeF2Y(orderBean.getData().getItems().get(i).getSku().getPrice())));
            GlideUtils.loadAvatar(
                    TextUtils.isEmpty(orderBean.getData().getItems().get(i).getSku().getCover())?orderBean.getData().getItems().get(i).getGoods().getCover():orderBean.getData().getItems().get(i).getSku().getCover(),R.color.BGPressedClassRoom,icCove);

            View view = new View(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommentUtils.dip2px(this, 0.5f));
            layoutParams.setMargins(dip10, 0, 0, 0);
            view.setLayoutParams(layoutParams);
            view.setBackgroundResource(R.color.LineClassRoom);
            //  if (goodId==null) {
            llContent.addView(itemView);
            llContent.addView(view);
            //   }else {
            if (goodId!=null&&goodId.equals(orderBean.getData().getItems().get(i).getId()+"")){//该产品
                goodPos =i;
                   /* goodPrice =orderBean.getData().getItems().get(i).getNum()*orderBean.getData().getItems().get(i).getSku().getPrice();
                    llContent.addView(itemView);
                    llContent.addView(view);
                    break;*/
            }
            //    }
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
    private void initListener() {
        ((CustomToolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
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
                initRequest();
            });
    }
    private void initView() {
        tvAddress= findViewById(R.id.tvAddress);
        tvPhone= findViewById(R.id.tvPhone);
        tvName= findViewById(R.id.tvName);
        llContent= findViewById(R.id.llContent);
        etRemarks= findViewById(R.id.etRemarks);
        tvPrice= findViewById(R.id.tvPrice);
        tvCancel= findViewById(R.id.tvCancel);
        tvPay= findViewById(R.id.tvPay);
        tvExpress= findViewById(R.id.tvExpress);
        tvCoupon= findViewById(R.id.tvCoupon);
        tvProductPrice= findViewById(R.id.tvProductPrice);
        tvAdminPrice= findViewById(R.id.tvAdminPrice);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OrderLiveData.getInstance().beautyObserveNonStickyForeverRemove(beautyObserver);
    }
}
