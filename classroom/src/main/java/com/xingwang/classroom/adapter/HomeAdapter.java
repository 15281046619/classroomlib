package com.xingwang.classroom.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.LectureListsBean;
import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.GlideUtils;

import java.util.List;

/**
 * Date:2019/8/21
 * Time;13:57
 * author:baiguiqiang
 */
public class HomeAdapter extends BaseLoadMoreAdapter<LectureListsBean.DataBean> {
    private int iconHeight ;
    public HomeAdapter(List<LectureListsBean.DataBean> mData, Activity activity) {
        super(mData);
        iconHeight = (CommentUtils.getScreenWidth(activity)-2*CommentUtils.dip2px(activity,15))*9/16;
    }

    @SuppressLint("SetTextI18n")
    @Override
   public void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HomeViewHolder){
            HomeViewHolder mViewHolder = (HomeViewHolder) viewHolder;
            mViewHolder.title.setText(mDatas.get(i).getTitle());
           /* mViewHolder.tvTime.setText(mDatas.get(i).getPublish_time().substring(0,mDatas.get(i).getPublish_time().indexOf(" ")));
            mViewHolder.tvNum.setText(mDatas.get(i).getClick()+"次学习");
            mViewHolder.tvDes.setText(Html.fromHtml(mDatas.get(i).getBody()));*/
            mViewHolder.ivIcon.getLayoutParams().height =iconHeight;
            GlideUtils.loadRoundedCorners(mDatas.get(i).getThumb(),R.mipmap.bg_default_placeholder_classroom,mViewHolder.ivIcon,5);
        }
    }

    @Override
    public RecyclerView.ViewHolder onBaseCreateViewHolder(View view,int viewType) {
        return new HomeViewHolder(view);
    }

    @Override
    public int getViewLayout(int viewType) {
        return R.layout.item_home_classroom;
    }
    class HomeViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        /*TextView tvTime;*/
        /*TextView tvNum;
        TextView tvDes;*/
        ImageView ivIcon;
         HomeViewHolder(@NonNull View itemView) {
            super(itemView);
           title= itemView.findViewById(R.id.title);
           //  tvTime= itemView.findViewById(R.id.tv_time);
          //   tvNum= itemView.findViewById(R.id.tv_num);
           //  tvDes= itemView.findViewById(R.id.tvDes);
             ivIcon= itemView.findViewById(R.id.iv_icon);
        }
    }
}
