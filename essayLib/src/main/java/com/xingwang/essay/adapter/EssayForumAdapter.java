package com.xingwang.essay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xingwang.essay.R;
import com.xingwang.essay.bean.EssayTitle;

import java.util.List;

public class EssayForumAdapter extends BaseAdapter {

    private List<EssayTitle> essayTitleList;
    private Context mContext;
    private LayoutInflater mInflater;
    //默认选中位置为0
    private int selectIndex = 0;

    public EssayForumAdapter(Context context) {
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    public EssayForumAdapter(Context context, List<EssayTitle> essayTitleList) {
        this.mContext = context;
        this.essayTitleList = essayTitleList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    public void setDatas(List<EssayTitle> essayTitleList){
        this.essayTitleList=essayTitleList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.essay_item_forum, null);
            holder.tv_essay_title =  convertView.findViewById(R.id.tv_essay_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == selectIndex) {
            holder.tv_essay_title.setSelected(true);
        } else {
            holder.tv_essay_title.setSelected(false);
        }

        holder.tv_essay_title.setText(essayTitleList.get(position).getTitle());

        return convertView;
    }

    @Override
    public int getCount() {
        return essayTitleList==null?0:essayTitleList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    private static class ViewHolder {
        private TextView tv_essay_title ;

    }

    public void setSelectIndex(int i){
        selectIndex = i;
        notifyDataSetChanged();
    }
}
