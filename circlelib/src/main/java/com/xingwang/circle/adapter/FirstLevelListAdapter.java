package com.xingwang.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xingwang.circle.R;
import com.xingwang.circle.bean.Forum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class FirstLevelListAdapter extends BaseAdapter {
    private List<Forum> list =  new ArrayList<>();
    private Context context;
    private LayoutInflater linearLayout;
    private int pos=0;//选中的位置,默认第一个
    private OnTvClickListener tvClickListener;

    public FirstLevelListAdapter(Context context, List<Forum> groupList) {
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
            convertView = linearLayout.inflate(R.layout.circle_first_level_item, null);
            viewHolder.tv_level_name = (TextView) convertView.findViewById(R.id.tv_level_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

       Forum firstLevel=getItem(position);

        if (position==pos){
            viewHolder.tv_level_name.setBackgroundColor(context.getResources().getColor(R.color.circle_white));
        }else {
            viewHolder.tv_level_name.setBackgroundColor(context.getResources().getColor(R.color.circle_list_bg));
        }

        viewHolder.tv_level_name.setText(firstLevel.getTitle());

        viewHolder.tv_level_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (pos!=position){
                   pos=position;
                   notifyDataSetChanged();

                   if (tvClickListener!=null){
                       tvClickListener.onClick(position);
                   }
               }
            }
        });

        return convertView;
    }

    public void setOnTvClickListener(OnTvClickListener tvClickListener){
        this.tvClickListener=tvClickListener;
    }

    class ViewHolder {
        private TextView tv_level_name;
    }

    public interface OnTvClickListener{
        void onClick(int pos);
    }
}
