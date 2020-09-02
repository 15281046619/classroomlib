package com.xingwang.groupchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xingwang.groupchat.R;
import com.xingwang.groupchat.bean.Teammate;
import com.xingwang.groupchat.bean.User;
import com.xingwang.swip.utils.GlideUtils;

import java.util.List;

/**
 * 聊天群成员
 */

public class ChatGroupMemberListAdapter extends BaseAdapter {
    private List<User> list;
    private Context context;
    private LayoutInflater layoutInflater;

    private boolean leader;//是否为群主

    public ChatGroupMemberListAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (!leader){
            if (list.size()>=5)
                return 5;
            if (list.size()<5)
                return list.size();
        }
        if (list.size()>=4){
            return 5;
        }
        return list.size()+1;
    }


    public void isLeader(boolean leader){
        this.leader=leader;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.groupchat_item_chatsetting_gridview,null);
            viewHolder=new ViewHolder();
            viewHolder.iv_pic= (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.tv_username= (TextView) convertView.findViewById(R.id.tv_username);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        if (!leader){
            GlideUtils.loadPic(list.get(position).getAvatar(),viewHolder.iv_pic,context);
            viewHolder.tv_username.setText(list.get(position).getNickname());
            return convertView;
        }
        if((list.size()>=4&&position==4)
        ||position==list.size()){
            GlideUtils.loadPic(R.drawable.groupchat_icon_addavatar_more,viewHolder.iv_pic,context);
            viewHolder.tv_username.setText("");
        }else {
            GlideUtils.loadAvatar(list.get(position).getAvatar(),viewHolder.iv_pic,context);
            viewHolder.tv_username.setText(list.get(position).getNickname());
        }

        return convertView;
    }
    class ViewHolder{
        private ImageView iv_pic;
        private TextView tv_username;
    }
}
