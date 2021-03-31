package com.xinwang.shoppingcenter.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.ShareResultCallBack;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.MyToast;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.bgqbaselib.view.CustomWebView;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.CategoryBean;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.bean.GoodsDetailBean;
import com.xinwang.shoppingcenter.bean.PicBean;

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
    private TextView tvSum,tvName,tvValue,tvTitle;
    private int mId;
    private GoodsBean.DataBean mDate;
    private CategoryBean categoryData;
    private CustomWebView webView;
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
            GlideUtils.loadAvatar(mDate!=null?mDate.getPicBeans().get(position).getUrl():"",R.color.BGPressedClassRoom,imageView);
            container.addView(imageView);
            imageView.setOnClickListener(v -> {
                ArrayList<String> mLists = new ArrayList<>();
                for (PicBean mBean:mDate.getPicBeans()){
                    mLists.add(mBean.getUrl());
                }
                jumpBigPic(mLists,position);
            });
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    };
    @Override
    protected int layoutResId() {
        return R.layout.activity_shopping_detail_shoppingcenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        initView();
        initRequest();
        initListener();

    }
    //跳转大图
    private void jumpBigPic(ArrayList<String> mLists,int position){
        BeautyDefine.getImagePreviewDefine(this).showImagePreview(mLists, position);
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
        webView.setOnClickImgListener(url -> {
            ArrayList<String> mLists = new ArrayList<>();
            mLists.add(url);
            jumpBigPic(mLists,0);
        });
        findViewById(R.id.tvBuy).setOnClickListener(v -> CommentUtils.jumpWebBrowser(ShoppingDetailActivity.this,HttpUrls.URL_CHAT));
        findViewById(R.id.llSeek).setOnClickListener(v -> CommentUtils.jumpWebBrowser(ShoppingDetailActivity.this,HttpUrls.URL_CHAT));
        findViewById(R.id.llShopping).setOnClickListener(v ->startActivity(new Intent(this,ShoppingCenterActivity.class)));//跳转购物车
        findViewById(R.id.tvAdd).setOnClickListener(v -> ShoppingCenterLibUtils.addShoppingCenter(this,mDate));//加入购物车
        findViewById(R.id.ivShare).setOnClickListener(v -> goShape(v));
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
        findViewById(R.id.rl_empty).setVisibility(View.GONE);
        rlViewPager.getLayoutParams().height = CommentUtils.getScreenWidth(this);
        tvTitle.setText(mDate.getTitle());
        if (mDate!=null)
            tvSum.setText("1/"+mDate.getPicBeans().size());
        showNameValue();

        webView.getSettings().setDefaultTextEncodingName("utf-8");
        String htmlText =mDate.getBody();
        webView.loadData( CommentUtils.getWebNewData(htmlText), "text/html;charset=utf-8", "utf-8");
    }

    private void showNameValue() {
        tvName.setText("类别\n");
        for(int i=0;i<categoryData.getData().size();i++){
            if (categoryData.getData().get(i).getId()==mDate.getCategory_id()){
                tvValue.setText(categoryData.getData().get(i).getTitle());
                tvValue.append("\n");
                for (int j=0;j<categoryData.getData().get(i).getAttr().size();j++){
                    switch (categoryData.getData().get(i).getAttr().get(j).getField()){
                        case "attr0":
                            if (!TextUtils.isEmpty(mDate.getAttr0())) {
                                tvName.append(categoryData.getData().get(i).getAttr().get(j).getTitle() + "\n");
                                tvValue.append(mDate.getAttr0() + "\n");
                            }
                            break;
                        case "attr1":
                            if (!TextUtils.isEmpty(mDate.getAttr1())) {
                                tvName.append(categoryData.getData().get(i).getAttr().get(j).getTitle() + "\n");
                                tvValue.append(mDate.getAttr1() + "\n");
                            }
                            break;
                        case "attr2":
                            if (!TextUtils.isEmpty(mDate.getAttr2())) {
                                tvName.append(categoryData.getData().get(i).getAttr().get(j).getTitle() + "\n");
                                tvValue.append(mDate.getAttr2() + "\n");
                            }
                            break;
                        case "attr3":
                            if (!TextUtils.isEmpty(mDate.getAttr3())) {
                                tvName.append(categoryData.getData().get(i).getAttr().get(j).getTitle() + "\n");
                                tvValue.append(mDate.getAttr3() + "\n");
                            }
                            break;
                        case "attr4":
                            if (!TextUtils.isEmpty(mDate.getAttr4())) {
                                tvName.append(categoryData.getData().get(i).getAttr().get(j).getTitle() + "\n");
                                tvValue.append(mDate.getAttr4() + "\n");
                            }
                            break;
                        case "attr5":
                            if (!TextUtils.isEmpty(mDate.getAttr5())) {
                                tvName.append(categoryData.getData().get(i).getAttr().get(j).getTitle() + "\n");
                                tvValue.append(mDate.getAttr5() + "\n");
                            }
                            break;
                        case "attr6":
                            if (!TextUtils.isEmpty(mDate.getAttr6())) {
                                tvName.append(categoryData.getData().get(i).getAttr().get(j).getTitle() + "\n");
                                tvValue.append(mDate.getAttr6() + "\n");
                            }
                            break;
                        case "attr7":
                            if (!TextUtils.isEmpty(mDate.getAttr7())) {
                                tvName.append(categoryData.getData().get(i).getAttr().get(j).getTitle() + "\n");
                                tvValue.append(mDate.getAttr7() + "\n");
                            }
                            break;
                        case "attr8":
                            if (!TextUtils.isEmpty(mDate.getAttr8())) {
                                tvName.append(categoryData.getData().get(i).getAttr().get(j).getTitle() + "\n");
                                tvValue.append(mDate.getAttr8() + "\n");
                            }
                            break;
                        case "attr9":
                            if (!TextUtils.isEmpty(mDate.getAttr9())) {
                                tvName.append(categoryData.getData().get(i).getAttr().get(j).getTitle() + "\n");
                                tvValue.append(mDate.getAttr9() + "\n");
                            }
                            break;
                    }
                }
                break;
            }
        }
        tvName.append("点击量");
        tvValue.append(mDate.getClick()+"次");
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
                requestDetail();
            }
        });
    }
    private void  requestDetail(){
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
                    mDate.getPicBeans().add(0, new PicBean(mDate.getCover()));
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
        if (mDate!=null) {
            mDate.getPicBeans().add(0, new PicBean(mDate.getCover()));
        }

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
        tvName = findViewById(R.id.tvName);
        tvValue = findViewById(R.id.tvValue);
        webView = findViewById(R.id.webview);

    }

    /**
     * 分享直播
     */
    public void goShape(View view){
        if (mDate!=null) {
            ArrayList<String> mPics = new ArrayList<>();
            mPics.add(mDate.getCover());
            String regMatchTag = "<[^>]*>";
            //暂时不用uri跳转 ，classroom://"+getPackageName()+".zbdetail?id="+mBean.getData().getLecture().getId()+"&type='video'
            BeautyDefine.getShareDefine(this).share("goods/detail","id="+ mDate.getId(),"classroom://"+getPackageName()+".spdetail?id="+ mDate.getId(),HttpUrls.URL_DOWNLOAD(),mPics,mDate.getTitle(),
                    mDate.getBody().replaceAll(regMatchTag,""),new ShareResultCallBack(){

                        @Override
                        public void onSucceed()
                        {

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
    }


}
