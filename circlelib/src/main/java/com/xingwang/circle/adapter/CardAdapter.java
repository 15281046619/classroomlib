package com.xingwang.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.xingwang.circle.R;
import com.xingwang.circle.bean.Card;
import com.xingwang.circle.bean.CardBody;
import com.xingwang.circle.bean.CardFileType;
import com.xingwang.circle.view.MostGridView;
import com.xingwang.circle.view.photo.CircleImageView;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.view.NiceImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class CardAdapter extends BaseAdapter {
    private List<Card> list =  new ArrayList<>();
    private Context context;
    private LayoutInflater linearLayout;

    public CardAdapter(Context context, List<Card> cardList) {
        this.list=cardList;
        this.context = context;
        linearLayout = LayoutInflater.from(context);
    }

    public CardAdapter(Context context) {
        this.context = context;
        linearLayout = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return list==null?0:list.size();
    }
    @Override
    public Card getItem(int position) {
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
            convertView = linearLayout.inflate(R.layout.circle_card_item, null);
            viewHolder.img_head = (NiceImageView) convertView.findViewById(R.id.img_head);
            viewHolder.tv_card_text = (TextView) convertView.findViewById(R.id.tv_card_text);
            viewHolder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.tv_thumbs = (TextView) convertView.findViewById(R.id.tv_thumbs);
            viewHolder.tv_comments = (TextView) convertView.findViewById(R.id.tv_comments);
            viewHolder.nice_img = (NiceImageView) convertView.findViewById(R.id.nice_img);
            viewHolder.nice_img_video = (NiceImageView) convertView.findViewById(R.id.nice_img_video);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Card card=getItem(position);

        GlideUtils.loadPic(card.getUser().getAvatar(),viewHolder.img_head,context.getApplicationContext());
        viewHolder.tv_username.setText(card.getUser().getNickname());
        viewHolder.tv_comments.setText(card.getComment_num()+" 评论");
        viewHolder.tv_thumbs.setText(card.getDigg_num()+" 点赞");

        CardBody cardBody=card.getCardBody();
        if (EmptyUtils.isEmpty(cardBody))
            return convertView;

        viewHolder.tv_card_text.setText(cardBody.getText());

        switch (card.getType()){
            case CardFileType.IMG://图片
                viewHolder.nice_img_video.setVisibility(View.GONE);
                if (EmptyUtils.isNotEmpty(cardBody.getImgs())){
                    viewHolder.nice_img.setVisibility(View.VISIBLE);
                    GlideUtils.loadPic(cardBody.getImgs().get(0),viewHolder.nice_img,context.getApplicationContext());
                }
                break;
            case CardFileType.VIDEO://视频
                viewHolder.nice_img.setVisibility(View.VISIBLE);
                viewHolder.nice_img_video.setVisibility(View.VISIBLE);
                GlideUtils.loadPic(cardBody.getCover(),viewHolder.nice_img,context.getApplicationContext());
                break;
            case CardFileType.NONE://纯文字
                viewHolder.nice_img_video.setVisibility(View.GONE);
                viewHolder.nice_img.setVisibility(View.GONE);
                break;
        }

        return convertView;
    }

    class ViewHolder {
        private NiceImageView img_head;
        private TextView tv_card_text;
        private TextView tv_username;
        private NiceImageView nice_img;
        private NiceImageView nice_img_video;
        private TextView tv_thumbs;
        private TextView tv_comments;

    }
}
