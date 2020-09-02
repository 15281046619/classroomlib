package com.xingwang.circle.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.xingwang.circle.CommentInfoActivity;
import com.xingwang.circle.R;
import com.xingwang.circle.bean.CommentBean;
import com.xingwang.circle.bean.Customer;
import com.xingwang.circle.bean.User;
import com.xingwang.circle.view.MostListView;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.view.NiceImageView;

import java.util.List;

public class UsefulCommentAdapter extends BaseAdapter {

    private Activity context;

    private List<CommentBean> commentList;

    public UsefulCommentAdapter(Activity context, List<CommentBean> datas) {
        this.context = context;
        this.commentList = datas;
    }

    @Override
    public int getCount() {
        if (EmptyUtils.isEmpty(commentList))
            return 0;
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderComment viewHolderComment = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.circle_useful_comment_item, null);
            viewHolderComment = new ViewHolderComment();
            viewHolderComment.img_head =convertView.findViewById(R.id.img_head);
            viewHolderComment.rela_avatar =convertView.findViewById(R.id.rela_avatar);
            viewHolderComment.tv_username =convertView.findViewById(R.id.tv_username);
            viewHolderComment.tv_to_username =convertView.findViewById(R.id.tv_to_username);
            viewHolderComment.tv_comment = convertView.findViewById(R.id.tv_comment);
            viewHolderComment.tv_time = convertView.findViewById(R.id.tv_time);
            viewHolderComment.line_comment_body =convertView.findViewById(R.id.line_comment_body);
            viewHolderComment.line_user =convertView.findViewById(R.id.line_user);
            convertView.setTag(viewHolderComment);
        } else {
            viewHolderComment = (ViewHolderComment) convertView.getTag();
        }
        bindViewByComment(viewHolderComment,position);
        return convertView;
    }

    private void bindViewByComment(ViewHolderComment viewHolderComment,int basePos) {
        CommentBean commentBean=commentList.get(basePos);
        viewHolderComment.tv_comment.setText(commentBean.getBody());
        viewHolderComment.tv_time.setText(commentBean.getPublish_time());

        User customer=commentBean.getUser();
        GlideUtils.loadPic(customer.getAvatar(),viewHolderComment.img_head,context.getApplicationContext());
        viewHolderComment.tv_username.setText(customer.getNickname());

        if(viewHolderComment.rela_avatar.getChildCount()==2){
            viewHolderComment.rela_avatar.removeViewAt(1);
        }
        if(viewHolderComment.line_user.getChildCount()==3){
            viewHolderComment.line_user.removeViewAt(2);
        }
        if (!"0".equals(commentBean.getPid())){//此时为子评论
            viewHolderComment.tv_to_username.setText(":"+commentBean.getTo_user().getNickname());
        }else {//此时为评论
            //添加徽章
            String badge=customer.getBadge();
            if (badge.contains("gov")){
                View govView=BeautyDefine.getBadgeUiFactoryDefine().getBadgeUiFactory().getBadgeView(context,"gov");
                viewHolderComment.rela_avatar.addView(govView);
            }
            if (badge.contains("vip")){
                View vipView=BeautyDefine.getBadgeUiFactoryDefine().getBadgeUiFactory().getBadgeView(context,"vip");
                viewHolderComment.line_user.addView(vipView);
            }
            viewHolderComment.tv_to_username.setText("");
        }

        viewHolderComment.line_comment_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentInfoActivity.getIntent(context,commentBean.getId());
            }
        });

        viewHolderComment.tv_to_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeautyDefine.getOpenPageDefine(ActivityUtils.getTopActivity()).toPersonal(Long.parseLong(commentBean.getTo_user_id()));
            }
        });

        //跳转至用户详情
        viewHolderComment.line_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeautyDefine.getOpenPageDefine(ActivityUtils.getTopActivity()).toPersonal(Long.parseLong(commentBean.getUser_id()));
            }
        });
    }

    class ViewHolderComment {
        private NiceImageView img_head;
        private RelativeLayout rela_avatar;
        private TextView tv_username;
        private TextView tv_to_username;
        private TextView tv_comment;
        private TextView tv_time;
        private LinearLayout line_comment_body;//评论体
        private LinearLayout line_user;//用户
    }
}
