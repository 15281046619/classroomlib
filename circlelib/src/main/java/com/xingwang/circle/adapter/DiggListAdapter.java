package com.xingwang.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xingwang.circle.R;
import com.xingwang.circle.bean.Digg;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.view.NiceImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class DiggListAdapter extends BaseAdapter {
    private List<Digg> list =  new ArrayList<>();
    private Context context;
    private LayoutInflater linearLayout;

    public DiggListAdapter(Context context, List<Digg> groupList) {
        this.list=groupList;
        this.context = context;
        linearLayout = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
    @Override
    public Digg getItem(int position) {
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
            convertView = linearLayout.inflate(R.layout.circle_item_digg, null);
            viewHolder = new ViewHolder();
            viewHolder.img_head = (NiceImageView) convertView.findViewById(R.id.img_head);
            viewHolder.tv_nick_name = (TextView) convertView.findViewById(R.id.tv_nick_name);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Digg digg=getItem(position);

        GlideUtils.loadPic(digg.getUser().getAvatar(),viewHolder.img_head,context.getApplicationContext());
        viewHolder.tv_nick_name.setText(digg.getUser().getNickname());
        viewHolder.tv_time.setText(digg.getDigg_time());

        return convertView;
    }

    class ViewHolder {
        private NiceImageView img_head;
        private TextView tv_nick_name;
        private TextView tv_time;
    }
}
