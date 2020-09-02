package com.xingwang.groupchat.adapter;

import android.view.View;
import android.widget.TextView;


import com.xingwang.groupchat.R;
import com.xingwang.groupchat.bean.Teammate;
import com.xingwang.groupchat.callback.OnItemClickListener;
import com.xingwang.groupchat.view.lookup.LeftAdapter;
import com.xingwang.groupchat.view.lookup.LookUpBean;

import java.util.List;

public class LookUpadapter extends LeftAdapter implements View.OnClickListener {
    public LookUpadapter(List<LookUpBean> mDatas) {
        super(mDatas);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.groupchat_item_lookup_team;
    }

    @Override
    protected void onBindViewHolder(LeftViewHolder mViewHolder, LookUpBean mData, int pos) {

        mViewHolder.rlItem.setTag(pos);
        mViewHolder.rlItem.setOnClickListener(this);
        Teammate teammate = (Teammate) mData;
        TextView mCheckBax = mViewHolder.getView(R.id.tv_select);
        mViewHolder.setText(teammate.getShowName(),R.id.tv_content);
        if (teammate.getClick()){
            mViewHolder.rlItem.setEnabled(true);
            mCheckBax.setBackgroundResource(R.drawable.groupchat_group_checkbox_style);
        }else {
            mCheckBax.setBackgroundResource(R.drawable.groupchat_icon_checkbox_noclick);
            mViewHolder.rlItem.setEnabled(false);
        }
        mCheckBax.setSelected(teammate.isCheck());
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    private OnItemClickListener mOnItemClickListener = null;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick((int)v.getTag());
        }
    }
}
