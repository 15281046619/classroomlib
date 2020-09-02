package com.xingwang.circle.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.ActivityUtils;
import com.xingwang.circle.R;
import com.xingwang.circle.bean.CommentBean;
import com.xingwang.circle.bean.Customer;
import com.xingwang.circle.bean.User;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.view.NiceImageView;

import java.util.ArrayList;
import java.util.List;

public class ChildCommentIInfoAdapter extends BaseAdapter {
    private List<CommentBean> list =  new ArrayList<>();
    private Context context;
    private LayoutInflater linearLayout;

    private OnChildItemClickListener childItemClickListener;

    public ChildCommentIInfoAdapter(Context context, List<CommentBean> commentBeans) {
        this.list=commentBeans;
        this.context = context;
        linearLayout = LayoutInflater.from(context);
    }

    public ChildCommentIInfoAdapter(Context context) {
        this.context = context;
        linearLayout = LayoutInflater.from(context);
    }

    public void setOnChildItemClickListener(OnChildItemClickListener childItemClickListener) {
        this.childItemClickListener = childItemClickListener;
    }

    @Override
    public int getCount() {
        if (list.size()>2)
            return 2;
        return list.size();
    }
    @Override
    public CommentBean getItem(int position) {
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
            convertView = linearLayout.inflate(R.layout.circle_child_comment_item, null);
            viewHolder.img_head = (NiceImageView) convertView.findViewById(R.id.img_head);
            viewHolder.tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            viewHolder.tv_to_username = (TextView) convertView.findViewById(R.id.tv_to_username);
            viewHolder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.img_comment = (ImageView) convertView.findViewById(R.id.img_comment);
            viewHolder.line_user = (LinearLayout) convertView.findViewById(R.id.line_user);
            viewHolder.line_child_body = (LinearLayout) convertView.findViewById(R.id.line_child_body);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CommentBean commentBean=getItem(position);
        viewHolder.tv_comment.setText(commentBean.getBody());
        viewHolder.tv_time.setText(commentBean.getPublish_time());

        User customer=commentBean.getUser();
        GlideUtils.loadPic(customer.getAvatar(),viewHolder.img_head,context.getApplicationContext());
        viewHolder.tv_username.setText(customer.getNickname()+":");
        viewHolder.tv_to_username.setText(commentBean.getTo_user().getNickname());

        viewHolder.line_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeautyDefine.getOpenPageDefine(ActivityUtils.getTopActivity()).toPersonal(customer.getId());
            }
        });

        viewHolder.tv_to_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeautyDefine.getOpenPageDefine(ActivityUtils.getTopActivity()).toPersonal(Long.parseLong(commentBean.getTo_user_id()));
            }
        });

        viewHolder.line_child_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("child","pos-->"+position);
                if (childItemClickListener!=null)
                    childItemClickListener.onClick(commentBean);
            }
        });

        return convertView;
    }

    class ViewHolder {
        private NiceImageView img_head;
        private TextView tv_username;
        private TextView tv_to_username;
        private TextView tv_comment;
        private TextView tv_time;
        private ImageView img_comment;
        private LinearLayout line_user;
        private LinearLayout line_child_body;
    }

    public interface OnChildItemClickListener{
        //basePos-基础评论的pos
        void onClick(CommentBean commentBean);
    }
}
