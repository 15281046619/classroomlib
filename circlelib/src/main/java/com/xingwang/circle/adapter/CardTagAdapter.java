package com.xingwang.circle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.xingwang.circle.R;
import com.xingwang.circle.bean.CommentBean;
import com.xingwang.circle.bean.Customer;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.view.NiceImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class CardTagAdapter extends  RecyclerView.Adapter<CardTagAdapter.TagViewHolder>  {
    private List<String> list=new ArrayList<>();
    private Context context;

    public CardTagAdapter(Context context, List<String> tagList) {
        this.list=tagList;
        this.context = context;
    }

    public CardTagAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<String> datas) {
        this.list = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.circle_tag_item,viewGroup,false);
        TagViewHolder holder = new TagViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder viewHolder, int i) {
        String tag=list.get(i);
        viewHolder.tv_card_tag.setText(tag);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (EmptyUtils.isEmpty(list))
            return 0;
        return list.size();
    }

    class TagViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_card_tag;

        public TagViewHolder(View convertView) {
            super(convertView);
            tv_card_tag=convertView.findViewById(R.id.tv_card_tag);
        }

    }
}
