package com.xingwang.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.ConvertUtils;
import com.xingwang.circle.R;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.view.NiceImageView;

import java.util.List;

/**
 * Created by admin on 2017/8/24.
 */

public class CardImgAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private LayoutInflater layoutInflater;

    public CardImgAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        layoutInflater= LayoutInflater.from(context);
    }

    public CardImgAdapter(Context context) {
        this.context = context;
        layoutInflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public String getItem(int position) {
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
            convertView=layoutInflater.inflate(R.layout.circle_card_img_item,null);
            viewHolder=new ViewHolder();
            viewHolder.img= (NiceImageView) convertView.findViewById(R.id.img);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        String thumbUrl=BeautyDefine.getThumbUrlDefine().createThumbUrl(ConvertUtils.dp2px(100),ConvertUtils.dp2px(100),getItem(position));
        GlideUtils.loadPic(thumbUrl,viewHolder.img,context.getApplicationContext());


        return convertView;
    }
    class ViewHolder{
        private NiceImageView img;
    }

}
