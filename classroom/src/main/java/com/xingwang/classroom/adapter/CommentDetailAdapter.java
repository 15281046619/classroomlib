package com.xingwang.classroom.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.xingwang.classroom.ClassRoomLibUtils;
import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.CommentBean;
import com.xingwang.classroom.ui.ClassRoomDetailActivity;
import com.xingwang.classroom.utils.ActivityManager;
import com.xingwang.classroom.utils.CommentUtils;
import com.xingwang.classroom.utils.GlideUtils;
import com.xingwang.classroom.utils.TimeUtil;
import com.xingwang.classroom.view.CircularImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2019/9/5
 * Time;14:35
 * author:baiguiqiang
 */
public class CommentDetailAdapter extends BaseLoadMoreAdapter<CommentBean.DataBean.CommentsBean> {
    private OnClickDetailItemListener mOnClickDetailItem;
    private int curId;
    private Activity activity;
    private boolean isClickAvatar = false;
    private int mWidth,mHeight;
    private boolean isShowAnimator = true;//只显示一次
    public CommentDetailAdapter(List<CommentBean.DataBean.CommentsBean> mData, String curId, Activity activity) {
        super(mData);
        if (!TextUtils.isEmpty(curId))
            this.curId = Integer.parseInt(curId);
        this.activity =activity;
        mWidth = CommentUtils.dip2px(activity,110);
        mHeight = CommentUtils.dip2px(activity,90);
    }
    public void setOnDetailItemListener(OnClickDetailItemListener mOnClickDetailItem){
        this.mOnClickDetailItem =mOnClickDetailItem;
    }

    @Override
    void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof BaseViewHolder){
            BaseViewHolder mBaseViewHolder = (BaseViewHolder) viewHolder;
            mBaseViewHolder.tvName.setText(mDatas.get(position).getUser().getshowName());
            GlideUtils.loadAvatar( BeautyDefine.getThumbUrlDefine().createThumbUrl(mWidth,mHeight,mDatas.get(position).getUser().getAvatar()),R.mipmap.default_teammate_avatar_classroom,mBaseViewHolder.ivAvatar);
            mBaseViewHolder.ivAvatar.setOnClickListener(v -> BeautyDefine.getOpenPageDefine(activity).toPersonal(mDatas.get(position).getUser().getId()));//跳转个人中心
            String badge =mDatas.get(position).getUser().getBadge();
           /* if (mBaseViewHolder.rlGov.getChildCount()==2){
                mBaseViewHolder.rlGov.removeViewAt(1);
            }*/
            /*if (badge.contains("gov")&&mBaseViewHolder.rlGov.getChildCount()==1){
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                mBaseViewHolder.rlGov.addView(BeautyDefine.getBadgeUiFactoryDefine().getBadgeUiFactory().getBadgeView(activity,"gov"),layoutParams);
            }*/
            if (mBaseViewHolder.llVip.getChildCount()>1){
                mBaseViewHolder.llVip.removeViews(1,mBaseViewHolder.llVip.getChildCount()-1);
            }
            if (!TextUtils.isEmpty(badge)&&mBaseViewHolder.llVip.getChildCount()==1){
                String[] badges = badge.split(",");
                for (int i=0;i<badges.length;i++)
                mBaseViewHolder.llVip.addView(BeautyDefine.getLabelUiFactoryDefine().getLabelUiFactory().getLabelView(activity,badge));
            }
            mBaseViewHolder.tvDatetime.setText(TimeUtil.getTimeFormatText(mDatas.get(position).getPublish_time()));
            if (!TextUtils.isEmpty(mDatas.get(position).getBody())){
                mBaseViewHolder.tvContent.setVisibility(View.VISIBLE);
                mBaseViewHolder.ivContent.setVisibility(View.GONE);
                mBaseViewHolder.tvContent.setText(mDatas.get(position).getBody());
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

            if (mDatas.get(position).getUser().getId()== curId){
                mBaseViewHolder.ivSendComment.setVisibility(View.GONE);
                mBaseViewHolder.llRoot.setOnClickListener(null);
            }else {
                mBaseViewHolder.ivSendComment.setVisibility(View.VISIBLE) ;
                mBaseViewHolder.llRoot.setOnClickListener(v -> {
                    if (mOnClickDetailItem != null) {
                        mOnClickDetailItem.onClickItem(position, -1, 1);
                    }
                });
            }
            if (position==0){
                mBaseViewHolder.ivSendComment.setVisibility(View.GONE);
                mBaseViewHolder.tvBack.setVisibility(View.VISIBLE);
                mBaseViewHolder.tvBack.setOnClickListener(v ->{
                    ActivityManager.getInstance().finishActivity(ClassRoomDetailActivity.class);
                    ClassRoomLibUtils.startDetailActivity(activity,mDatas.get(0).getLecture_id());
                    activity.finish();
                });
                if (mDatas.get(position).isCurPosition()){
                        if (isShowAnimator){
                            isShowAnimator = false;
                            CommentUtils.showGradualChangeAnimator(mBaseViewHolder.llRoot, Color.parseColor("#ffffff"),Color.parseColor("#E4DCC0"));
                        }else {
                            mBaseViewHolder.llRoot.setBackgroundResource(android.R.color.white);
                        }
                }else
                         mBaseViewHolder.llRoot.setBackgroundResource(android.R.color.white);

            }else {
                mBaseViewHolder.tvBack.setVisibility(View.GONE);
                if (mDatas.get(position).getPid()!=0&&mDatas.get(position).getPid()!=mDatas.get(0).getId()){//回复不是该主评论的人
                    if (TextUtils.isEmpty(mDatas.get(position).getBody())){
                        mBaseViewHolder.tvContent.setVisibility(View.VISIBLE);
                    }
                    mBaseViewHolder.tvContent.setText(getSpannableStr(activity,mDatas.get(position)));
                    mBaseViewHolder.tvContent.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
                    mBaseViewHolder.tvContent.setOnClickListener(v ->{
                        if (mOnClickDetailItem != null&&!isClickAvatar&&mDatas.get(position).getUser().getId()!= curId)
                            mOnClickDetailItem.onClickItem(position, -1, 1);
                        isClickAvatar = false;
                    });

                }
                if (mDatas.get(position).isCurPosition()){
                    if (isShowAnimator){
                        isShowAnimator = false;
                        CommentUtils.showGradualChangeAnimator(mBaseViewHolder.llRoot, Color.parseColor("#f2f2f2"),Color.parseColor("#E4DCC0"));
                    }else {
                        mBaseViewHolder.llRoot.setBackgroundResource(R.color.BGClassRoom);
                    }
                }else {
                    mBaseViewHolder.llRoot.setBackgroundResource(R.color.BGClassRoom);
                }
            }

        }
    }
    private SpannableString getSpannableStr(Context context,CommentBean.DataBean.CommentsBean mItem){

        String mShowToCustomer = mItem.getTo_user().getshowName();
        SpannableString spannableString;
        if (!TextUtils.isEmpty(mItem.getBody())){//文字
            spannableString = new SpannableString("回复"+mShowToCustomer+"："+mItem.getBody());
        }else {
            spannableString = new SpannableString("回复"+mShowToCustomer+"：");
        }
        spannableString.setSpan(new MyCheckTextView(context,mItem.getTo_user().getId()),2,
                mShowToCustomer.length()+2,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    @Override
    RecyclerView.ViewHolder onBaseCreateViewHolder(View view,int viewType) {
        return new BaseViewHolder(view);
    }

    class MyCheckTextView extends ClickableSpan {
        private Context context;
        private int clickId;
         MyCheckTextView(Context context,int id) {
            this.context = context;
            this.clickId = id;
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            //设置文本的颜色
            ds.setColor(ContextCompat.getColor(context,R.color.themeClassRoom));
            //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线,其实默认也是true，如果要下划线的话可以不设置
            ds.setUnderlineText(false);
        }

        //点击事件，自由操作
        @Override
        public void onClick(@NonNull View widget) {
            isClickAvatar = true;
            BeautyDefine.getOpenPageDefine(activity).toPersonal(clickId);
        }
    }



    class BaseViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvDatetime;
        TextView tvContent;
        TextView tvBack;
        ImageView ivContent;
        ImageView ivSendComment;
        LinearLayout llVip;
        RelativeLayout rlGov;
        LinearLayout llRoot;
        CircularImage ivAvatar;
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDatetime = itemView.findViewById(R.id.tv_datetime);
            tvName = itemView.findViewById(R.id.tv_name);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvBack = itemView.findViewById(R.id.tv_back);
            ivContent = itemView.findViewById(R.id.iv_content);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivSendComment = itemView.findViewById(R.id.iv_send_comment);
            llVip = itemView.findViewById(R.id.ll_vip);
            rlGov = itemView.findViewById(R.id.rl_gov);
            llRoot = itemView.findViewById(R.id.ll_root);
        }
    }




    @Override
    int getViewLayout(int viewType) {
        return R.layout.item_comment_detail_classroom;
    }

    public interface OnClickDetailItemListener{
        void onClickItem(int position, int child, int type);
    }
}
