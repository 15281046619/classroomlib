package com.xingwang.classroom.adapter;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.CommentBean;
import com.xinwang.bgqbaselib.utils.GlideUtils;
import com.xingwang.classroom.view.BaseBarrageAdapter;

import java.util.List;

/**
 * Date:2019/9/12
 * Time;10:56
 * author:baiguiqiang
 */
public class DetailBarrageAdapter extends BaseBarrageAdapter<CommentBean.DataBean.CommentsBean> {
    public DetailBarrageAdapter(List<CommentBean.DataBean.CommentsBean> mLists) {
        super(mLists);
    }
    @Override
    public View getView(ViewGroup viewGroup, int position) {


        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_custom_barrage_view_classroom, viewGroup, false);
        CommentBean.DataBean.CommentsBean mData = mLists.get(position);
        TextView tvContent=  view.findViewById(R.id.tv_content);
        String mBadge="";
        if (!TextUtils.isEmpty(mData.getUser().getBadge())){
            mBadge =mData.getUser().getBadge()+"：";
            tvContent.setTextColor(ContextCompat.getColor(viewGroup.getContext(),R.color.yellowDarkClassRoom));
        }else {
            tvContent.setTextColor(ContextCompat.getColor(viewGroup.getContext(),android.R.color.white));
        }
        tvContent.setText(mBadge);
        tvContent.setSelected(true);
        tvContent.append(TextUtils.isEmpty(mData.getBody())?"[图片]":mData.getBody());
        GlideUtils.loadAvatar(mData.getUser().getAvatar(),R.mipmap.default_teammate_avatar_classroom,view.findViewById(R.id.iv_avatar));
        return view;
    }

    @Override
    public int getShowSum() {
        return 3;
    }
}
