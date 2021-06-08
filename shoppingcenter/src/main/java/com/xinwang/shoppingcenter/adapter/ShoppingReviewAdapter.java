package com.xinwang.shoppingcenter.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.LabelUiFactoryDefine;
import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.CountUtil;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.TimeUtil;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.ShoppingCenterLibUtils;
import com.xinwang.shoppingcenter.bean.GoodsBean;
import com.xinwang.shoppingcenter.bean.ReviewListBean;
import com.xinwang.shoppingcenter.interfaces.AdapterItemClickListener;
import com.xinwang.shoppingcenter.view.CircularImage;

import java.util.List;

/**
 * Date:2021/3/19
 * Time;14:05
 * author:baiguiqiang
 */
public class ShoppingReviewAdapter extends BaseLoadMoreAdapter<ReviewListBean.DataBean> {
    private Activity activity;
    private boolean isListPage;//是不是评论列表
    private int photoWith =0;
    public ShoppingReviewAdapter(Activity activity,List<ReviewListBean.DataBean> mDatas, boolean  isListPage) {
        super(mDatas);
        this.isListPage = isListPage;
        this.activity =activity;

        photoWith = CommentUtils.getScreenWidth(activity)-CommentUtils.dip2px(activity,isListPage?20:40);
    }

    @Override
    protected void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof BaseViewHolder){
            BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
            ReviewListBean.DataBean mEntity = mDatas.get(i);

            if (mEntity.getAnonymous_state()==1||(mEntity.getUser().getId()+"").equals(BeautyDefine.getUserInfoDefine(activity).getUserId())) {
                baseViewHolder.tv_name.setText(mEntity.getUser().getNickname());
                GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(50, 50, mEntity.getUser().getAvatar()), R.mipmap.default_teammate_avatar_classroom, baseViewHolder.iv_avatar);
                baseViewHolder.iv_avatar.setOnClickListener(v -> BeautyDefine.getOpenPageDefine(activity).toPersonal(mEntity.getUser().getId()));//跳转个人中心


            }else {
                baseViewHolder.tv_name.setText("****");
                GlideUtils.loadAvatar(R.mipmap.default_teammate_avatar_classroom, baseViewHolder.iv_avatar);

                baseViewHolder.iv_avatar.setOnClickListener(null);
            }
            if (isListPage) {
                showStart(baseViewHolder.llStart, mEntity);
                baseViewHolder.tvMore.setText("");
            }
            else {
                baseViewHolder.llStart.removeAllViews();
                if (TextUtils.isEmpty(mEntity.getReply())){
                    baseViewHolder.tvMore.setText("");
                }else
                    baseViewHolder.tvMore.setText("...");
            }
            showPic(baseViewHolder.llPhoto,mEntity.getMediaList());
            if (!TextUtils.isEmpty(mEntity.getContent()))
                baseViewHolder.tvContent.setText(mEntity.getContent());
            baseViewHolder.tv_datetime.setText(TimeUtil.getTimeFormatText(TimeUtil.getYMDHMS2(mEntity.getReview_time()+"")));

        }
    }


    private void showPic(LinearLayout llPhoto, List<String> mListPhotos) {
        llPhoto.removeAllViews();
        if (mListPhotos.size()>0)
            switch (mListPhotos.size()) {
                case 1:
                    View mRoot = LayoutInflater.from(activity).inflate(R.layout.item_review_photo_1_shoppingcenter, llPhoto,false);
                    ImageView imageView = mRoot.findViewById(R.id.iv1);
                    RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(photoWith/ 3*2, photoWith / 3*2);
                    mRoot.setLayoutParams(layout);
                    imageView.setLayoutParams(layout);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpBigPic(mListPhotos,0);
                        }
                    });
                    GlideUtils.loadRoundedCorners(BeautyDefine.getThumbUrlDefine().createThumbUrl(photoWith / 3*2,photoWith / 3*2,mListPhotos.get(0)),R.color.BGPressedClassRoom,imageView,5);
                    llPhoto.addView(mRoot);
                    break;
                case 2:
                    View mRoot1 = LayoutInflater.from(activity).inflate(R.layout.item_review_photo_2_shoppingcenter, llPhoto,false);
                    int  width = (photoWith-CommentUtils.dip2px(activity,2))/2;
                    ImageView iv11= mRoot1.findViewById(R.id.iv1);
                    ImageView iv12 = mRoot1.findViewById(R.id.iv2);
                    iv11.getLayoutParams().height =width;
                    iv11.getLayoutParams().width =width;
                    iv12.getLayoutParams().height =width;
                    iv12.getLayoutParams().width =width;
                    iv11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpBigPic(mListPhotos,0);
                        }
                    });
                    iv12.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpBigPic(mListPhotos,1);
                        }
                    });
                    GlideUtils.loadRoundedCorners(BeautyDefine.getThumbUrlDefine().createThumbUrl(width,width,mListPhotos.get(0)),R.color.BGPressedClassRoom,iv11,5);
                    GlideUtils.loadRoundedCorners(BeautyDefine.getThumbUrlDefine().createThumbUrl(width,width,mListPhotos.get(1)),R.color.BGPressedClassRoom,iv12,5);
                    llPhoto.addView(mRoot1);
                    break;
                case 3:
                    View mRoot2 = LayoutInflater.from(activity).inflate(R.layout.item_review_photo_3_shoppingcenter, llPhoto,false);
                    ImageView iv1 = mRoot2.findViewById(R.id.iv1);
                    ImageView iv2 = mRoot2.findViewById(R.id.iv2);
                    ImageView iv3 = mRoot2.findViewById(R.id.iv3);
                    int iv1Width = (photoWith-CommentUtils.dip2px(activity,2))/3*2;
                    int iv2Width = (photoWith-2*CommentUtils.dip2px(activity,2))/3*1;
                    iv1.getLayoutParams().width = iv1Width;
                    iv1.getLayoutParams().height = iv1Width;
                    iv2.getLayoutParams().height = iv2Width;
                    iv2.getLayoutParams().width = iv2Width;
                    iv3.getLayoutParams().width = iv2Width;
                    iv3.getLayoutParams().height = iv2Width;
                    iv1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpBigPic(mListPhotos,0);
                        }
                    });
                    iv2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpBigPic(mListPhotos,1);
                        }
                    });
                    iv3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpBigPic(mListPhotos,2);
                        }
                    });
                    GlideUtils.loadRoundedCorners(BeautyDefine.getThumbUrlDefine().createThumbUrl(iv1Width,iv1Width,mListPhotos.get(0)),R.color.BGPressedClassRoom,iv1,5);
                    GlideUtils.loadRoundedCorners(BeautyDefine.getThumbUrlDefine().createThumbUrl(iv2Width,iv2Width,mListPhotos.get(1)),R.color.BGPressedClassRoom,iv2,5);
                    GlideUtils.loadRoundedCorners(BeautyDefine.getThumbUrlDefine().createThumbUrl(iv2Width,iv2Width,mListPhotos.get(2)),R.color.BGPressedClassRoom,iv3,5);
                    llPhoto.addView(mRoot2);
                    break;
                default:
                    View mRoot3 = LayoutInflater.from(activity).inflate(R.layout.item_review_photo_4_shoppingcenter, llPhoto,false);
                    ImageView iv31 = mRoot3.findViewById(R.id.iv1);
                    ImageView iv32 = mRoot3.findViewById(R.id.iv2);
                    ImageView iv33 = mRoot3.findViewById(R.id.iv3);
                    ImageView iv34 = mRoot3.findViewById(R.id.iv4);
                    TextView tvMore = mRoot3.findViewById(R.id.tvMore);
                    int iv11Width = (photoWith-CommentUtils.dip2px(activity,2))/3*2;
                    int iv12Width = (photoWith-2*CommentUtils.dip2px(activity,2))/3*1;
                    iv31.getLayoutParams().height =iv11Width;
                    iv31.getLayoutParams().width =photoWith;
                    iv32.getLayoutParams().width =iv12Width;
                    iv32.getLayoutParams().height =iv12Width;
                    iv33.getLayoutParams().height =iv12Width;
                    iv33.getLayoutParams().width =iv12Width;
                    iv34.getLayoutParams().height =iv12Width;
                    iv34.getLayoutParams().width =iv12Width;
                    tvMore.getLayoutParams().width =iv12Width;
                    tvMore.getLayoutParams().height =iv12Width;
                    iv31.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpBigPic(mListPhotos,0);
                        }
                    });
                    iv32.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpBigPic(mListPhotos,1);
                        }
                    });
                    iv33.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpBigPic(mListPhotos,2);
                        }
                    });
                    iv34.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpBigPic(mListPhotos,3);
                        }
                    });
                    tvMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            jumpBigPic(mListPhotos,3);
                        }
                    });
                    GlideUtils.loadRoundedCorners(BeautyDefine.getThumbUrlDefine().createThumbUrl(photoWith,iv11Width,mListPhotos.get(0)),R.color.BGPressedClassRoom,iv31,5);
                    GlideUtils.loadRoundedCorners(BeautyDefine.getThumbUrlDefine().createThumbUrl(iv12Width,iv12Width,mListPhotos.get(1)),R.color.BGPressedClassRoom,iv32,5);
                    GlideUtils.loadRoundedCorners(BeautyDefine.getThumbUrlDefine().createThumbUrl(iv12Width,iv12Width,mListPhotos.get(2)),R.color.BGPressedClassRoom,iv33,5);
                    GlideUtils.loadRoundedCorners(BeautyDefine.getThumbUrlDefine().createThumbUrl(iv12Width,iv12Width,mListPhotos.get(3)),R.color.BGPressedClassRoom,iv34,5);
                    if (mListPhotos.size()>4){
                        tvMore.setVisibility(View.VISIBLE);
                        tvMore.setText("+"+(mListPhotos.size()-4));
                    }else {
                        tvMore.setVisibility(View.GONE);
                    }
                    llPhoto.addView(mRoot3);
                    break;
            }
    }
    /**
     *  跳转大图
     * @param mLists
     * @param pos
     */
    private void jumpBigPic( List<String> mLists,int pos ){
        BeautyDefine.getImagePreviewDefine(activity).showImagePreview(mLists,pos);
    }
    private void showStart(LinearLayout llStart,ReviewListBean.DataBean mEntity) {
        llStart.removeAllViews();
        for (int i=0;i<mEntity.getScore();i++){
            ImageView imageView=new ImageView(activity);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(CommentUtils.dip2px(activity,15),LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setImageResource(R.drawable.ic_baseline_star_24_shoppingcenter);
            llStart.addView(imageView);
        }
    }


    @Override
    public RecyclerView.ViewHolder onBaseCreateViewHolder(View view, int viewType) {
        return new BaseViewHolder(view);
    }
    class BaseViewHolder extends RecyclerView.ViewHolder {
        CircularImage iv_avatar;
        TextView tvContent,tv_name,tv_datetime,tvMore;
        LinearLayout llStart;
        LinearLayout llPhoto;
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_datetime = itemView.findViewById(R.id.tv_datetime);
            tvContent = itemView.findViewById(R.id.tv_content);

            llStart = itemView.findViewById(R.id.llStart);
            tvMore = itemView.findViewById(R.id.tvMore);
            llPhoto = itemView.findViewById(R.id.llPhoto);
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
        }
    }
    @Override
    public int getViewLayout(int viewType) {
        return R.layout.item_shopping_review_shoppingcenter;
    }


}
