package com.xinwang.shoppingcenter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.http.ApiParams;
import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.http.HttpUrls;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.TimeUtil;
import com.xinwang.bgqbaselib.view.CustomProgressBar;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.bean.MediaBean;
import com.xinwang.shoppingcenter.bean.OrderGoodReviewDetailBean;
import com.xinwang.shoppingcenter.bean.ReviewListBean;
import com.xinwang.shoppingcenter.view.CircularImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/6/7
 * Time;8:34
 * author:baiguiqiang
 */
public class ReviewDetailActivity extends BaseNetActivity {
    CircularImage iv_avatar;
    TextView tvContent,tv_name,tv_datetime,tvReply,tvReplyTime;
    LinearLayout llStart;
    LinearLayout llPhoto;
    RelativeLayout rl_empty;
    private ReviewListBean.DataBean mEntity;
    @Override
    protected int layoutResId() {
        return R.layout.activity_review_detail_shoppingcenter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEntity = (ReviewListBean.DataBean) getIntent().getSerializableExtra("data");
        initViews();
        if (mEntity!=null)
            initShow();
        else
            initRequest();
        initListener();
    }

    private void initRequest() {
        requestGet(HttpUrls.URL_USER_ITEM_DETAIL(),new ApiParams().with("item_id",getIntent().getStringExtra("id")), OrderGoodReviewDetailBean.class, new HttpCallBack<OrderGoodReviewDetailBean>() {
            @Override
            public void onFailure(String message) {
                requestFailureShow(message);
            }

            @Override
            public void onSuccess(OrderGoodReviewDetailBean orderGoodBean) {
                mEntity =orderGoodBean.getData().getReview();
                String id = BeautyDefine.getUserInfoDefine(ReviewDetailActivity.this).getUserId();
                mEntity.setUser(new ReviewListBean.DataBean.UserBean(TextUtils.isEmpty(id)?0:Integer.parseInt(id),
                        BeautyDefine.getUserInfoDefine(ReviewDetailActivity.this).getUserHeadUrl(),BeautyDefine.getUserInfoDefine(ReviewDetailActivity.this).getNickName()));
                initShow();
            }
        });
    }

    private void initListener() {
        ((CustomToolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initShow() {
        if (mEntity.getAnonymous_state()==1||(mEntity.getUser().getId()+"").equals(BeautyDefine.getUserInfoDefine(this).getUserId())) {
            tv_name.setText(mEntity.getUser().getNickname());
            GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(50, 50, mEntity.getUser().getAvatar()), R.mipmap.default_teammate_avatar_classroom, iv_avatar);
            iv_avatar.setOnClickListener(v -> BeautyDefine.getOpenPageDefine(this).toPersonal(mEntity.getUser().getId()));//跳转个人中心
        }else {
            tv_name.setText("****");
            GlideUtils.loadAvatar(R.mipmap.default_teammate_avatar_classroom, iv_avatar);

            iv_avatar.setOnClickListener(null);
        }

        showStart(llStart, mEntity);

        showPic(llPhoto,mEntity.getMediaList());
        if (!TextUtils.isEmpty(mEntity.getContent()))
            tvContent.setText(mEntity.getContent());
        tv_datetime.setText(TimeUtil.getYMDHMS1(mEntity.getReview_time()+""));
        if (TextUtils.isEmpty(mEntity.getReply())) {
            findViewById(R.id.llReplay).setVisibility(View.GONE);
            tvReply.setVisibility(View.GONE);
        }else {
            tvReply.setText(mEntity.getReply());
        }
        tvReplyTime.setVisibility(View.GONE);
        rl_empty.setVisibility(View.GONE);
    }
    private void requestFailureShow(String error){
        TextView tvMsg = findViewById(R.id.tv_msg);
        tvMsg.setText(error);
        CustomProgressBar progressbar = findViewById(R.id.progressbar);
        progressbar .setVisibility(View.GONE);

        if (!error.equals(getString(R.string.no_data_ClassRoom)))
            rl_empty.setOnClickListener(v -> {
                progressbar.setVisibility(View.VISIBLE);
                tvMsg.setText("加载中...");
                initRequest();
            });
    }
    private void showPic(LinearLayout llPhoto, List<MediaBean> mListPhotos) {
        llPhoto.removeAllViews();
        for (int i=0;i<mListPhotos.size();i++){
            if(mListPhotos.get(i).getType()==1){
                View view =LayoutInflater.from(this).inflate(R.layout.item_review_detail_video_shoppingcenter,llPhoto,false);
                GlideUtils.loadAvatarNoCenterCrop(mListPhotos.get(i).getPicPath(), R.color.BGPressedClassRoom, view.findViewById(R.id.ivContent));
                int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jumpBigPic(mListPhotos, finalI);
                    }
                });
                llPhoto.addView(view);
            }else {
                ImageView imageView = new ImageView(this);
                imageView.setAdjustViewBounds(true);
                LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(CommentUtils.getScreenWidth(this) - CommentUtils.dip2px(this, 20), LinearLayout.LayoutParams.WRAP_CONTENT);
                mLayoutParams.setMargins(0, 0, 0, CommentUtils.dip2px(this, 10));
                imageView.setLayoutParams(mLayoutParams);

                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                GlideUtils.loadAvatarNoCenterCrop(mListPhotos.get(i).getPath(), R.color.BGPressedClassRoom, imageView);
                int finalI = i;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        jumpBigPic(mListPhotos, finalI);
                    }
                });
                llPhoto.addView(imageView);
            }
        }
    }
    private void jumpBigPic(List<MediaBean> mLists, int pos ){
        if (mLists.get(0).getType()==1){//有视频
            if (pos==0){
                startActivity(new Intent(this, SimplePlayerActivity.class).putExtra("url",mLists.get(0).getPath()));
            }else {
                ArrayList<String> mPics =new ArrayList<>();
                for (int i=0;i<mLists.size()-1;i++){
                    mPics.add(mLists.get(i+1).getPath());
                }
                BeautyDefine.getImagePreviewDefine(this).showImagePreview(mPics,pos-1);
            }
        }else {
            ArrayList<String> mPics =new ArrayList<>();
            for (int i=0;i<mLists.size();i++){
                mPics.add(mLists.get(i).getPath());
            }
            BeautyDefine.getImagePreviewDefine(this).showImagePreview(mPics,pos);
        }

    }

    private void showStart(LinearLayout llStart,ReviewListBean.DataBean mEntity) {
        llStart.removeAllViews();
        for (int i=0;i<mEntity.getScore();i++){
            ImageView imageView=new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(CommentUtils.dip2px(this,15),LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setImageResource(R.drawable.ic_baseline_star_24_shoppingcenter);
            llStart.addView(imageView);
        }
    }
    private void initViews() {
        tv_name = findViewById(R.id.tv_name);
        tv_datetime = findViewById(R.id.tv_datetime);
        tvContent = findViewById(R.id.tv_content);
        rl_empty =findViewById(R.id.rl_empty);
        llStart = findViewById(R.id.llStart);
        tvReplyTime = findViewById(R.id.tvReplyTime);
        llPhoto = findViewById(R.id.llPhoto);
        tvReply = findViewById(R.id.tvReply);
        iv_avatar = findViewById(R.id.iv_avatar);
    }
}
