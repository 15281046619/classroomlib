package com.xingwang.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.xingwang.circle.R;
import com.xingwang.circle.bean.Forum;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.view.NiceImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class SecondLevelListAdapter extends BaseAdapter {
    private List<Forum> list =  new ArrayList<>();
    private Context context;
    private LayoutInflater linearLayout;

    public SecondLevelListAdapter(Context context, List<Forum> groupList) {
        this.list=groupList;
        this.context = context;
        linearLayout = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
    @Override
    public Forum getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = linearLayout.inflate(R.layout.circle_item, null);
            viewHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            viewHolder.tv_card_num = (TextView) convertView.findViewById(R.id.tv_card_num);
            viewHolder.img_item = (NiceImageView) convertView.findViewById(R.id.img_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Forum secondLevel=getItem(position);

        viewHolder.tv_card_num.setText("共"+secondLevel.getThread_num()+"条帖子");
        viewHolder.tv_item_name.setText(secondLevel.getTitle());

        if (EmptyUtils.isNotEmpty(secondLevel.getLogo())){
            GlideUtils.loadPic(secondLevel.getLogo(),viewHolder.img_item,context.getApplicationContext());
        }else {
            GlideUtils.loadPic(viewHolder.img_item,context.getApplicationContext());
        }

        return convertView;
    }

    class ViewHolder {
        private TextView tv_item_name;//栏目名称
        private TextView tv_card_num;//帖子条数
        private NiceImageView img_item;
    }
}
