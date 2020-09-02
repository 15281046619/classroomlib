package com.xingwang.groupchat.adapter;

import android.content.Context;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.xingwang.groupchat.R;
import com.xingwang.groupchat.bean.Teammate;
import com.xingwang.groupchat.bean.User;
import com.xingwang.groupchat.view.CircularImage;
import com.xingwang.swip.utils.Constants;
import com.xingwang.swip.utils.GlideUtils;

import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class GroupTransferAdapter extends BaseAdapter {
    private List<User> list;
    private Context context;
    private LayoutInflater linearLayout;
    private int flag;//页面功能标识

    public GroupTransferAdapter(Context context, List<User> teammates) {
        this.list=teammates;
        this.context = context;
        linearLayout = LayoutInflater.from(context);
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
    @Override
    public User getItem(int position) {
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
            convertView = linearLayout.inflate(R.layout.groupchat_item_transfer_group, null);
            viewHolder = new ViewHolder();
            viewHolder.img_head =convertView.findViewById(R.id.img_head);
            viewHolder.tv_group_name = convertView.findViewById(R.id.tv_group_name);
            viewHolder.tv_admin_info = convertView.findViewById(R.id.tv_admin_info);
            viewHolder.tb_open_voice = convertView.findViewById(R.id.tb_open_voice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User user=getItem(position);

        GlideUtils.loadAvatar(user.getAvatar(),viewHolder.img_head,context);
        viewHolder.tv_group_name.setText(user.getNickname());

        //非群管理&&屏蔽功能方显示
        if (flag== Constants.GROUP_BLOCK_FLAG){
            viewHolder.tb_open_voice.setVisibility(View.VISIBLE);
            if (user.getIs_hidden()==1){//此时屏蔽
                viewHolder.tb_open_voice.setChecked(true);
            }else {
                viewHolder.tb_open_voice.setChecked(false);
            }
        }else {
            viewHolder.tb_open_voice.setVisibility(View.GONE);
        }

        if (user.getIs_manager()==1){//此时为管理员
            viewHolder.tv_admin_info.setVisibility(View.VISIBLE);

        }else {
            viewHolder.tv_admin_info.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        private CircularImage img_head;
        private TextView tv_group_name;
        private ToggleButton tb_open_voice;
        private TextView tv_admin_info;
    }
}
