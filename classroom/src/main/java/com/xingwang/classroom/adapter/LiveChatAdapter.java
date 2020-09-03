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
            LiveChatListBean.DataBean.ItemsBean itemsBean;
            LiveChatListBean.DataBean.ItemsBean itemsQuoteBean;
            if (mDatas.get(position).getQuote()==null) {
                itemsBean = mDatas.get(position);
                mBaseViewHolder.llQuoteComment.setVisibility(View.GONE);
            } else {
                mBaseViewHolder.llQuoteComment.setVisibility(View.VISIBLE);
                itemsBean =mDatas.get(position).getQuote();
                itemsQuoteBean =mDatas.get(position);
                if (itemsQuoteBean.getType()==1) {
                    mBaseViewHolder.ivQuiteContent.setVisibility(View.GONE);
                    mBaseViewHolder.tvQuoteContent.setTextColor(ContextCompat.getColor(activity,android.R.color.black));
                    mBaseViewHolder.tvQuoteContent.setText(getQuoteSpannable(position));
                    mBaseViewHolder.tvQuoteContent.setMovementMethod(LinkMovementMethod.getInstance());
                } else if (itemsQuoteBean.getType() == 2) {
                    mBaseViewHolder.ivQuiteContent.setVisibility(View.VISIBLE);
                    mBaseViewHolder.tvQuoteContent.setTextColor(ContextCompat.getColor(activity,R.color.themeClassRoom));
                    mBaseViewHolder.tvQuoteContent.setText(itemsQuoteBean.getUser().getNickname()+":");
                    GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(mWidth, mHeight, itemsQuoteBean.getBody()), mBaseViewHolder.ivQuiteContent);
                    mBaseViewHolder.ivQuiteContent.setOnClickListener(v -> {
                        List<String> mLists = new ArrayList<>();
                        mLists.add(itemsQuoteBean.getBody());
                        BeautyDefine.getImagePreviewDefine(activity).showImagePreview(mLists, 0);
                    });
                }
            }
            mBaseViewHolder.tvName.setText(itemsBean.getUser().getNickname());
            GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(mWidth, mHeight, itemsBean.getUser().getAvatar())
                    , R.mipmap.default_teammate_avatar_classroom, mBaseViewHolder.ivAvatar);
            LiveChatListBean.DataBean.ItemsBean finalItemsBean = itemsBean;
            mBaseViewHolder.ivAvatar.setOnClickListener(v -> BeautyDefine.getOpenPageDefine(activity).toPersonal(finalItemsBean.getUser().getId()));//跳转个人中心
            String badge =itemsBean.getUser().getBadge();

            if (mBaseViewHolder.llVip.getChildCount() > 1) {
                mBaseViewHolder.llVip.removeViews(1, mBaseViewHolder.llVip.getChildCount() - 1);
            }
            if (!TextUtils.isEmpty(badge) && mBaseViewHolder.llVip.getChildCount() == 1) {
                String[] badges = badge.split(",");
                for (int i = 0; i < badges.length; i++)
                    mBaseViewHolder.llVip.addView(BeautyDefine.getLabelUiFactoryDefine().getLabelUiFactory().getLabelView(activity, badges[i]));
            }

            if (itemsBean.getType()==1) {
                mBaseViewHolder.tvContent.setVisibility(View.VISIBLE);
                mBaseViewHolder.ivContent.setVisibility(View.GONE);
                mBaseViewHolder.tvContent.setText(itemsBean.getBody());
            } else if (itemsBean.getType() == 2) {
                mBaseViewHolder.tvContent.setVisibility(View.GONE);
                mBaseViewHolder.ivContent.setVisibility(View.VISIBLE);
                GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(mWidth, mHeight, itemsBean.getBody()), mBaseViewHolder.ivContent);

                mBaseViewHolder.ivContent.setOnClickListener(v -> {
                    List<String> mLists = new ArrayList<>();
                    mLists.add(itemsBean.getBody());
                    BeautyDefine.getImagePreviewDefine(activity).showImagePreview(mLists, 0);
                });

            }
            if (!String.valueOf(itemsBean.getUser().getId()).equals(BeautyDefine.getUserInfoDefine(activity).getUserId()))//不能回复自己
                mBaseViewHolder.llRoot.setOnClickListener(v -> {
                    if (onItemClick != null) {
                        onItemClick.onClickItem(position, position, position);
                    }
                });
            else
                mBaseViewHolder.llRoot.setOnClickListener(null);




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
    private SpannableString getQuoteSpannable(int position){
        String mShowName = mDatas.get(position).getUser().getNickname();
        String mDes =mDatas.get(position).getBody();
        SpannableString  spannableString =new SpannableString(""+mShowName+"："+mDes+"");
        spannableString.setSpan(new MyCheckTextView(position,false),0,
                mShowName.length()+1,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private SpannableString getSpannableStr(int position,String des){
        String mShowName = mDatas.get(position).getUser().getNickname();
        SpannableString  spannableString =new SpannableString("恭喜"+mShowName+des);
        spannableString.setSpan(new MyCheckTextView(position,false),2,
                mShowName.length()+2,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    class MyCheckTextView extends ClickableSpan {
        private boolean isQuote;
        private int position;
        public MyCheckTextView(int position,boolean isQuote) {
            this.position = position;
            this.isQuote =isQuote;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            //设置文本的颜色
            ds.setColor(ContextCompat.getColor(activity,R.color.themeClassRoom));
            //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线,其实默认也是true，如果要下划线的话可以不设置
            ds.setUnderlineText(false);
        }

        //点击事件，自由操作
        @Override
        public void onClick(View widget) {
            if (isQuote){
                BeautyDefine.getOpenPageDefine(activity).toPersonal(mDatas.get(position).getQuote().getUser().getId());
            }else
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
        TextView tvQuoteContent;
        ImageView ivQuiteContent;
        LinearLayout llQuoteComment;
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
            llQuoteComment = itemView.findViewById(R.id.llQuoteComment);
            ivQuiteContent = itemView.findViewById(R.id.ivQuiteContent);
            tvQuoteContent = itemView.findViewById(R.id.tvQuoteContent);
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
