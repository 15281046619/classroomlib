package com.xingwang.essay.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.xingwang.essay.EssayWebviewActivity;
import com.xingwang.essay.R;
import com.xingwang.essay.bean.Essay;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.view.NiceImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class EssayListAdapter extends RecyclerView.Adapter<EssayListAdapter.ViewHolder> {
    private List<Essay> list =  new ArrayList<>();
    private Context context;
    //private LayoutInflater linearLayout;

    public EssayListAdapter(Context context, List<Essay> essayList) {
        this.list=essayList;
        this.context = context;
        //linearLayout = LayoutInflater.from(context);
    }


   /* @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Essay getItem(int position) {
        return list.get(position);
    }*/

    @NonNull
    @Override
    public EssayListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.essay_item_essay,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EssayListAdapter.ViewHolder viewHolder, int i) {
        Essay essay=list.get(i);

        if (EmptyUtils.isEmpty(essay.getImgList())){
            viewHolder.img.setVisibility(View.GONE);
        }else {
            viewHolder.img.setVisibility(View.VISIBLE);
            GlideUtils.loadPic(essay.getImgList().get(0),viewHolder.img,context.getApplicationContext());
        }
        viewHolder.tv_essay_title.setText(essay.getTitle());
        viewHolder.tv_click.setText(essay.getClick()+" 阅读");
   /*     if (essay.getTop()==1){
            viewHolder.tv_top.setVisibility(View.VISIBLE);
            viewHolder.tv_top.setText("置顶");
        }else {
            viewHolder.tv_top.setVisibility(View.GONE);
        }*/

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(EssayWebviewActivity.getIntent(context,essay));
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = linearLayout.inflate(R.layout.essay_item_essay, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (NiceImageView) convertView.findViewById(R.id.img);
            viewHolder.tv_essay_title = (TextView) convertView.findViewById(R.id.tv_essay_title);
            viewHolder.tv_click = (TextView) convertView.findViewById(R.id.tv_click);
            viewHolder.tv_top = (TextView) convertView.findViewById(R.id.tv_top);
            viewHolder.cardView = (CardView) convertView.findViewById(R.id.cardView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Essay essay=getItem(position);

        if (EmptyUtils.isEmpty(essay.getImgList())){
            viewHolder.img.setVisibility(View.GONE);
        }else {
            viewHolder.img.setVisibility(View.VISIBLE);
            GlideUtils.loadPic(essay.getImgList().get(0),viewHolder.img,context.getApplicationContext());
        }
        viewHolder.tv_essay_title.setText(essay.getTitle());
        viewHolder.tv_click.setText(essay.getClick()+" 阅读");
        if (essay.getTop()==1){
            viewHolder.tv_top.setVisibility(View.VISIBLE);
            viewHolder.tv_top.setText("置顶");
        }else {
            viewHolder.tv_top.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(EssayWebviewActivity.getIntent(context,essay));
            }
        });

        return convertView;
    }
*/

   /* class ViewHolder {
        private NiceImageView img;
        private TextView tv_essay_title;
        private TextView tv_click;
        private TextView tv_top;
        private CardView cardView;
    }*/

    class ViewHolder extends RecyclerView.ViewHolder {

        protected NiceImageView img;
        private TextView tv_essay_title;
        private TextView tv_click;
       // private TextView tv_top;
        private CardView cardView;

        public ViewHolder(View convertView) {
            super(convertView);
            img=convertView.findViewById(R.id.img);
            tv_essay_title=convertView.findViewById(R.id.tv_essay_title);
            tv_click=convertView.findViewById(R.id.tv_click);
            //tv_top=convertView.findViewById(R.id.tv_top);
            cardView=convertView.findViewById(R.id.cardView);
        }

    }
}
