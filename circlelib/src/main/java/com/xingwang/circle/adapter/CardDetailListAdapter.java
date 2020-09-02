package com.xingwang.circle.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class CardDetailListAdapter extends BaseAdapter {

    private Activity context;

    private List<CommentBean> commentList;

    private OnPCItemClickListener pcItemClickListener;

    public CardDetailListAdapter(Activity context, List<CommentBean> datas) {
        this.context = context;
        this.commentList = datas;
    }

    public void setOnPcItemClickListener(OnPCItemClickListener pcItemClickListener) {
        this.pcItemClickListener = pcItemClickListener;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.circle_comment_item, null);
            viewHolderComment = new ViewHolderComment();
            viewHolderComment.img_head =convertView.findViewById(R.id.img_head);
            viewHolderComment.tv_username =convertView.findViewById(R.id.tv_username);
            viewHolderComment.tv_comment = convertView.findViewById(R.id.tv_comment);
            viewHolderComment.tv_time = convertView.findViewById(R.id.tv_time);
            viewHolderComment.img_comment = convertView.findViewById(R.id.img_comment);
            viewHolderComment.list_child = convertView.findViewById(R.id.list_child);
            viewHolderComment.tv_view_all_reply =  convertView.findViewById(R.id.tv_view_all_reply);
            viewHolderComment.line_child = convertView.findViewById(R.id.line_child);
            viewHolderComment.line_all_reply = convertView.findViewById(R.id.line_all_reply);
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

        viewHolderComment.line_comment_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pcItemClickListener!=null)
                    pcItemClickListener.onClick(commentBean,basePos);
            }
        });

        //跳转至用户详情
        viewHolderComment.line_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeautyDefine.getOpenPageDefine(ActivityUtils.getTopActivity()).toPersonal(Long.parseLong(commentBean.getUser_id()));
            }
        });

        if (commentBean.isChildComment()){//此时存在子评论
            viewHolderComment.line_child.setVisibility(View.VISIBLE);
            List<CommentBean> childComments=commentBean.getSub_comments();
            ChildCommentIInfoAdapter childCommentAdapter=new ChildCommentIInfoAdapter(context,childComments);
            viewHolderComment.list_child.setAdapter(childCommentAdapter);

            childCommentAdapter.setOnChildItemClickListener(new ChildCommentIInfoAdapter.OnChildItemClickListener() {
                @Override
                public void onClick(CommentBean commentBean) {
                    if (pcItemClickListener!=null)
                        pcItemClickListener.onClick(commentBean,basePos);
                }
            });

            viewHolderComment.line_all_reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentInfoActivity.getIntent(context,commentBean);
                }
            });

            if (childComments.size()>2){//此时不完全展示
                viewHolderComment.line_all_reply.setVisibility(View.VISIBLE);
                viewHolderComment.tv_view_all_reply.setText("查看其他回复");
            }else {
                viewHolderComment.line_all_reply.setVisibility(View.GONE);
            }

        }else {
            viewHolderComment.line_child.setVisibility(View.GONE);
            viewHolderComment.line_all_reply.setVisibility(View.GONE);
        }
    }

    class ViewHolderComment {
        private NiceImageView img_head;
        private TextView tv_username;
        private TextView tv_comment;
        private TextView tv_time;
        private ImageView img_comment;
        private MostListView list_child;//子评论
        private LinearLayout line_child;//子评论
        private TextView tv_view_all_reply;//查看全部评论
        private LinearLayout line_all_reply;//查看全部评论
        private LinearLayout line_comment_body;//评论体
        private LinearLayout line_user;//用户
    }

    public interface OnPCItemClickListener{
        //basePos-基础评论的pos
        void onClick(CommentBean commentBean,int basePos);
    }
}
