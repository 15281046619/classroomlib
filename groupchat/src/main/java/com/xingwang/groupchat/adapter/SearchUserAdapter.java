package com.xingwang.groupchat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.xingwang.groupchat.R;
import com.xingwang.groupchat.bean.Teammate;
import com.xingwang.groupchat.bean.User;
import com.xingwang.groupchat.callback.OnItemClickListener;
import com.xingwang.swip.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangxutong .
 * Date: 16/08/28
 */

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder>{
    protected Context mContext;
    protected List<User> mDatas;
    protected LayoutInflater mInflater;

    public SearchUserAdapter(Context mContext, List<User> mDatas) {
        this.mContext = mContext;
        this.mDatas=mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    private OnItemClickListener mOnItemClickListener = null;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.groupchat_item_group_select,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        final User user = mDatas.get(position);

        //是否为群中成员
        viewHolder.check_select.setSelected(user.isGroup());
        //是否选中
        viewHolder.check_select.setChecked(user.isSelect());

        viewHolder.line_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user.isGroup())
                    return;

                viewHolder.check_select.setChecked(!(viewHolder.check_select.isChecked()));
                user.setSelect(viewHolder.check_select.isChecked());
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position);
                }
            }
        });

        GlideUtils.loadAvatar(user.getAvatar(),viewHolder.img_head,mContext);
        viewHolder.tv_name.setText(user.getNickname());
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name;
        private CheckBox check_select;
        private LinearLayout line_item;
        private ImageView img_head;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            check_select = itemView.findViewById(R.id.check_select);
            line_item =  itemView.findViewById(R.id.line_item);
            img_head =  itemView.findViewById(R.id.iv_pic);
        }
    }
}
