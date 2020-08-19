package com.xingwang.classroom.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.LiveListBean;
import com.xingwang.classroom.utils.GlideUtils;
import com.xingwang.classroom.utils.LogUtil;
import com.xingwang.classroom.utils.TimeUtil;
import com.xingwang.classroom.view.ImageWidthHeightView;

import java.util.List;

/**
 * Date:2019/8/21
 * Time;13:57
 * author:baiguiqiang
 */
public class LiveListAdapter extends BaseLoadMoreAdapter<LiveListBean.DataBean> {
    public LiveListAdapter(List<LiveListBean.DataBean> mData) {
        super(mData);
    }

    @SuppressLint("SetTextI18n")
    @Override
    void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HomeViewHolder){
            HomeViewHolder mViewHolder = (HomeViewHolder) viewHolder;
            mViewHolder.title.setText(mDatas.get(i).getTitle());
            mViewHolder.tvTime.setText(TimeUtil.getYMDHMS(String.valueOf(mDatas.get(i).getStart_time())));
            mViewHolder.tvName.setText(mDatas.get(i).getSpeaker());
            mViewHolder.tvDes.setText(Html.fromHtml(mDatas.get(i).getBody()));
            mViewHolder.tvView.setText(mDatas.get(i).getClick()+"次");
            mViewHolder.tvComment.setText(mDatas.get(i).getChat_count()+"条");
            if (mDatas.get(i).getIs_end()==1){
                mViewHolder.tvState.setVisibility(View.VISIBLE);
                LogUtil.i(System.currentTimeMillis()/1000+"");
                if (System.currentTimeMillis()/1000>=mDatas.get(i).getStart_time()){
                    mViewHolder.tvState.setText("正在直播");
                    mViewHolder.tvState.setTextColor(ContextCompat.getColor(mViewHolder.tvState.getContext(), android.R.color.holo_red_light));
                }else {
                    mViewHolder.tvState.setText("即将直播");
                    mViewHolder.tvState.setTextColor(ContextCompat.getColor(mViewHolder.tvState.getContext(), android.R.color.white));
                }
            }else {
                mViewHolder.tvState.setVisibility(View.GONE);
            }
            GlideUtils.loadAvatar(mDatas.get(i).getCover(),mViewHolder.ivThumb);
        }
    }

    @Override
    RecyclerView.ViewHolder onBaseCreateViewHolder(View view,int viewType) {
        return new HomeViewHolder(view);
    }

    @Override
    int getViewLayout(int viewType) {
        return R.layout.item_live_list_classroom;
    }
    class HomeViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView tvTime;
        TextView tvName;
        TextView tvDes;
        TextView tvComment;
        TextView tvView;
        ImageWidthHeightView ivThumb;
        TextView tvState;
        HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.tvTitle);
            tvTime= itemView.findViewById(R.id.tvTime);
            tvName= itemView.findViewById(R.id.tvName);
            tvDes= itemView.findViewById(R.id.tvDes);
            tvView= itemView.findViewById(R.id.tvView);
            tvComment= itemView.findViewById(R.id.tvComment);
            ivThumb= itemView.findViewById(R.id.ivThumb);
            tvState= itemView.findViewById(R.id.tvState);
        }
    }
}
