package com.xingwang.circle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.xingwang.circle.R;
import com.xingwang.circle.bean.OnChildClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class CircleForumAdapter extends  RecyclerView.Adapter<CircleForumAdapter.TagViewHolder>  {
    private List<String> list=new ArrayList<>();
    private Context context;

    private OnChildClickListener childClickListener;

    public void setChildClickListener(OnChildClickListener onChildClickListener) {
        this.childClickListener = onChildClickListener;
    }

    public CircleForumAdapter(Context context, List<String> forumList) {
        this.list=forumList;
        this.context = context;
    }

    public CircleForumAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<String> datas) {
        this.list = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.circle_item_forum,viewGroup,false);
        TagViewHolder holder = new TagViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder viewHolder, int i) {
        String tag=list.get(i);
        viewHolder.tv_forum.setText(tag);
        viewHolder.line_forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childClickListener!=null)
                    childClickListener.onItemClick(tag);
            }
        });

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
        protected LinearLayout line_forum;
        protected TextView tv_forum;

        public TagViewHolder(View convertView) {
            super(convertView);
            line_forum=convertView.findViewById(R.id.line_forum);
            tv_forum=convertView.findViewById(R.id.tv_forum);
        }

    }
}
