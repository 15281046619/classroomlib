package com.xingwang.groupchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.xingwang.groupchat.R;
import com.xingwang.groupchat.bean.Group;
import com.xingwang.groupchat.view.CircularImage;
import com.xingwang.swip.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class GroupListAdapter extends BaseAdapter {
    private List<Group> list ;
    private Context context;
    private LayoutInflater linearLayout;

    public GroupListAdapter(Context context, List<Group> groupList) {
        this.list=groupList;
        this.context = context;
        linearLayout = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
    @Override
    public Group getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = linearLayout.inflate(R.layout.groupchat_item_group, null);
            viewHolder = new ViewHolder();
            viewHolder.img_head = (CircularImage) convertView.findViewById(R.id.img_head);
            viewHolder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_group_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Group group=getItem(position);

        if (EmptyUtils.isEmpty(group.getAvatar())){
            GlideUtils.loadPic(R.drawable.groupchat_default_group_avatar,viewHolder.img_head,context);
        }else {
            GlideUtils.loadPic(group.getAvatar(),viewHolder.img_head,context);
        }
        viewHolder.tv_group_name.setText(group.getTitle());

        return convertView;
    }

    class ViewHolder {
        private CircularImage img_head;
        private TextView tv_group_name;
    }
}
