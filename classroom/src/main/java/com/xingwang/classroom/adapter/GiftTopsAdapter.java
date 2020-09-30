package com.xingwang.classroom.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.xingwang.classroom.bean.GiftTopsBean;
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
public class GiftTopsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<GiftTopsBean.DataBean> mDatas =new ArrayList<>();

    private DetailAdapter.OnClickDetailItemListener onItemClick;
    private final int ITEM_TYPE_1 =1;
    private final int ITEM_TYPE_2 =2;


    public ArrayList<GiftTopsBean.DataBean> getData(){
        return mDatas;
    }
    public void addData(ArrayList<GiftTopsBean.DataBean> mDatas){
        this.mDatas.addAll(mDatas);
    }
    public void setData(ArrayList<GiftTopsBean.DataBean> mDatas){
        this.mDatas=mDatas;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i==ITEM_TYPE_1)
            return new GifTopsOneViwHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_live_gift_tops_one_classroom,viewGroup,false));
        else
            return new GifTopsViwHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_live_gift_tops_classroom,viewGroup,false));
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return ITEM_TYPE_1;
        }else {
            return ITEM_TYPE_2;
        }

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mViewHolder, @SuppressLint("RecyclerView") int position) {
        if (mViewHolder instanceof GifTopsViwHolder) {
            GifTopsViwHolder mGifTopsViwHolder = (GifTopsViwHolder) mViewHolder;
            mGifTopsViwHolder.tvSum.setText("打赏"+mDatas.get(position+2).getJifen()+"积分");
            mGifTopsViwHolder.tvName.setText(mDatas.get(position+2).getUser().getNickname());
            GlideUtils.loadAvatar(mDatas.get(position+2).getUser().getAvatar()
                    ,R.mipmap.default_teammate_avatar_classroom,mGifTopsViwHolder.ivHead);
            mGifTopsViwHolder.tvPosition.setText(String.valueOf(position+3));
            mGifTopsViwHolder.rlRoot.setOnClickListener(v -> {
                if (onItemClick!=null){
                    onItemClick.onClickItem(position+2,0,0);
                }
            });
        }else if (mViewHolder instanceof GifTopsOneViwHolder){
            GifTopsOneViwHolder mGifTopsOneViwHolder = (GifTopsOneViwHolder) mViewHolder;
            if(mDatas.size()>0){
                mGifTopsOneViwHolder.tvSum1.setText("打赏"+mDatas.get(0).getJifen()+"积分");
                mGifTopsOneViwHolder.tvName1.setText(mDatas.get(0).getUser().getNickname());
                GlideUtils.loadAvatar( mDatas.get(0).getUser().getAvatar()
                        ,R.mipmap.default_teammate_avatar_classroom,mGifTopsOneViwHolder.iv1);
                mGifTopsOneViwHolder.rl1.setVisibility(View.VISIBLE);
                mGifTopsOneViwHolder.rl1.setOnClickListener(v -> {
                    if (onItemClick!=null){
                        onItemClick.onClickItem(0,0,0);
                    }
                });
            }else {
                mGifTopsOneViwHolder.rl1.setVisibility(View.VISIBLE);
                GlideUtils.loadAvatar( ""
                        ,R.mipmap.default_teammate_avatar_classroom,mGifTopsOneViwHolder.iv1);
                mGifTopsOneViwHolder.tvSum1.setText("");
                mGifTopsOneViwHolder.tvName1.setText("虚位以待");
            }

            if (mDatas.size()>1){
                mGifTopsOneViwHolder.tvSum2.setText("打赏"+mDatas.get(1).getJifen()+"积分");

                mGifTopsOneViwHolder.tvName2.setText(mDatas.get(1).getUser().getNickname());
                GlideUtils.loadAvatar( mDatas.get(1).getUser().getAvatar()
                        ,R.mipmap.default_teammate_avatar_classroom,mGifTopsOneViwHolder.iv2);
                mGifTopsOneViwHolder.rl2.setVisibility(View.VISIBLE);
                mGifTopsOneViwHolder.rl2.setOnClickListener(v -> {
                    if (onItemClick!=null){
                        onItemClick.onClickItem(1,0,0);
                    }
                });
            }else {
                mGifTopsOneViwHolder.rl2.setVisibility(View.VISIBLE);
                GlideUtils.loadAvatar( ""
                        ,R.mipmap.default_teammate_avatar_classroom,mGifTopsOneViwHolder.iv2);
                mGifTopsOneViwHolder.tvSum2.setText("");
                mGifTopsOneViwHolder.tvName2.setText("虚位以待");
            }

            if (mDatas.size()>2){
                mGifTopsOneViwHolder.tvSum3.setText("打赏"+mDatas.get(2).getJifen()+"积分");

                mGifTopsOneViwHolder.tvName3.setText(mDatas.get(2).getUser().getNickname());
                GlideUtils.loadAvatar( mDatas.get(2).getUser().getAvatar()
                        ,R.mipmap.default_teammate_avatar_classroom,mGifTopsOneViwHolder.iv3);
                mGifTopsOneViwHolder.rl3.setVisibility(View.VISIBLE);
                mGifTopsOneViwHolder.rl3.setOnClickListener(v -> {
                    if (onItemClick!=null){
                        onItemClick.onClickItem(2,0,0);
                    }
                });
            }else {
                mGifTopsOneViwHolder.rl3.setVisibility(View.VISIBLE);
                GlideUtils.loadAvatar( ""
                        ,R.mipmap.default_teammate_avatar_classroom,mGifTopsOneViwHolder.iv3);
                mGifTopsOneViwHolder.tvSum3.setText("");
                mGifTopsOneViwHolder.tvName3.setText("虚位以待");
            }
        }

    }





    public void setOnItemClick(DetailAdapter.OnClickDetailItemListener onItemClick){
        this.onItemClick =onItemClick;
    }
    @Override
    public int getItemCount() {
        if (mDatas.size()==0){
            return 0;
        }else {
            if (mDatas.size()<=3) {
                return 1;
            } else {
                return mDatas.size() - 2;
            }
        }

    }

    class GifTopsViwHolder extends RecyclerView.ViewHolder{
        private TextView tvPosition,tvName,tvSum;
        private CircularImage ivHead;
        private RelativeLayout rlRoot;
        public GifTopsViwHolder(@NonNull View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvName = itemView.findViewById(R.id.tvName);
            tvSum = itemView.findViewById(R.id.tvSum);
            ivHead = itemView.findViewById(R.id.ivHead);
            rlRoot = itemView.findViewById(R.id.rlRoot);
        }
    }
    class GifTopsOneViwHolder extends RecyclerView.ViewHolder{
        private RelativeLayout rl1,rl2,rl3;
        private CircularImage  iv1,iv2,iv3;
        private TextView  tvName1,tvName2,tvName3,tvSum1,tvSum2,tvSum3;

        public GifTopsOneViwHolder(@NonNull View itemView) {
            super(itemView);
            rl1= itemView.findViewById(R.id.rl1);
            rl2= itemView.findViewById(R.id.rl2);
            rl3= itemView.findViewById(R.id.rl3);
            iv1= itemView.findViewById(R.id.iv1);
            iv2= itemView.findViewById(R.id.iv2);
            iv3= itemView.findViewById(R.id.iv3);
            tvSum1= itemView.findViewById(R.id.tvSum1);
            tvSum2= itemView.findViewById(R.id.tvSum2);
            tvSum3= itemView.findViewById(R.id.tvSum3);
            tvName1= itemView.findViewById(R.id.tvName1);
            tvName2= itemView.findViewById(R.id.tvName2);
            tvName3= itemView.findViewById(R.id.tvName3);

        }
    }
}
