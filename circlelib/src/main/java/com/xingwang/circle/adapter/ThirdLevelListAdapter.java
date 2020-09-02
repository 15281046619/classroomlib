package com.xingwang.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.xingwang.circle.R;
import com.xingwang.circle.bean.Forum;
import com.xingwang.circle.bean.ThirdLevel;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.view.NiceImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class ThirdLevelListAdapter extends BaseAdapter {
    private List<ThirdLevel> list =  new ArrayList<>();
    private Context context;
    private LayoutInflater linearLayout;

    public ThirdLevelListAdapter(Context context, List<ThirdLevel> groupList) {
        this.list=groupList;
        this.context = context;
        linearLayout = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
    @Override
    public ThirdLevel getItem(int position) {
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
            convertView = linearLayout.inflate(R.layout.circle_third_item, null);
            viewHolder.tv_item_name = (TextView) convertView.findViewById(R.id.tv_item_name);
            viewHolder.check_select = (CheckBox) convertView.findViewById(R.id.check_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ThirdLevel thirdLevel=getItem(position);

        viewHolder.tv_item_name.setText(thirdLevel.getCategorys());
        viewHolder.check_select.setChecked(thirdLevel.isSelected());
        return convertView;
    }

    class ViewHolder {
        private TextView tv_item_name;//栏目名称
        private CheckBox check_select;//栏目名称
    }
}
