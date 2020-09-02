package com.xingwang.circle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.xingwang.circle.R;
import com.xingwang.circle.bean.Forum;
import com.xingwang.circle.bean.OnChildClickListener;
import com.xingwang.circle.view.CustomerLinearLayoutManager;

import java.util.List;

/**
 * description:
 * 展示组织关系
 */
public class PartAdapter extends BaseAdapter {

    private Context mContext;
    private List<Forum> teamDatas;

    private OnChildClickListener onChildClickListener;

    public void setChildClickListener(OnChildClickListener onChildClickListener) {
        this.onChildClickListener = onChildClickListener;
    }


    public PartAdapter(Context context){
        mContext = context;
    }

    public PartAdapter(Context context, List<Forum> datas){
        mContext = context;
        this.teamDatas = datas;
    }

    /**
     * 通知数据改变
     *
     * @param datas
     */
    public void setDatsAndNotify(List<Forum> datas) {
        this.teamDatas= datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return EmptyUtils.isEmpty(teamDatas)?0:teamDatas.size();
    }

    @Override
    public Forum getItem(int position) {
        return teamDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getTopLevelView();
        }
        final GroupHolder holder = (GroupHolder) convertView.getTag();

        final Forum item = getItem(position);

        holder.tvName.setText(item.getTitle());
        holder.childLayout.removeAllViews();
        if (item.isExpanded()){
            generateChildView(item, holder.childLayout,1);
        }

        //判断展开状态
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blank(item,holder.childLayout,holder.ivArrow,1);
            }
        });

        return convertView;
    }

    /**
     * 获取层级布局控件
     * @return
     */
    private View getTopLevelView() {
        GroupHolder holder = new GroupHolder();
        View view = LayoutInflater.from(mContext).inflate(R.layout.circle_item_top_part, null);
        holder.tvName = (TextView) view.findViewById(R.id.tv_group_name);
        holder.childLayout = (LinearLayout) view.findViewById(R.id.child_layout);
        holder.layout = (LinearLayout) view.findViewById(R.id.line_layout);
        holder.ivArrow = (ImageView) view.findViewById(R.id.iv_arrow);
        view.setTag(holder);
        return view;
    }

    /**
     * 获取子布局控件
     * @return
     */
    private View getChildView() {
        ChildHolder holder = new ChildHolder();
        View view = LayoutInflater.from(mContext).inflate(R.layout.circle_child_forum, null);
        holder.recycler_forum =  view.findViewById(R.id.recycler_forum);
        view.setTag(holder);
        return view;
    }

    /**
     * 子布局控件数据填充
     *
     * @param parent   该子级的父级
     * @param layout   布局
     * @param level    层级
     */
    public void generateChildView(Forum parent, LinearLayout layout, int level) {

        if (EmptyUtils.isNotEmpty(parent.getCategorys())){//无子级--进入栏目列表
            View view = getChildView();
            ChildHolder holder = (ChildHolder) view.getTag();
            CircleForumAdapter forumAdapter = new CircleForumAdapter(mContext,parent.getCategorys());
            //设置布局方式
            CustomerLinearLayoutManager customerManager=new CustomerLinearLayoutManager(mContext);
            customerManager.setOrientation(CustomerLinearLayoutManager.HORIZONTAL);
            holder.recycler_forum.setLayoutManager(customerManager);
            holder.recycler_forum.setAdapter(forumAdapter);

            forumAdapter.setChildClickListener(new OnChildClickListener() {
                @Override
                public void onItemClick(String categorys) {
                    if (onChildClickListener!=null)
                        onChildClickListener.onForumClick(parent,categorys);
                }

                @Override
                public void onForumClick(Forum forum, String categorys) {

                }
            });

            holder.recycler_forum.setPadding(level * 25, 0, 0, 0);
            layout.addView(view);

        }

        if (parent.hasChild()){//此时仍有子级
            final int tempLevel=level;
            for (final Forum item : parent.getChildForums()) {
                //设置父类、全称、判断是否选中
               // item.setParent(parent);
                View view = getTopLevelView();
                final GroupHolder holder = (GroupHolder) view.getTag();
                holder.tvName.setText(item.getTitle());
                if (EmptyUtils.isNotEmpty(item)) {
                    holder.tvName.setText(item.getTitle());
                    holder.childLayout.removeAllViews();
                    if (item.isExpanded()){
                        holder.ivArrow.setImageResource(R.mipmap.circle_part_arrow_down);
                        generateChildView(item, holder.childLayout,1);
                    }else {
                        holder.ivArrow.setImageResource(R.mipmap.circle_part_arrow_up);
                    }
                }
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        blank(item,holder.childLayout,holder.ivArrow,tempLevel+1);
                    }
                });
                layout.addView(view);
            }
        }
    }

    class GroupHolder {
        TextView tvName;
        LinearLayout childLayout;
        LinearLayout layout;
        ImageView ivArrow;
    }

    class ChildHolder {
        RecyclerView recycler_forum;
    }


    public void blank(Forum item,LinearLayout childLayout,ImageView ivArrow,int level){
        if (item.hasChild()
                || EmptyUtils.isNotEmpty(item.getCategorys())){
            if (item.isExpanded()){//展开状态
                childLayout.removeAllViews();
                ivArrow.setImageResource(R.mipmap.circle_part_arrow_up);
                item.setExpanded(false);
            }else {//收缩状态
                ivArrow.setImageResource(R.mipmap.circle_part_arrow_down);
                item.setExpanded(true);
                generateChildView(item,childLayout, level);
            }
        }else{
            ToastUtils.showShort("该栏目暂无数据");
        }
    }

}
