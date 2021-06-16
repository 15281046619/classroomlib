package com.xinwang.shoppingcenter.ui;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.OpenPageDefine;
import com.beautydefinelibrary.ShareResultCallBack;
import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.sku.bean.Sku;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.GsonUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.utils.SharedPreferenceUntils;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.bgqbaselib.view.CustomWebView;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.adapter.ShoppingReviewAdapter;
import com.xinwang.shoppingcenter.bean.CategoryBean;
import com.xinwang.shoppingcenter.bean.ErpBean;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.bean.GoodsDetailBean;
import com.xinwang.shoppingcenter.bean.NumberBean;
import com.xinwang.shoppingcenter.bean.ReviewListBean;
import com.xinwang.shoppingcenter.bean.SkuBean;
import com.xinwang.shoppingcenter.dialog.BottomSkuDialog;
import com.xinwang.shoppingcenter.interfaces.OnClickOkListener;
import com.xinwang.shoppingcenter.view.WrapContentLinearLayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/3/26
 * Time;13:03
 * author:baiguiqiang
 */
public class ShoppingDetailActivity extends BaseNetActivity {
    private ViewPager viewPager;
    private CustomToolbar toolbar;
    private RelativeLayout rlViewPager;
    private TextView tvSum,tvTitle,tvPrice,tvNumber,tvDes;
    private LinearLayout llContent;
    private RecyclerView rcvReview;
    private int mId;
    private GoodsBean.DataBean mDate;
    private CategoryBean categoryData;
    private CustomWebView webView;
    private List<Sku> skuList =new ArrayList<>();
    long curTime =0;
    private List<ReviewListBean.DataBean> mReviewList =new ArrayList<>();
    private PagerAdapter mAdapter = new PagerAdapter() {

        @Override
        public int getCount() {
            return mDate==null?0:mDate.getPicBeans().size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view==o;
        }


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView imageView =new ImageView(container.getContext());

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setBackgroundResource(R.color.black);
            GlideUtils.loadAvatar(mDate!=null?mDate.getPicBeans().get(position):"",R.color.BGPressedClassRoom,imageView);
            container.addView(imageView);
            imageView.setOnClickListener(v -> {
                ArrayList<String> mLists = new ArrayList<>(mDate.getPicBeans());
                jumpBigPic(mLists,position);
            });
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    };
    private int number;

    @Override
    protected int layoutResId() {
        return R.layout.activity_shopping_detail_shoppingcenter;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webView!=null){
            return webView.onViewKeyDown(keyCode,event);
        }else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initIntent();
        initView();
        initRequest();
        initListener();
        initNumber();

    }
    @Subscribe()
    public void updateNumber(NumberBean numberBean){
        number =number+numberBean.getSum();
        showNumber();
    }
    //跳转大图
    private void jumpBigPic(ArrayList<String> mLists,int position){
        BeautyDefine.getImagePreviewDefine(this).showImagePreview(mLists, position);
    }
    private void initNumber() {
        try {
            number = GsonUtils.changeGsonToSafeList( SharedPreferenceUntils.getGoods(this), Sku.class).size();

        }catch (Exception e){

        }
        showNumber();
    }

    private void showNumber(){
        if (number<=99) {
            if (number<0){
                tvNumber.setText("0");
            }else {
                tvNumber.setText(number+"");
            }
        }else {
            tvNumber.setText("99");
        }
    }

    private void initListener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tvSum.setText(i+1+"/"+mDate.getPicBeans().size());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        findViewById(R.id.tvBuy).setOnClickListener(v -> showSkuDialog(1));
        findViewById(R.id.llSeek).setOnClickListener(v -> goRequestErp());//咨询
        findViewById(R.id.llShopping).setOnClickListener(v ->startActivity(new Intent(this,ShoppingCenterActivity.class)));//跳转购物车
        findViewById(R.id.tvAdd).setOnClickListener(v -> showSkuDialog(0));//加入购物车
        findViewById(R.id.ivShare).setOnClickListener(v -> goShape(v));
        findViewById(R.id.tvAllReview).setOnClickListener(v -> startActivity(new Intent(ShoppingDetailActivity.this,ReviewListActivity.class)
        .putExtra("id",mId+"").putExtra("data", (Serializable) mReviewList)));
    }

    /**
     * 获取技术老师
     */
    private void goRequestErp() {
        BeautyDefine.getOpenPageDefine(this).progressControl(new OpenPageDefine.ProgressController.Showder("加载中",false));
        requestGet(HttpUrls.URL_USER_MY_ERP(),new ApiParams(), ErpBean.class, new HttpCallBack<ErpBean>() {

            @Override
            public void onFailure(String message) {
                MyToast.myToast(getApplicationContext(),message);
                BeautyDefine.getOpenPageDefine(ShoppingDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
            }

            @Override
            public void onSuccess(ErpBean erpBean) {
                BeautyDefine.getOpenPageDefine(ShoppingDetailActivity.this).progressControl(new OpenPageDefine.ProgressController.Hider());
                ShoppingCenterLibUtils.jumpChat(ShoppingDetailActivity.this,erpBean.getData()==null?-1:erpBean.getData().getId(),
                        mDate.getTitle());
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
                initRequest();
            });
    }
    private void initShow() {


        tvTitle.setText(mDate.getTitle());
        if (mDate!=null) {
            float coverRate= Float.parseFloat(mDate.getCoverRate());
            rlViewPager.getLayoutParams().height = (int) (coverRate*(CommentUtils.getScreenWidth(this)));
            tvSum.setText("1/" + mDate.getPicBeans().size());
            showNameValue();
            curTime =System.currentTimeMillis();
            webView.loadData(mDate.getBody());

            if (TextUtils.isEmpty(skuList.get(0).getShowPrice())){
                tvPrice.setVisibility(View.GONE);
            }else {
                tvPrice.setVisibility(View.VISIBLE);
                tvPrice.setText(ShoppingCenterLibUtils.getPriceSpannable("￥"+skuList.get(0).getShowPrice()));
            }
        }else {
            rlViewPager.getLayoutParams().height = CommentUtils.getScreenWidth(this);
        }

        if (mReviewList.size()>0){
            rcvReview.setLayoutManager(new WrapContentLinearLayoutManager(this));
            tvDes.setText("商品评价（"+mDate.getReview_count()+"）");
            List<ReviewListBean.DataBean> mDatas =new ArrayList<>();
            mDatas.add(mReviewList.get(0));
            if (mReviewList.size()>1&&TextUtils.isEmpty(mReviewList.get(0).getMedia())&&TextUtils.isEmpty(mReviewList.get(1).getMedia())){
                mDatas.add(mReviewList.get(1));
            }

            ShoppingReviewAdapter mAdapter = new ShoppingReviewAdapter(this,mDatas,false);
            mAdapter.setOnItemClickListener(new BaseLoadMoreAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    startActivity(new Intent(ShoppingDetailActivity.this,ReviewDetailActivity.class).putExtra("data",mAdapter.mDatas.get(position)));
                }
            });
            mAdapter.setLoadState(2);
            rcvReview.setAdapter(mAdapter);
            findViewById(R.id.rlReview).setVisibility(View.VISIBLE);
        }


        rlViewPager.post(new Runnable() {
            @Override
            public void run() {
                findViewById(R.id.rl_empty).setVisibility(View.GONE);
            }
        });
    }
    private void showNameValue() {
        llContent.removeAllViews();
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.setMargins(0,0,0,CommentUtils.dip2px(this,10));
        for(int i=0;i<categoryData.getData().size();i++){
            if (categoryData.getData().get(i).getId()==mDate.getCategory_id()){
                View view1 = LayoutInflater.from(this).inflate(R.layout.item_detail_attr_shoppingcenter,llContent,false);
                TextView tvName1= view1.findViewById(R.id.tvName);
                TextView tvValue1= view1.findViewById(R.id.tvValue);
                tvName1.setText("类别");
                tvValue1.setText(categoryData.getData().get(i).getTitle());
                llContent.addView(view1,mLayoutParams);
                for (int j=0;j<categoryData.getData().get(i).getAttr().size();j++){



                    switch (categoryData.getData().get(i).getAttr().get(j).getField()){
                        case "attr0":
                                addAttrView(mLayoutParams,mDate.getAttr0(), i, j);
                            break;
                        case "attr1":
                                addAttrView(mLayoutParams,mDate.getAttr1(), i, j);
                            break;
                        case "attr2":
                                addAttrView(mLayoutParams,mDate.getAttr2(), i, j);
                            break;
                        case "attr3":
                                addAttrView(mLayoutParams,mDate.getAttr3(), i, j);
                            break;
                        case "attr4":
                                addAttrView(mLayoutParams,mDate.getAttr4(), i, j);
                            break;
                        case "attr5":
                                addAttrView(mLayoutParams,mDate.getAttr5(), i, j);
                            break;
                        case "attr6":
                                addAttrView(mLayoutParams,mDate.getAttr6(), i, j);
                            break;
                        case "attr7":
                                addAttrView(mLayoutParams,mDate.getAttr7(), i, j);
                            break;
                        case "attr8":
                                addAttrView(mLayoutParams,mDate.getAttr8(), i, j);
                            break;
                        case "attr9":
                                addAttrView(mLayoutParams,mDate.getAttr9(), i, j);
                            break;
                    }

                }

                break;
            }
        }
        View view = LayoutInflater.from(this).inflate(R.layout.item_detail_attr_shoppingcenter,llContent,false);
        TextView tvName= view.findViewById(R.id.tvName);
        TextView tvValue= view.findViewById(R.id.tvValue);
        tvName.setText("点击量");
        tvValue.append(mDate.getClick()+"次");
        llContent.addView(view);


    }

    private void addAttrView(LinearLayout.LayoutParams mLayoutParams,String data, int i, int j) {
        if (!TextUtils.isEmpty(data)&&!data.equals("[]")) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_detail_attr_shoppingcenter, llContent, false);
            TextView tvName = view.findViewById(R.id.tvName);
            TextView tvValue = view.findViewById(R.id.tvValue);
            List<String> mList = GsonUtils.changeGsonToSafeList(data, String.class);
            if (mList.size() > 0) {
                for (int t = 0; t < mList.size(); t++) {
                    tvValue.append(mList.get(t));
                    if (t != mList.size() - 1) {
                        tvValue.append(" ");
                    }
                }
                tvName.append(categoryData.getData().get(i).getAttr().get(j).getTitle());
            }
            llContent.addView(view, mLayoutParams);
        }
    }

    private void showSkuDialog(int clickType){
        if (skuList!=null&&skuList.size()>0&&skuList.get(0).getId()!=null)
            BottomSkuDialog.getInstance(skuList,clickType).setOnClickOkListener(new OnClickOkListener() {
                @Override
                public void onClickOk(Sku sku) {
                    ArrayList<Sku> skus =new ArrayList<>();
                    skus.add(sku);
                    jumpOrderActivity(skus);
                }
            }).showDialog(getSupportFragmentManager());
        else {
            if (clickType==0) {
                if (skuList.size() > 0)
                    ShoppingCenterLibUtils.addShoppingCenter(this, skuList.get(0));
            }else  if (clickType==1){//立即购买
                jumpOrderActivity((ArrayList<Sku>) skuList);
            }
        }
    }

    /**
     * 跳转到确认页面
     */
    private void jumpOrderActivity(ArrayList<Sku> skuList) {
        startActivity(new Intent(this,ShoppingOrderActivity.class).putParcelableArrayListExtra("data",  skuList));
    }

    private void initRequest() {
        requestCategoryList();
    }
    private void requestCategoryList(){
        requestGet(HttpUrls.URL_GOODS_CATEGORY_LISTS(),new ApiParams(), CategoryBean.class, new HttpCallBack<CategoryBean>() {

            @Override
            public void onFailure(String message) {
                requestFailureShow(message);
                MyToast.myToast(getApplicationContext(),message);
            }

            @Override
            public void onSuccess(CategoryBean categoryBean) {
                categoryData = categoryBean;
                getReviewList();
            }
        });
    }

    private void  getReviewList(){
        requestGet(HttpUrls.URL_GOODS_REVIEW_LISTS(),new ApiParams().with("goods_id",mId).with("page","1").with("page_num",10), ReviewListBean.class, new HttpCallBack<ReviewListBean>() {

            @Override
            public void onFailure(String message) {
                requestFailureShow(message);
                MyToast.myToast(getApplicationContext(),message);
            }

            @Override
            public void onSuccess(ReviewListBean categoryBean) {
                if (categoryBean.getData()!=null&&categoryBean.getData().size()>0){
                    mReviewList =categoryBean.getData();
                }
                requestSkuData();
            }
        });
    }
    /**
     * 获取sku
     */
    private void requestSkuData() {

        requestGet(HttpUrls.URL_GOODS_HOME_SKU_LISTS(),new ApiParams().with("goods_id",mId), SkuBean.class, new HttpCallBack<SkuBean>(){

            @Override
            public void onFailure(String message) {
                requestFailureShow(message);
                MyToast.myToast(getApplicationContext(),message);
            }

            @Override
            public void onSuccess(SkuBean skuBean) {
                requestDetail(skuBean);
            }
        });
    }
    private void  requestDetail(SkuBean skuBean){
        requestGet(HttpUrls.URL_GOODS_DETAIL(),new ApiParams().with("id",mId), GoodsDetailBean.class, new HttpCallBack<GoodsDetailBean>() {

            @Override
            public void onFailure(String message) {
                requestFailureShow(message);
                MyToast.myToast(getApplicationContext(),message);
            }

            @Override
            public void onSuccess(GoodsDetailBean goodsDetailBean) {
                mDate =goodsDetailBean.getData().getGoods();
                if (mDate!=null) {
                    mDate.getPicBeans().add(0, mDate.getCover());
                    skuList= ShoppingCenterLibUtils.skuToBean(skuBean.getData(),goodsDetailBean.getData().getGoods());
                    initShow();
                    initAdapter();
                }else {
                    requestFailureShow("暂无商品");
                }
            }
        });
    }


    private void initAdapter() {
        viewPager.setAdapter(mAdapter);
    }

    private void initIntent() {
        Intent intent =getIntent();
        mId =intent.getIntExtra("id",0);
        Uri uri = intent.getData();
        if (uri == null) {
            return;
        }
        String actionData = uri.getQueryParameter("id");

        if (actionData!=null)
            mId =Integer.parseInt(actionData);
    }

    private void initView() {
        viewPager = findViewById(R.id.viewPager);
        toolbar =findViewById(R.id.toolbar);
        rlViewPager =findViewById(R.id.rlViewPager);
        tvSum = findViewById(R.id.tvSum);
        tvTitle = findViewById(R.id.tvTitle);
        llContent = findViewById(R.id.llContent);
        tvNumber =findViewById(R.id.tvNumber);
        webView = findViewById(R.id.webview);
        tvPrice = findViewById(R.id.tvPrice);
        tvDes = findViewById(R.id.tvDes);
        rcvReview = findViewById(R.id.rcvReview);
    }

    /**
     * 分享课程
     */
    public void goShape(View view){
        if (mDate!=null) {
            ArrayList<String> mPics = new ArrayList<>();
            mPics.add(mDate.getCover());
            String regMatchTag = "<[^>]*>";
            //暂时不用uri跳转 ，classroom://"+getPackageName()+".zbdetail?id="+mBean.getData().getLecture().getId()+"&type='video'
            BeautyDefine.getShareDefine(this).share("goods/detail",CommentUtils.urlDecode(new String[]{"id"},new String[]{mDate.getId()+""}),
                    "classroom://"+getPackageName()+".spdetail?"+CommentUtils.urlDecode(new String[]{"id"},new String[]{mDate.getId()+""}),
                    HttpUrls.URL_DOWNLOAD(),mPics,mDate.getTitle(),
                    mDate.getBody().replaceAll(regMatchTag,""),new ShareResultCallBack(){

                        @Override
                        public void onSucceed()
                        {
                            MyToast.myToast(getApplicationContext(),"分享成功");
                        }
                        @Override
                        public void onFailure(String s) {
                            MyToast.myToast(getApplicationContext(),s);
                        }
                    });
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
