package com.xingwang.classroom.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.beautydefinelibrary.LabelUiFactoryDefine;
import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.CommentBean;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.TimeUtil;
import com.xingwang.classroom.view.CircularImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2019/9/5
 * Time;14:35
 * author:baiguiqiang
 */
public class DetailAdapter extends BaseLoadMoreAdapter<CommentBean.DataBean.CommentsBean> {
    private OnClickDetailItemListener mOnClickDetailItem;
    private int curId;
    private int lectureId;
    private Activity activity;
    private boolean isClickAvatar = false;
    private int mWidth,mHeight;
    public int curMovePosition = -1;//当前跳转的position
    public DetailAdapter(List<CommentBean.DataBean.CommentsBean> mData,String curId,Activity activity,int lectureId) {
        super(mData);
        if (!TextUtils.isEmpty(curId))
            this.curId = Integer.parseInt(curId);
        this.activity =activity;
        this.lectureId =lectureId;
        mWidth = CommentUtils.dip2px(activity,50);
        mHeight = CommentUtils.dip2px(activity,40);
    }
    public void setOnDetailItemListener(OnClickDetailItemListener mOnClickDetailItem){
        this.mOnClickDetailItem =mOnClickDetailItem;
    }

    @Override
    void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof BaseViewHolder){
            BaseViewHolder mBaseViewHolder = (BaseViewHolder) viewHolder;
            mBaseViewHolder.tvName.setText(mDatas.get(position).getUser().getshowName());
            GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(mWidth,mHeight,mDatas.get(position).getUser().getAvatar()),R.mipmap.default_teammate_avatar_classroom,mBaseViewHolder.ivAvatar);
            mBaseViewHolder.ivAvatar.setOnClickListener(v -> BeautyDefine.getOpenPageDefine(activity).toPersonal(mDatas.get(position).getUser().getId()));//跳转个人中心
            mBaseViewHolder.tvDatetime.setText(TimeUtil.getTimeFormatText(mDatas.get(position).getPublish_time()));
            String badge =mDatas.get(position).getUser().getBadge();

            if (mBaseViewHolder.llVip.getChildCount()>1){
                mBaseViewHolder.llVip.removeViews(1,mBaseViewHolder.llVip.getChildCount()-1);
            }

            if (!TextUtils.isEmpty(badge)&&mBaseViewHolder.llVip.getChildCount()==1){
                String[] badges = badge.split(",");
                for (String s : badges)
                    mBaseViewHolder.llVip.addView(BeautyDefine.getLabelUiFactoryDefine().getLabelUiFactory().getLabelView(activity, s, LabelUiFactoryDefine.Style.LEVEL));
            }
            if (!TextUtils.isEmpty(mDatas.get(position).getBody())){
                mBaseViewHolder.tvContent.setVisibility(View.VISIBLE);
                mBaseViewHolder.ivContent.setVisibility(View.GONE);
                mBaseViewHolder.tvContent.setText(Html.fromHtml(mDatas.get(position).getBody()));
            }else if (!TextUtils.isEmpty(mDatas.get(position).getPics())){
                mBaseViewHolder.tvContent.setVisibility(View.GONE);
                mBaseViewHolder.ivContent.setVisibility(View.VISIBLE);
                GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(mWidth,mHeight,mDatas.get(position).getPics()),mBaseViewHolder.ivContent);
                mBaseViewHolder.ivContent.setOnClickListener(v -> {
                    List<String> mLists = new ArrayList<>();
                    mLists.add(mDatas.get(position).getPics());
                    BeautyDefine.getImagePreviewDefine(activity).showImagePreview(mLists,0);
                });
            }
            mBaseViewHolder.llCommentMore.removeAllViews();
            if (curMovePosition==0&&position==0&&mDatas.get(position).getSub_comments().size()==0){
                curMovePosition=-1;
                CommentUtils.showGradualChangeAnimator(mBaseViewHolder.llRoot, Color.parseColor("#f2f2f2"),Color.parseColor("#E4DCC0"));
            }
            if (mDatas.get(position).getSub_comments()!=null&&mDatas.get(position).getSub_comments().size()>0){
                mBaseViewHolder.llCommentMore.setVisibility(View.VISIBLE);
                for (int i=0;i<mDatas.get(position).getSub_comments().size();i++){
                    View mRootView =  LayoutInflater.from( mBaseViewHolder.llCommentMore.getContext()).inflate(R.layout.item_detail_chider_classroom,mBaseViewHolder.llCommentMore,false);
                    TextView tvContent =  mRootView.findViewById(R.id.tv_content);
                    ImageView ivContent =  mRootView.findViewById(R.id.iv_content);
                    tvContent.setText(getSpannableStr(mBaseViewHolder.llCommentMore.getContext(),mDatas.get(position).getSub_comments().get(i),mDatas.get(position).getId()));
                    tvContent.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
                    if (!TextUtils.isEmpty(mDatas.get(position).getSub_comments().get(i).getBody())){//显示文字
                        ivContent.setVisibility(View.GONE);
                    }else {
                        ivContent.setVisibility(View.VISIBLE);
                        int finalI1 = i;
                        ivContent.setOnClickListener(v -> {
                            List<String> mLists = new ArrayList<>();
                            mLists.add(mDatas.get(position).getSub_comments().get(finalI1).getPics());
                            BeautyDefine.getImagePreviewDefine(activity).showImagePreview(mLists,0);
                        });
                        GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(mWidth,mHeight,mDatas.get(position).getSub_comments().get(i).getPics()),ivContent);
                    }
                    int finalI = i;
                    if (mDatas.get(position).getSub_comments().get(i).getUser().getId()!=curId) {
                        tvContent.setOnClickListener(v -> {
                            if (mOnClickDetailItem != null&&!isClickAvatar) {
                                mOnClickDetailItem.onClickItem(position, finalI, 1);
                            }
                            isClickAvatar = false;
                        });
                        mRootView.setOnLongClickListener(v -> {
                            if (mOnClickDetailItem != null) {
                                mOnClickDetailItem.onClickItem(position, finalI, 2);
                            }
                            return true;
                        });
                        mRootView.setOnClickListener(v -> {
                            if (mOnClickDetailItem != null) {
                                mOnClickDetailItem.onClickItem(position, finalI, 1);
                            }
                        });
                    }
                    if (curMovePosition!=-1&&mDatas.get(position).getSub_comments().size()-1==i){
                        curMovePosition=-1;
                        CommentUtils.showGradualChangeAnimator(mRootView, Color.parseColor("#E0DEDE"),Color.parseColor("#E4DCC0"));
                    }
                    mBaseViewHolder.llCommentMore.addView(mRootView);
                }
                View mRootView =  LayoutInflater.from( mBaseViewHolder.llCommentMore.getContext()).inflate(R.layout.item_detail_chider_footer_classroom,mBaseViewHolder.llCommentMore,false);
                mRootView.setOnClickListener(v -> {
                    Uri uri = Uri.parse("classroom://"+activity.getApplicationInfo().packageName+".pldetail?div_id=0&lecture_id="+lectureId+"&bid="+mDatas.get(position).getId()+"");
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    activity.startActivity(intent);

                });
                mBaseViewHolder.llCommentMore.addView(mRootView);
            }else {
                mBaseViewHolder.llCommentMore.setVisibility(View.GONE);
            }
            if (mDatas.get(position).getUser().getId()== curId){
                mBaseViewHolder.ivSendComment.setVisibility(View.GONE);
                mBaseViewHolder.llRoot.setOnClickListener(null);
            }else {
                mBaseViewHolder.ivSendComment.setVisibility(View.VISIBLE);
                mBaseViewHolder.ivSendComment.setOnClickListener(v -> {
                    if (mOnClickDetailItem != null) {
                        mOnClickDetailItem.onClickItem(position, -1, 1);
                    }
                });
                mBaseViewHolder.llRoot.setOnClickListener(v -> {
                    if (mOnClickDetailItem != null) {
                        mOnClickDetailItem.onClickItem(position, -1, 1);
                    }
                });
                mBaseViewHolder.llCommentMore.setOnClickListener(null);
            }

        }
    }
    private SpannableString getSpannableStr(Context context,CommentBean.DataBean.CommentsBean mItem, int homeId){
        String mShowName = mItem.getUser().getshowName();
        String mShowToCustomer = mItem.getTo_user().getshowName();
        SpannableString spannableString;

        if (!TextUtils.isEmpty(mItem.getBody())){
            if (homeId==0||mItem.getPid()==homeId){
                spannableString = new SpannableString(mShowName+"："+mItem.getBody());
                spannableString.setSpan(new MyCheckTextView(context,mItem.getUser().getId()),0,mShowName.length(),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else {
                spannableString = new SpannableString(mShowName+"回复"+mShowToCustomer+"："+mItem.getBody());
                if (mShowName.length()!=0)
                    spannableString.setSpan(new MyCheckTextView(context,mItem.getUser().getId()),0,mShowName.length(),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (mShowName.length()+mShowToCustomer.length()!=0)
                    spannableString.setSpan(new MyCheckTextView(context,mItem.getTo_user().getId()),mShowName.length()+2,
                            mShowName.length()+mShowToCustomer.length()+2,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }else {
            if (homeId==0||mItem.getPid()==homeId){
                spannableString = new SpannableString(mShowName+"：");
                spannableString.setSpan(new MyCheckTextView(context,mItem.getUser().getId()),0,mShowName.length(),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else {
                spannableString = new SpannableString(mShowName+"回复"+mShowToCustomer+"："+mItem.getBody());
                if (mShowName.length()!=0)
                    spannableString.setSpan(new MyCheckTextView(context,mItem.getUser().getId()),0,mShowName.length(),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (mShowToCustomer.length()!=0)
                    spannableString.setSpan(new MyCheckTextView(context,mItem.getTo_user().getId()),mShowName.length()+2,mShowName.length()+mShowToCustomer.length()+2,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannableString;
    }
    @Override
    RecyclerView.ViewHolder onBaseCreateViewHolder(View view,int viewType) {
        return new BaseViewHolder(view);
    }
    class MyCheckTextView extends ClickableSpan {
        private Context context;
        private int clickId;
        public MyCheckTextView(Context context,int id) {
            this.context = context;
            this.clickId = id;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            //设置文本的颜色
            ds.setColor(ContextCompat.getColor(context,R.color.themeClassRoom));
            //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线,其实默认也是true，如果要下划线的话可以不设置
            ds.setUnderlineText(false);
        }

        //点击事件，自由操作
        @Override
        public void onClick(View widget) {
            isClickAvatar = true;
            BeautyDefine.getOpenPageDefine(activity).toPersonal(clickId);
        }
    }


    class BaseViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDatetime;
        TextView tvContent;
        ImageView ivContent;
        ImageView ivSendComment;
        LinearLayout llCommentMore;
        LinearLayout llRoot;
        LinearLayout llVip;
        RelativeLayout rlGov;
        CircularImage ivAvatar;
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDatetime = itemView.findViewById(R.id.tv_datetime);
            tvName = itemView.findViewById(R.id.tv_name);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            ivContent = itemView.findViewById(R.id.iv_content);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivSendComment = itemView.findViewById(R.id.iv_send_comment);
            llCommentMore = itemView.findViewById(R.id.ll_comment_more);
            llVip = itemView.findViewById(R.id.ll_vip);
            rlGov = itemView.findViewById(R.id.rl_gov);
            llRoot = itemView.findViewById(R.id.ll_root);
        }
    }




    @Override
    int getViewLayout(int viewType) {
        return R.layout.item_comment_left_text_classroom;
    }

    public interface OnClickDetailItemListener{
        void onClickItem(int position,int child,int type);

    }
}
