package com.xinwang.shoppingcenter.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.xinwang.bgqbaselib.adapter.BaseLoadMoreAdapter;
import com.xinwang.bgqbaselib.utils.CommentUtils;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xinwang.bgqbaselib.utils.TimeUtil;
import com.xinwang.shoppingcenter.R;
import com.xinwang.shoppingcenter.bean.CasesListBean;
import com.xinwang.shoppingcenter.bean.GoodsDetailBean;
import com.xinwang.shoppingcenter.bean.ReviewListBean;
import com.xinwang.shoppingcenter.ui.SimplePlayerActivity;
import com.xinwang.shoppingcenter.view.CircularImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2021/3/19
 * Time;14:05
 * author:baiguiqiang
 */
public class ShoppingCasesListAdapter extends BaseLoadMoreAdapter<CasesListBean.CaseBean> {
    private Activity activity;
    private boolean isListPage;//是不是评论列表

    public ShoppingCasesListAdapter(Activity activity, List<CasesListBean.CaseBean> mDatas, boolean  isListPage) {
        super(mDatas);
        this.isListPage = isListPage;
        this.activity =activity;

    }

    @Override
    protected void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof BaseViewHolder){
            BaseViewHolder baseViewHolder = (BaseViewHolder) viewHolder;
            baseViewHolder.tvClick.setText(mDatas.get(i).getClick()+"次点击");
            baseViewHolder.tvTitle.setText(mDatas.get(i).getTitle());
            GlideUtils.loadRoundedCorners(mDatas.get(i).getCover(),R.mipmap.bg_default_placeholder_classroom,baseViewHolder.ivSrc,5);
        }
    }

    @Override
    public RecyclerView.ViewHolder onBaseCreateViewHolder(View view, int viewType) {
        return new BaseViewHolder(view);
    }
    class BaseViewHolder extends RecyclerView.ViewHolder {
        ImageView ivSrc;
        TextView tvTitle,tvClick;
        BaseViewHolder(@NonNull View itemView) {
            super(itemView);

            ivSrc = itemView.findViewById(R.id.ivSrc);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvClick = itemView.findViewById(R.id.tvClick);


        }
    }
    @Override
    public int getViewLayout(int viewType) {
        return R.layout.item_shopping_cases_shoppingcenter;
    }


}
