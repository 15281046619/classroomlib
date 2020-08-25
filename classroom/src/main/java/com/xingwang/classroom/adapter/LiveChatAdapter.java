package com.xingwang.classroom.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.xingwang.classroom.R;
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
public class LiveChatAdapter extends RecyclerView.Adapter<LiveChatAdapter.LiveChatViwHolder> {
    private ArrayList<LiveChatListBean.DataBean.ItemsBean> mDatas =new ArrayList<>();
    private Activity activity;
    private int mWidth,mHeight;
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
    public LiveChatAdapter.LiveChatViwHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LiveChatViwHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_live_chat,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LiveChatViwHolder mBaseViewHolder, int position) {

            mBaseViewHolder.tvName.setText(mDatas.get(position).getUser().getNickname());
            GlideUtils.loadAvatar( BeautyDefine.getThumbUrlDefine().createThumbUrl(mWidth,mHeight,mDatas.get(position).getUser().getAvatar())
                    ,R.mipmap.default_teammate_avatar_classroom,mBaseViewHolder.ivAvatar);
            mBaseViewHolder.ivAvatar.setOnClickListener(v -> BeautyDefine.getOpenPageDefine(activity).toPersonal(mDatas.get(position).getUser().getId()));//跳转个人中心
            String badge =mDatas.get(position).getUser().getBadge();

            if (mBaseViewHolder.llVip.getChildCount()>1){
                mBaseViewHolder.llVip.removeViews(1,mBaseViewHolder.llVip.getChildCount()-1);
            }
            if (!TextUtils.isEmpty(badge)&&mBaseViewHolder.llVip.getChildCount()==1){
                String[] badges = badge.split(",");
                for (int i=0;i<badges.length;i++)
                    mBaseViewHolder.llVip.addView(BeautyDefine.getLabelUiFactoryDefine().getLabelUiFactory().getLabelView(activity,badges[i]));
            }

            if (!TextUtils.isEmpty(mDatas.get(position).getBody())){
                mBaseViewHolder.tvContent.setVisibility(View.VISIBLE);
                mBaseViewHolder.ivContent.setVisibility(View.GONE);
                mBaseViewHolder.tvContent.setText(mDatas.get(position).getBody());
            }else if (mDatas.get(position).getType()!=1){
                mBaseViewHolder.tvContent.setVisibility(View.GONE);
                mBaseViewHolder.ivContent.setVisibility(View.VISIBLE);
                GlideUtils.loadAvatar(BeautyDefine.getThumbUrlDefine().createThumbUrl(mWidth,mHeight,mDatas.get(position).getBody()),mBaseViewHolder.ivContent);
                mBaseViewHolder.ivContent.setOnClickListener(v -> {
                    List<String> mLists = new ArrayList<>();
                    mLists.add(mDatas.get(position).getBody());
                    BeautyDefine.getImagePreviewDefine(activity).showImagePreview(mLists,0);
                });
            }

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
            rlGov = itemView.findViewById(R.id.rl_gov);
        }
    }
}
