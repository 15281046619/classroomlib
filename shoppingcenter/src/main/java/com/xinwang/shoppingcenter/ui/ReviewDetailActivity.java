package com.xinwang.shoppingcenter.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.xinwang.bgqbaselib.base.BaseNetActivity;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.TimeUtil;
import com.xinwang.bgqbaselib.view.CustomToolbar;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.bean.ReviewListBean;
import com.xinwang.shoppingcenter.view.CircularImage;

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
        initShow();
        initListener();
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
        if (mEntity.getAnonymous_state()==1) {
            tv_name.setText(mEntity.getUser().getNickname());
            GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(50, 50, mEntity.getUser().getAvatar()), R.mipmap.default_teammate_avatar_classroom, iv_avatar);
            iv_avatar.setOnClickListener(v -> BeautyDefine.getOpenPageDefine(this).toPersonal(mEntity.getUser().getId()));//跳转个人中心
        }else {
            tv_name.setText("****");
            GlideUtils.loadAvatar(R.mipmap.default_teammate_avatar_classroom, iv_avatar);

            iv_avatar.setOnClickListener(null);
        }

        showStart(llStart, mEntity);
        List<String> mListPhotos = mEntity.getMediaList();
        showPic(llPhoto,mListPhotos);
        if (!TextUtils.isEmpty(mEntity.getContent()))
            tvContent.setText(mEntity.getContent());
        tv_datetime.setText(TimeUtil.getYMDHMS1(mEntity.getReview_time()+""));
        if (TextUtils.isEmpty(mEntity.getReply())) {
            findViewById(R.id.llReplay).setVisibility(View.GONE);
        }else {
            tvReply.setText(mEntity.getReply());
            if (mEntity.getReview_time() != 0)
                tvReplyTime.setText(TimeUtil.getYMD(mEntity.getReview_time() + ""));
        }
    }

    private void showPic(LinearLayout llPhoto, List<String> mListPhotos) {
        llPhoto.removeAllViews();
        for (int i=0;i<mListPhotos.size();i++){
            ImageView imageView =new ImageView(this);
            imageView.setAdjustViewBounds(true);
            LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mLayoutParams.setMargins(0,0,0,CommentUtils.dip2px(this,5));
            imageView.setLayoutParams(mLayoutParams);
            GlideUtils.loadAvatar(mListPhotos.get(i),R.color.BGPressedClassRoom,imageView);
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

    private void jumpBigPic( List<String> mLists,int pos ){
        BeautyDefine.getImagePreviewDefine(this).showImagePreview(mLists,pos);
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

        llStart = findViewById(R.id.llStart);
        tvReplyTime = findViewById(R.id.tvReplyTime);
        llPhoto = findViewById(R.id.llPhoto);
        tvReply = findViewById(R.id.tvReply);
        iv_avatar = findViewById(R.id.iv_avatar);
    }
}
