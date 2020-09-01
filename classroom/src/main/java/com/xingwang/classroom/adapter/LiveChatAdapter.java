package com.xingwang.classroom.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.CommentBean;
import com.xingwang.classroom.bean.LiveChatListBean;
import com.xingwang.classroom.utils.CommentUtils;
import com.xingwang.classroom.utils.GlideUtils;
import com.xingwang.classroom.view.CircularImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2020/8/20
 * Time;15:32
 * author:baiguiqiang
 */
public class LiveChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<LiveChatListBean.DataBean.ItemsBean> mDatas =new ArrayList<>();
    private Activity activity;
    private int mWidth,mHeight;
    private DetailAdapter.OnClickDetailItemListener onItemClick;
    private final int ITEM_TYPE_1 =1;
    private final int ITEM_TYPE_2 =2;
    public LiveChatAdapter( FragmentActivity activity) {

        this.activity =activity;
        mWidth = CommentUtils.dip2px(activity,110);
        mHeight = CommentUtils.dip2px(activity,90);
    }
    public ArrayList<LiveChatListBean.DataBean.ItemsBean> getData(){
        return mDatas;
    }
    public void AddData(ArrayList<LiveChatListBean.DataBean.ItemsBean> mDatas){
        this.mDatas.addAll(mDatas);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i==ITEM_TYPE_1)
            return new LiveChatViwHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_live_chat_classroom,viewGroup,false));
        else
            return new SysViwHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_live_chat_type2_classroom,viewGroup,false));
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.get(position).getType()==1||mDatas.get(position).getType()==2){
            if (!TextUtils.isEmpty(mDatas.get(position).getGame_tips()))
                return ITEM_TYPE_2;
            else
                return ITEM_TYPE_1;
        }else {
            return ITEM_TYPE_2;
        }

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mViewHolder, int position) {
        if (mViewHolder instanceof LiveChatViwHolder) {
            LiveChatViwHolder mBaseViewHolder = (LiveChatViwHolder) mViewHolder;
            mBaseViewHolder.tvName.setText(mDatas.get(position).getUser().getNickname());
            GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(mWidth, mHeight, mDatas.get(position).getUser().getAvatar())
                    , R.mipmap.default_teammate_avatar_classroom, mBaseViewHolder.ivAvatar);
            mBaseViewHolder.ivAvatar.setOnClickListener(v -> BeautyDefine.getOpenPageDefine(activity).toPersonal(mDatas.get(position).getUser().getId()));//跳转个人中心
            String badge = mDatas.get(position).getUser().getBadge();

            if (mBaseViewHolder.llVip.getChildCount() > 1) {
                mBaseViewHolder.llVip.removeViews(1, mBaseViewHolder.llVip.getChildCount() - 1);
            }
            if (!TextUtils.isEmpty(badge) && mBaseViewHolder.llVip.getChildCount() == 1) {
                String[] badges = badge.split(",");
                for (int i = 0; i < badges.length; i++)
                    mBaseViewHolder.llVip.addView(BeautyDefine.getLabelUiFactoryDefine().getLabelUiFactory().getLabelView(activity, badges[i]));
            }

            if (mDatas.get(position).getType()==1) {
                mBaseViewHolder.tvContent.setVisibility(View.VISIBLE);
                mBaseViewHolder.ivContent.setVisibility(View.GONE);
                mBaseViewHolder.tvContent.setText(mDatas.get(position).getBody());
            } else if (mDatas.get(position).getType() == 2) {
                mBaseViewHolder.tvContent.setVisibility(View.GONE);
                mBaseViewHolder.ivContent.setVisibility(View.VISIBLE);
                GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(mWidth, mHeight, mDatas.get(position).getBody()), mBaseViewHolder.ivContent);
                mBaseViewHolder.ivContent.setOnClickListener(v -> {
                    List<String> mLists = new ArrayList<>();
                    mLists.add(mDatas.get(position).getBody());
                    BeautyDefine.getImagePreviewDefine(activity).showImagePreview(mLists, 0);
                });
            }
            mBaseViewHolder.llRoot.setOnClickListener(v -> {
                if (onItemClick != null) {
                    onItemClick.onClickItem(position, position, position);
                }
            });
        }else if (mViewHolder instanceof SysViwHolder){
            if (mDatas.get(position).getType()==3) {//下单
                ((SysViwHolder) mViewHolder).tvContent.setText(getSpannableStr(position,"下单成功"));
            }else if (mDatas.get(position).getType()==4){//打赏成功
                ((SysViwHolder) mViewHolder).tvContent.setText(getSpannableStr(position,"打赏成功"));
            }else if (!TextUtils.isEmpty(mDatas.get(position).getGame_tips())){
                ((SysViwHolder) mViewHolder).tvContent.setText(getSpannableStr(position,"抢答中获得积分"));
            }
            ((SysViwHolder) mViewHolder).tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        }

    }
    private SpannableString getSpannableStr(int position,String des){
        String mShowName = mDatas.get(position).getUser().getNickname();
        SpannableString  spannableString =new SpannableString("恭喜"+mShowName+des);
        spannableString.setSpan(new MyCheckTextView(position),2,
                mShowName.length()+2,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    class MyCheckTextView extends ClickableSpan {

        private int position;
        public MyCheckTextView(int position) {
            this.position = position;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            //设置文本的颜色
            ds.setColor(ContextCompat.getColor(activity,R.color.sysMessageClassRoom));
            //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线,其实默认也是true，如果要下划线的话可以不设置
            ds.setUnderlineText(false);
        }

        //点击事件，自由操作
        @Override
        public void onClick(View widget) {
            BeautyDefine.getOpenPageDefine(activity).toPersonal(mDatas.get(position).getUser().getId());
        }
    }

    public void setOnItemClick(DetailAdapter.OnClickDetailItemListener onItemClick){
        this.onItemClick =onItemClick;
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class LiveChatViwHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvContent;
        TextView tvBack;
        ImageView ivContent;
        LinearLayout llVip;
        LinearLayout llRoot;
        RelativeLayout rlGov;
        CircularImage ivAvatar;
        LiveChatViwHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvBack = itemView.findViewById(R.id.tv_back);
            ivContent = itemView.findViewById(R.id.iv_content);
            tvContent = itemView.findViewById(R.id.tv_content);
            llVip = itemView.findViewById(R.id.ll_vip);
            llRoot = itemView.findViewById(R.id.ll_root);
            rlGov = itemView.findViewById(R.id.rl_gov);
        }
    }
    class SysViwHolder extends RecyclerView.ViewHolder{
        private TextView tvContent;
        SysViwHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
}
