package com.xingwang.circle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beautydefinelibrary.BeautyDefine;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.EmptyUtils;
import com.xingwang.circle.R;
import com.xingwang.circle.bean.Card;
import com.xingwang.circle.bean.CommentBean;
import com.xingwang.circle.bean.Customer;
import com.xingwang.circle.bean.User;
import com.xingwang.circle.view.FlowsLayout;
import com.xingwang.circle.view.MostGridView;
import com.xingwang.circle.view.MyJzvdStd;
import com.xingwang.swip.utils.ActivityManager;
import com.xingwang.swip.utils.GlideUtils;
import com.xingwang.swip.view.NiceImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/8/15.
 */

public class ChildCommentAdapter extends  RecyclerView.Adapter<ChildCommentAdapter.ViewHolder>  {
    private List<CommentBean> list=new ArrayList<>();
    private Context context;
    private OnChildItemClickListener childItemClickListener;

    public ChildCommentAdapter(Context context, List<CommentBean> commentBeans) {
        this.list=commentBeans;
        this.context = context;
    }

    public ChildCommentAdapter(Context context) {
        this.context = context;
    }

    public void setOnChildItemClickListener(OnChildItemClickListener childItemClickListener) {
        this.childItemClickListener = childItemClickListener;
    }


    public void setDatas(List<CommentBean> datas) {
        this.list = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.circle_child_comment_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CommentBean commentBean=list.get(i);
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
                if (childItemClickListener!=null)
                    childItemClickListener.onClick(commentBean);
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

    class ViewHolder extends RecyclerView.ViewHolder {

        private NiceImageView img_head;
        private TextView tv_username;
        private TextView tv_to_username;
        private TextView tv_comment;
        private TextView tv_time;
        private ImageView img_comment;
        private LinearLayout line_user;
        private LinearLayout line_child_body;

        public ViewHolder(View convertView) {
            super(convertView);
            img_head=convertView.findViewById(R.id.img_head);
            line_user=convertView.findViewById(R.id.line_user);
            tv_username=convertView.findViewById(R.id.tv_username);
            tv_comment=convertView.findViewById(R.id.tv_comment);
            tv_to_username=convertView.findViewById(R.id.tv_to_username);
            tv_time=convertView.findViewById(R.id.tv_time);
            line_child_body=convertView.findViewById(R.id.line_child_body);
        }

    }


    public interface OnChildItemClickListener{
        //basePos-基础评论的pos
        void onClick(CommentBean commentBean);
    }
}
