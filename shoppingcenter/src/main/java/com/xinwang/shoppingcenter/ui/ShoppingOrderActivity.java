package com.xinwang.shoppingcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.DeliveryaddrCallBack;
import com.beautydefinelibrary.DeliveryaddrDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.sku.bean.Sku;
import com.xinwang.bgqbaselib.sku.bean.SkuAttribute;
import com.xinwang.bgqbaselib.utils.AndroidBug5497Workaround;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.LogUtil;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.CouponBean;
import com.xinwang.shoppingcenter.bean.ErpBean;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单确定页面
 * Date:2021/4/9
 * Time;14:26
 * author:baiguiqiang
 */
public class ShoppingOrderActivity extends BaseNetActivity {
    private TextView tvAdd,tvAddress,tvPhone,tvName,tvSum,tvPrice;
    private RadioButton rbXX,rbWX,rbZFB;
    private LinearLayout llContent;
    private TextView tvCoupon;
    private NestedScrollView scrollview;
    private EditText etRemarks;
    private Double aDoublePrice;
    private int selectSum;
    private List<Sku> skuList =new ArrayList<>();

    private CouponBean.DataBean.CouponsBean couponsBean;//当前选中的优惠劵
    private int couponPos =-1;
    private int totalCoupon=0;
    private   DeliveryaddrDefine mADDrDefine;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidBug5497Workaround.assistActivity(this);
        initView();
        initListener();
        initData();
    }

    private void initData() {
        skuList = getIntent().getParcelableArrayListExtra("data");
        initShowAddress();
        initShowGoods();
        initCoupon();
    }

    private void initCoupon() {
        requestGet(HttpUrls.URL_COUPON_LISTS(), new ApiParams().with("type", 2).with("page", 1).with("page_num", 1),
                CouponBean.class, new HttpCallBack<CouponBean>() {
                    @Override
                    public void onFailure(String message) {
                        MyToast.myToast(ShoppingOrderActivity.this,message);
                    }

                    @Override
                    public void onSuccess(CouponBean commonEntity) {
                        if (commonEntity.getData()!=null&&commonEntity.getData().getCoupons().size()>0) {
                            totalCoupon =commonEntity.getData().getTotal();
                            tvCoupon.setText(totalCoupon+"张可用");
                        }
                    }
                });
    }



    /**
     * 商品清单
     */
    private void initShowGoods() {
        llContent.removeAllViews();
        aDoublePrice= 0D;
        selectSum =0;
        int dip10 = CommentUtils.dip2px(this, 10);
        for (int i=0;i<skuList.size();i++){
            if (!TextUtils.isEmpty(skuList.get(i).getSellingPrice()))
                aDoublePrice = CountUtil.add(aDoublePrice , CountUtil.multiply(skuList.get(i).getAddSum(),Double.parseDouble(skuList.get(i).getSellingPrice())));
            selectSum+=skuList.get(i).getAddSum();

            View itemView = LayoutInflater.from(this).inflate(R.layout.layout_shopping_item_shoppingcenter,llContent,false);
            ImageView icCove =itemView.findViewById(R.id.icCove);
            TextView tvTitle =itemView.findViewById(R.id.tvTitle);
            TextView tvSub =itemView.findViewById(R.id.tvSub);
            TextView tvSum =itemView.findViewById(R.id.tvSum);
            TextView tvAdd =itemView.findViewById(R.id.tvAdd);
            TextView tvPrice =itemView.findViewById(R.id.tvPrice);
            TextView tvSku =itemView.findViewById(R.id.tvSku);

            tvTitle.setText(skuList.get(i).getGoodTitle());
            int finalI = i;
            tvAdd.setOnClickListener(v -> {
                if (skuList.get(finalI).getAddSum()<skuList.get(finalI).getStockQuantity()) {
                    if (skuList.get(finalI).getAddSum()>=skuList.get(0).getMaxBugSum()){
                        MyToast.myToast(this,"超出个人限购数目");
                        return;
                    }
                    skuList.get(finalI).setAddSum(skuList.get(finalI).getAddSum() + 1);
                    tvSum.setText(String.valueOf(skuList.get(finalI).getAddSum()));
                    if (!TextUtils.isEmpty(skuList.get(finalI).getSellingPrice()))
                        aDoublePrice = CountUtil.add(aDoublePrice, Double.parseDouble(skuList.get(finalI).getSellingPrice()));
                    selectSum = selectSum + 1;
                    showTotalPrice();
                }else {
                    MyToast.myToast(  this,"超出库存数目");
                }
            });
            tvSum.setText(String.valueOf(skuList.get(i).getAddSum()));
            tvSku.setText("");
            if (skuList.get(i).getAttributes()!=null&&skuList.get(i).getAttributes().size()>0) {
                for (SkuAttribute skuAttribute : skuList.get(i).getAttributes()) {
                    tvSku.append(skuAttribute.getValue() + " ");
                }
                tvSku.setVisibility(View.VISIBLE);
            }else {
                tvSku.setVisibility(View.INVISIBLE);
            }

            if (TextUtils.isEmpty(skuList.get(i).getSellingPrice()))
                tvPrice.setText("");
            else
                tvPrice.setText(ShoppingCenterLibUtils.getPriceSpannable("￥"+CountUtil.doubleToString(skuList.get(i).getSellingPrice())));
            tvSub.setOnClickListener(v -> {
                if (skuList.get(finalI).getAddSum()>1){
                    skuList.get(finalI).setAddSum(skuList.get(finalI).getAddSum()-1);
                    tvSum.setText(String.valueOf(skuList.get(finalI).getAddSum()));
                    if (!TextUtils.isEmpty(skuList.get(finalI).getSellingPrice()))
                        aDoublePrice = CountUtil.sub(aDoublePrice ,Double.parseDouble(skuList.get(finalI).getSellingPrice()));
                    selectSum = selectSum - 1;
                    showTotalPrice();

                }else {
                    MyToast.myToast( this,"数值低于范围");

                }
            });
            GlideUtils.loadAvatar(skuList.get(i).getMainImage(),R.color.BGPressedClassRoom,icCove);

            llContent.addView(itemView);
            View view =new View(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CommentUtils.dip2px(this, 0.5f));
            layoutParams.setMargins(dip10,0,0,0);
            view.setLayoutParams(layoutParams);
            view.setBackgroundResource(R.color.LineClassRoom);
            llContent.addView(view);
        }
        showTotalPrice();
    }

    public void showTotalPrice(){
        Double mTotal = CountUtil.sub(aDoublePrice, couponsBean == null ? 0D :
                CountUtil.divide(couponsBean.getFee(), 100D));
        SpannableString spannableString =new SpannableString("总计:￥"+(mTotal>0?CountUtil.doubleToString(mTotal):0));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeClassRoom)),3,spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.6f),3,4,SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        if (spannableString.toString().indexOf(".")!=-1&&(spannableString.toString().indexOf(".")!=spannableString.length()-1)){
            spannableString.setSpan(new RelativeSizeSpan(0.6f),spannableString.toString().indexOf("."),spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        tvPrice.setText(spannableString);
        if (selectSum>0) {
            tvSum.setText("共" + selectSum + "件，\n");
           /* if(couponsBean!=null){
                tvSum.append("优惠￥"+CountUtil.doubleToString( CountUtil.divide(couponsBean.getFee(),100D)));
            }*/
        }

    }
    private void initShowAddress() {
        String json = BeautyDefine.getUserInfoDefine(this).getDeliveryaddr();
        LogUtil.i(json);
        try {
            JSONObject jsonObject =new JSONObject(json);
            int defaultIndex = jsonObject.getInt("defaultIndex");
            JSONArray jsonArray = jsonObject.getJSONArray("deliveryaddrs");
            if (defaultIndex<0||jsonArray.length()<=defaultIndex)//没有默认地址属性,此处默认选择第一个地址
                defaultIndex=jsonArray.length()-1;
            tvAdd.setVisibility(View.GONE);
            tvAddress.setText( jsonArray.getJSONObject(defaultIndex).getString("accurateAddress"));
            tvPhone.setText( jsonArray.getJSONObject(defaultIndex).getString("phone"));
            tvName.setText( jsonArray.getJSONObject(defaultIndex).getString("consignee"));

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtil.i("error:"+e);
            tvAdd.setVisibility(View.VISIBLE);
        }

    }

    private void initListener() {
        ((RadioGroup)findViewById(R.id.radioGroup)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId!=R.id.rbXX){
                    rbXX.setChecked(true);
                }
            }
        });
        ((CustomToolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
        rbWX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.myToast(ShoppingOrderActivity.this,"暂未开通");
            }
        });
        rbZFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.myToast(ShoppingOrderActivity.this,"暂未开通");
            }
        });
        findViewById(R.id.cdAddress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etRemarks.clearFocus();
                /*mDialog = CenterBuyDialog.getInstance("收货地址","请准确的填写收货信息");
                mDialog.setCallback(new CenterBuyDialog.Callback1<String>() {
                    @Override
                    public void run(String s) {
                        initShowAddress();
                    }

                    @Override
                    public void clickLocation() {
                        getPersimmions();
                    }
                }).showDialog(getSupportFragmentManager());*/
                mADDrDefine = BeautyDefine.getDeliveryaddrDefine(ShoppingOrderActivity.this);
                mADDrDefine.showDeliveryaddr(tvAdd.getVisibility() != View.GONE, true, new DeliveryaddrCallBack() {
                    @Override
                    public void edited() {

                    }

                    @Override
                    public void selected(String s) {

                        try {
                            LogUtil.i(s);
                            JSONObject jsonObject = new JSONObject(s);
                            tvAddress.setText(jsonObject.getString("accurateAddress"));
                            tvPhone.setText(jsonObject.getString("phone"));
                            tvName.setText(jsonObject.getString("consignee"));
                            tvAdd.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            LogUtil.i("error:" + e);
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
        findViewById(R.id.tvBuy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvAdd.getVisibility()==View.GONE) {
                    goRequestErp();
                }else {
                    scrollview.smoothScrollTo(0,0);
                    MyToast.myToast(ShoppingOrderActivity.this,"请填写收货地址");
                }
            }
        });
        findViewById(R.id.llCoupon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ShoppingOrderActivity.this,CouponListsActivity.class)
                        .putExtra("pos",couponPos),100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mADDrDefine!= null) {
            mADDrDefine.onActivityResultHanlder(requestCode,resultCode,data);
        }
        if (requestCode==100&&resultCode==100){
            if (data==null){//取消优惠劵
                couponsBean =null;
                tvCoupon.setText(totalCoupon+"张可用");
                couponPos =-1;
            }else {// 选中优惠劵
                couponsBean = (CouponBean.DataBean.CouponsBean) data.getSerializableExtra("data");
                tvCoupon.setText(ShoppingCenterLibUtils.getPriceSpannableSub("-￥"+CountUtil.doubleToString(CountUtil.divide(couponsBean.getFee(),100D))));
                couponPos =data.getIntExtra("pos",-1);
            }
            showTotalPrice();
        }else if (requestCode==1){
            initShowAddress();
        }
    }
   /* @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList();
            *//***
     * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
     *//*
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            *//*
     * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
     *//*

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 100);
            }else {
                if (mDialog!=null)
                    mDialog.goLocationAddress();
            }
        }else {
            if (mDialog!=null)
                mDialog.goLocationAddress();
        }
    }*/

/*    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==100){
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    MyToast.myToast(getApplicationContext(), "你拒绝了该权限");
                    return;
                }
            }
            if (mDialog!=null)
                mDialog.goLocationAddress();
        }
    }*/

    /**
     * 获取技术老师
     */
    private void goRequestErp() {
        BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("加载中",false));
        requestGet(HttpUrls.URL_USER_MY_ERP(),new ApiParams(), ErpBean.class, new HttpCallBack<ErpBean>() {

            @Override
            public void onFailure(String message) {
                MyToast.myToast(getApplicationContext(),message);
                BeautyDefine.getOpenPageDefine(ShoppingOrderActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
            }

            @Override
            public void onSuccess(ErpBean erpBean) {
                BeautyDefine.getOpenPageDefine(ShoppingOrderActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                ShoppingCenterLibUtils.jumpChat(ShoppingOrderActivity.this,erpBean.getData()==null?-1:erpBean.getData().getId(),
                        getContent());
                finish();
            }
        });
    }

    private String getContent() {
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("姓名："+tvName.getText().toString()+"\n");
        stringBuffer.append("电话："+tvPhone.getText().toString()+"\n");
        stringBuffer.append("地址："+tvAddress.getText().toString()+"\n");
        for (int i=0;i<skuList.size();i++) {
            stringBuffer.append(skuList.get(i).getGoodTitle() + "：" );
            if (skuList.get(i).getAttributes()!=null&&skuList.get(i).getAttributes().size()>0) {
                for (SkuAttribute skuAttribute : skuList.get(i).getAttributes()) {
                    stringBuffer.append(skuAttribute.getValue() + " ");
                }
            }
            stringBuffer.append("×"+skuList.get(i).getAddSum());
            if (i!=skuList.size()-1){
                stringBuffer.append("\n");
            }
        }
        if (!TextUtils.isEmpty(etRemarks.getText().toString())){
            stringBuffer.append("\n备注："+etRemarks.getText().toString());
        }
        if (couponsBean!=null){
            stringBuffer.append("\n优惠劵："+couponsBean.getName()+" id="+couponsBean.getId());
        }
        return stringBuffer.toString();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    private void initView() {
        tvAdd= findViewById(R.id.tvAdd);
        tvAddress= findViewById(R.id.tvAddress);
        tvPhone= findViewById(R.id.tvPhone);
        tvName= findViewById(R.id.tvName);
        rbXX= findViewById(R.id.rbXX);
        rbZFB= findViewById(R.id.rbZFB);
        rbWX= findViewById(R.id.rbWX);
        llContent= findViewById(R.id.llContent);
        etRemarks= findViewById(R.id.etRemarks);
        tvPrice= findViewById(R.id.tvPrice);
        tvSum= findViewById(R.id.tvSum);
        tvCoupon =findViewById(R.id.tvCoupon);
        scrollview =findViewById(R.id.scrollview);

    }

    @Override
    protected int layoutResId() {
        return R.layout.activity_shopping_order_shoppingcneter;
    }
}
