package com.xingwang.classroom.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.ActivityUtils;
import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.GoodListBean;
import com.xingwang.classroom.utils.GlideUtils;
import com.xingwang.classroom.view.ImageWidthHeightView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class GoodListAdapter extends  RecyclerView.Adapter<GoodListAdapter.ViewHolder>  {
    private List<GoodListBean.GoodBean> list=new ArrayList<>();
    private Context context;
    private OnChildItemClickListener childItemClickListener;

    public GoodListAdapter(Context context, List<GoodListBean.GoodBean> list) {
        this.list=list;
        this.context = context;
    }

    public GoodListAdapter(Context context) {
        this.context = context;
    }

    public void setOnChildItemClickListener(OnChildItemClickListener childItemClickListener) {
        this.childItemClickListener = childItemClickListener;
    }


    public void setDatas(List<GoodListBean.GoodBean> datas) {
        this.list = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_good_list_classroom,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        GoodListBean.GoodBean goodBean=list.get(i);
        viewHolder.tv_good_title.setText(goodBean.getTitle());
        viewHolder.tv_good_price.setText("￥"+goodBean.getPrice());
        viewHolder.tv_good_des.setText(Html.fromHtml(goodBean.getBody()));

        GlideUtils.loadAvatar(goodBean.getCover(),viewHolder.img_good_cover);

        viewHolder.line_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childItemClickListener!=null)
                    childItemClickListener.onClick(goodBean);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageWidthHeightView img_good_cover;
        private LinearLayout line_view;
        private TextView tv_good_title;
        private TextView tv_good_price;
        private TextView tv_good_des;

        public ViewHolder(View convertView) {
            super(convertView);
            line_view=convertView.findViewById(R.id.line_view);
            img_good_cover=convertView.findViewById(R.id.img_good_cover);
            tv_good_title=convertView.findViewById(R.id.tv_good_title);
            tv_good_price=convertView.findViewById(R.id.tv_good_price);
            tv_good_des=convertView.findViewById(R.id.tv_good_des);
        }

    }


    public interface OnChildItemClickListener{
        //basePos-基础评论的pos
        void onClick( GoodListBean.GoodBean goodBean);
    }
}
