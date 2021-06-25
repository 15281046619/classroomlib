package com.xinwang.shoppingcenter.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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
import com.xingwreslib.beautyreslibrary.OrderInfo;
import com.xingwreslib.beautyreslibrary.OrderLiveData;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.dialog.CenterDefineDialog;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.sku.bean.Sku;
import com.xinwang.bgqbaselib.sku.bean.SkuAttribute;
import com.xinwang.bgqbaselib.utils.AndroidBug5497Workaround;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.AddressBean;
import com.xinwang.shoppingcenter.bean.CouponBean;
import com.xinwang.shoppingcenter.bean.ErpBean;
import com.xinwang.shoppingcenter.bean.NumberBean;
import com.xinwang.shoppingcenter.bean.OrderSuccessBean;
import com.xinwang.shoppingcenter.dialog.CenterEditNumberDialog;


import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 订单确定页面
 * Date:2021/4/9
 * Time;14:26
 * author:baiguiqiang
 */
public class ShoppingOrderActivity extends BaseNetActivity {
    private TextView tvAdd,tvAddress,tvPhone,tvName,tvSum,tvPrice,tvExpress,tvProductPrice;
    private RadioButton rbXX,rbWX,rbZFB;
    private LinearLayout llContent;
    private TextView tvCoupon;
    private NestedScrollView scrollview;
    private EditText etRemarks;
    private int aDoublePrice;
    private int selectSum;
    private List<Sku> skuList =new ArrayList<>();

    private CouponBean.DataBean.CouponsBean couponsBean;//当前选中的优惠劵
    private int couponPos =-1;
    private int totalCoupon=0;
    private DeliveryaddrDefine mADDrDefine;
    private int expressPrice=0;//邮费价格

    public static Intent getIntent(Context context, String body, int resultCode){
        Intent intent = new Intent(context, ShoppingOrderActivity.class);
        ArrayList<Sku> mData = (ArrayList<Sku>) GsonUtils.changeGsonToSafeList(body, Sku.class);
        intent.putParcelableArrayListExtra("data",  mData);
        intent.putExtra("resultCode",resultCode);
        return intent;
    }
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
                            tvCoupon.setText(totalCoupon+"张未使用");
                        }
                    }
                });
    }



    /**
     * 商品清单
     */
    private void initShowGoods() {
        llContent.removeAllViews();
        aDoublePrice= 0;
        selectSum =0;
        int dip10 = CommentUtils.dip2px(this, 10);
        for (int i=0;i<skuList.size();i++){
            if (skuList.get(i).getSellingPrice()!=0)
                aDoublePrice = aDoublePrice + skuList.get(i).getAddSum()*skuList.get(i).getSellingPrice();
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
                    if (skuList.get(finalI).getSellingPrice()!=0)
                        aDoublePrice = aDoublePrice+ skuList.get(finalI).getSellingPrice();
                    selectSum = selectSum + 1;
                    showTotalPrice();
                }else {
                    MyToast.myToast(  this,"超出库存数目");
                }
            });
            tvSum.setText(String.valueOf(skuList.get(i).getAddSum()));
            tvSum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CenterEditNumberDialog.getInstance(skuList.get(0).getMaxBugSum(),skuList.get(finalI).getStockQuantity(),tvSum.getText().toString())
                            .setCallback(new CenterEditNumberDialog.Callback1<Integer>() {
                                @Override
                                public void run(Integer integer) {

                                    int curNumber =Integer.parseInt(tvSum.getText().toString());
                                    if (curNumber!=integer){
                                        if (couponsBean!=null){
                                            couponsBean =null;
                                            tvCoupon.setText(totalCoupon+"张未使用");
                                            couponPos =-1;
                                        }
                                        int countNumber =integer-curNumber;
                                        int countPrice = integer * skuList.get(finalI).getSellingPrice()-curNumber * skuList.get(finalI).getSellingPrice() ;
                                        skuList.get(finalI).setAddSum(integer);
                                        tvSum.setText(String.valueOf(integer));
                                        aDoublePrice = aDoublePrice+ countPrice;
                                        selectSum = selectSum + countNumber;
                                        showTotalPrice();
                                    }
                                }
                            }).showDialog(getSupportFragmentManager());
                }
            });

            tvSku.setText("");
            if (skuList.get(i).getAttributes()!=null&&skuList.get(i).getAttributes().size()>0) {
                for (SkuAttribute skuAttribute : skuList.get(i).getAttributes()) {
                    tvSku.append(skuAttribute.getValue() + " ");
                }
                tvSku.setVisibility(View.VISIBLE);
            }else {
                tvSku.setVisibility(View.INVISIBLE);
            }

            if (skuList.get(i).getSellingPrice()==0)
                tvPrice.setText("");
            else
                tvPrice.setText(ShoppingCenterLibUtils.getPriceSpannable("￥"+CountUtil.changeF2Y(skuList.get(i).getSellingPrice())));
            tvSub.setOnClickListener(v -> {

                if (skuList.get(finalI).getAddSum()>1){
                    if (couponsBean!=null){
                        couponsBean =null;
                        tvCoupon.setText(totalCoupon+"张未使用");
                        couponPos =-1;
                    }
                    skuList.get(finalI).setAddSum(skuList.get(finalI).getAddSum()-1);
                    tvSum.setText(String.valueOf(skuList.get(finalI).getAddSum()));
                    if (skuList.get(finalI).getSellingPrice()!=0)
                        aDoublePrice = aDoublePrice -skuList.get(finalI).getSellingPrice();
                    selectSum = selectSum - 1;
                    showTotalPrice();
                }else {
                    MyToast.myToast(this, "数值低于范围");
                 /*   if (isEdit) {
                        if (couponsBean != null) {
                            couponsBean = null;
                            tvCoupon.setText(totalCoupon + "张未使用");
                            couponPos = -1;
                        }
                        skuList.remove(finalI);
                        initShowGoods();
                    }else {

                    }*/
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
    private int mTotal;
    public void showTotalPrice(){
        countExpress();//计算邮费
        mTotal = aDoublePrice - (couponsBean == null ? 0:couponsBean.getFee())+expressPrice;

        SpannableString spannableString =new SpannableString("总计:￥"+(mTotal>0?CountUtil.changeF2Y(mTotal):0));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.themeClassRoom)),3,spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.6f),3,4,SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        if (spannableString.toString().indexOf(".")!=-1&&(spannableString.toString().indexOf(".")!=spannableString.length()-1)){
            spannableString.setSpan(new RelativeSizeSpan(0.6f),spannableString.toString().indexOf("."),spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        tvProductPrice.setText(CountUtil.changeF2Y(aDoublePrice));
        tvPrice.setText(spannableString);
        if (selectSum>0) {
            tvSum.setText("共" + selectSum + "件，\n");
        }

    }

    private void initShowAddress() {
        requestGet(HttpUrls.URL_EXTRA_GET(), new ApiParams().with("names", "deliveryaddrs"), AddressBean.class, new HttpCallBack<AddressBean>() {
            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onSuccess(AddressBean commonEntity) {
                if (selectData==null)
                    addressGetSuccess(GsonUtils.getGsonInstance().fromJson(commonEntity.getData().getDeliveryaddrs(),AddressBean.DataBean2.class));
            }
        });
    }
    private String selectData =null;
    private String regions ="";//地区json [{"name":"辽宁省","provinceId":"210000000000"},{"city_id":"210500000000","name":"本溪市","province_id":"210000000000"},{"city_id":"210500000000","county_id":"210521000000","name":"本溪满族自治县"}]","phone":"18482131033"},{"accurateAddress":"华西大厦A座 1011","city":"成都","phone":"18482131033"}]
    private void addressGetSuccess(AddressBean.DataBean2 dataBean){
        if (dataBean!=null&&dataBean.getDeliveryaddrs().size()>0) {
            tvAdd.setVisibility(View.GONE);
            int defaultIndex =dataBean.getDeliveryaddrs().size()-1;
            for (int i=0;i<dataBean.getDeliveryaddrs().size();i++){
                if (dataBean.getDefaultId().equals(dataBean.getDeliveryaddrs().get(i).getId())){
                    defaultIndex =i;
                    break;
                }
            }
            regions =dataBean.getDeliveryaddrs().get(defaultIndex).getRegions();
            showAddress(dataBean.getDeliveryaddrs().get(defaultIndex).getAccurateAddress());
            tvPhone.setText(dataBean.getDeliveryaddrs().get(defaultIndex).getPhone());
            tvName.setText(dataBean.getDeliveryaddrs().get(defaultIndex).getConsignee());
        }else {
            tvAddress.setText("");
            tvPhone.setText("");
            tvName.setText("");
            regions ="";
            tvAdd.setVisibility(View.VISIBLE);
        }
        showTotalPrice();
    }
    private void initListener() {
        ((RadioGroup)findViewById(R.id.radioGroup)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId!=R.id.rbWX){
                    rbWX.setChecked(true);
                }
            }
        });
        ((CustomToolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(v -> finish());
    /*    rbWX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyToast.myToast(ShoppingOrderActivity.this,"暂未开通");
            }
        });*/
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
                selectData =null;
                mADDrDefine = BeautyDefine.getDeliveryaddrDefine(ShoppingOrderActivity.this);
                mADDrDefine.showDeliveryaddr(tvAdd.getVisibility() != View.GONE, true, new DeliveryaddrCallBack() {
                    @Override
                    public void edited(String data) {
                        if (data != null) {
                            AddressBean.DataBean2 mData = GsonUtils.getGsonInstance().fromJson(data, AddressBean.DataBean2.class);
                            addressGetSuccess(mData);
                        }else {
                            initShowAddress();
                        }

                    }
                    @Override
                    public void selected(String s) {
                        try {
                            selectData =s;
                            JSONObject jsonObject = new JSONObject(s);
                            regions =jsonObject.getString("regions");
                            showAddress(jsonObject.getString("accurateAddress"));

                            tvPhone.setText(jsonObject.getString("phone"));
                            tvName.setText(jsonObject.getString("consignee"));
                            tvAdd.setVisibility(View.GONE);
                            showTotalPrice();
                        } catch (JSONException e) {
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
                    createOrder();
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
                        .putExtra("pos",couponPos)
                        .putExtra("price",aDoublePrice)
                        .putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) skuList),100);
            }
        });
        findViewById(R.id.llExpress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CenterDefineDialog.getInstance("1.包邮条件：\n   海南  新疆 西藏  内蒙  青海 宁夏满500包邮 \n   其他地方满300包邮\n2.邮费价格：\n   四川重庆10元\n   其他地区全都15元").showDialog(getSupportFragmentManager());
            }
        });
    }
    private void showAddress(String address){
        try {
            JSONArray jsonArray =new JSONArray(regions);
            tvAddress.setText(jsonArray.getJSONObject(0).getString("name")+" "+jsonArray.getJSONObject(1).getString("name")
                    +" "+jsonArray.getJSONObject(2).getString("name")+" "+address);
            //  apiParams.with("area_code", jsonArray.getJSONObject(2).getString("county_id").substring(0,6));
        }catch (JSONException e) {
            e.printStackTrace();
        }

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
                tvCoupon.setText(totalCoupon+"张未使用");
                couponPos =-1;
            }else {// 选中优惠劵
                couponsBean = (CouponBean.DataBean.CouponsBean) data.getSerializableExtra("data");
                tvCoupon.setText(ShoppingCenterLibUtils.getPriceSpannableSub("-￥"+CountUtil.doubleToString(CountUtil.changeF2Y(couponsBean.getFee()))));
                couponPos =data.getIntExtra("pos",-1);
            }
            showTotalPrice();
        }else if (requestCode==1){
            initShowAddress();
        }
    }




    /**
     * 创建订单
     */
    private void createOrder(){

        BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("加载中",false));

        ApiParams apiParams =new ApiParams();
        if (!TextUtils.isEmpty(etRemarks.getText().toString()))
            apiParams.with("tips",etRemarks.getText().toString());
        if (couponsBean!=null)
            apiParams.with("coupon_ids",couponsBean.getId()+"");
        apiParams.with("nickname",tvName.getText().toString());
        apiParams.with("tel",tvPhone.getText().toString());
        apiParams.with("address",tvAddress.getText().toString());
        try {
            JSONArray jsonArray =new JSONArray(regions);
            apiParams.with("area_code", jsonArray.getJSONObject(2).getString("county_id").substring(0,6));
        }catch (JSONException e) {
            e.printStackTrace();
        }
        apiParams.with("items",getItems());
        requestPost(HttpUrls.URL_USER_ORDER_CREATE(),apiParams, OrderSuccessBean.class, new HttpCallBack<OrderSuccessBean>() {

            @Override
            public void onFailure(String message) {
                MyToast.myToast(getApplicationContext(),message);
                BeautyDefine.getOpenPageDefine(ShoppingOrderActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
            }

            @Override
            public void onSuccess(OrderSuccessBean orderSuccessBean) {
                if (getIntent().getBooleanExtra("isShoppingCenter",false))
                    removeOrderData();//只有购物车里面进入才移除
                OrderLiveData.getInstance().notifyInfoChanged(new OrderInfo(orderSuccessBean.getData().getOrder_id(), Constants.PAY_STATE_NO));//广播
                BeautyDefine.getOpenPageDefine(ShoppingOrderActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                //跳转支付
                getTitle();
                BeautyDefine.getCashierDeskDefine(ShoppingOrderActivity.this).jumpCashierPage(mTotal>0?CountUtil.changeF2Y(mTotal):"0",
                        getPayTitle(),orderSuccessBean.getData().getOrder_id()+"",null);

                finish();
            }
        });
    }
    private String getPayTitle(){
        StringBuffer title=new StringBuffer();
        for(int j=0;j<skuList.size();j++){

            if (j==0){
                title.append(skuList.get(j).getGoodTitle());
            }else {
                if (j==1) {
                    if (title.indexOf(skuList.get(j).getGoodTitle())==-1) {
                        title.append("、");
                        title.append(skuList.get(j).getGoodTitle());
                    }
                }else if (j==2) {
                    title.append("...");
                }
            }
        }
        return title+" 共"+selectSum+"件商品";
    }
    private void removeOrderData() {
        String saveGoods = SharedPreferenceUntils.getGoods(this);
        if (!TextUtils.isEmpty(saveGoods)){
            List<Sku> mLists = GsonUtils.changeGsonToSafeList(saveGoods, Sku.class);
            List<Sku> saveDate=new ArrayList<>();
            for (int i=0;i<mLists.size();i++){
                for (int j=0;j<skuList.size();j++){
                    if ((skuList.get(j).getGoodId()==mLists.get(i).getGoodId())&&(mLists.get(i).getId()==null?(skuList.get(j).getId()==null):(
                            mLists.get(i).getId().equals(skuList.get(j).getId())))){
                        break;
                    }
                    if (j==skuList.size()-1){
                        saveDate.add(mLists.get(i));
                    }
                }
            }
            EventBus.getDefault().post(new NumberBean(-skuList.size()));
            SharedPreferenceUntils.saveGoods(this,GsonUtils.createGsonString(saveDate));
        }


    }

    private String getItems(){
        HashMap<String,Integer> map =new HashMap<>();
        for (int i=0;i<skuList.size();i++) {
            map.put(skuList.get(i).getId(),skuList.get(i).getAddSum());
        }
        return GsonUtils.createGsonString(map);
    }

    private String getContent() {
        StringBuffer stringBuffer =new StringBuffer();
        stringBuffer.append("姓名："+tvName.getText().toString()+"\n");
        stringBuffer.append("电话："+tvPhone.getText().toString()+"\n");
        stringBuffer.append("地址："+tvAddress.getText().toString()+"\n");
        stringBuffer.append("注册手机号码："+BeautyDefine.getUserInfoDefine(this).getPhone()+"\n");
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
        tvExpress =findViewById(R.id.tvExpress);
        tvProductPrice =findViewById(R.id.tvProductPrice);

    }

    /**
     * 计算邮费
     */
    private void countExpress(){
        String[] m500 =new String[]{"63","64","15","54","65","46"};
        String[] m10Price =new String[]{"50","51"};
        int price300 =30000;//分
        int price500 =50000;
        try {
            JSONArray jsonArray =new JSONArray(regions);
            String curRegions = jsonArray.getJSONObject(2).getString("county_id").substring(0,2);
            if (isContain(m500,curRegions)){//5大区域
                if (aDoublePrice>=price500){
                    expressPrice =0;
                }else {
                    expressPrice =1500;
                }
            }else {
                if (isContain(m10Price,curRegions)){//四川 重新邮费 10块钱
                    if (aDoublePrice>=price300){
                        expressPrice =0;
                    }else {
                        expressPrice =1000;
                    }
                }else {
                    if (aDoublePrice>=price300){
                        expressPrice =0;
                    }else {
                        expressPrice =1500;
                    }
                }
            }

        }catch (JSONException e) {//没有地区
            e.printStackTrace();
            expressPrice =0;
        }
        if (expressPrice==0){
            tvExpress.setTextColor(ContextCompat.getColor(this,R.color.textColorClassRoom));
            tvExpress.setText("邮费 包邮");
        }else {
            tvExpress.setTextColor(ContextCompat.getColor(this,R.color.red));
            tvExpress.setText("+"+CountUtil.changeF2Y(expressPrice));
        }
    }
    private boolean isContain(String[] strings,String s){
        for (int i=0;i<strings.length;i++){
            if (strings[i].equals(s)){
                return true;
            }
        }
        return false;
    }
    @Override
    protected int layoutResId() {
        return R.layout.activity_shopping_order_shoppingcneter;
    }
}
