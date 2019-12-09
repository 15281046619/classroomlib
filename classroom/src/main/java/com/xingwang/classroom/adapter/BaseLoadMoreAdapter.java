package com.xingwang.classroom.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.ViewHolder.ViewFooterViewHodler;

import java.util.List;

public abstract class BaseLoadMoreAdapter<T>  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    // 普通布局
     final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 正在加载
     private final int LOADING = 1;
    // 加载完成
     private final int LOADING_COMPLETE = 2;
    // 加载到底
     private final int LOADING_END = 3;
    // 当前加载状态，默认为加载完成
    private int loadState = 1;
    List<T> mDatas ;
    private OnItemClickListener mOnItemClickListener;
     BaseLoadMoreAdapter(List<T> mDatas){
        this.mDatas =mDatas;
    }
    public void setLoadState(int loadState) {
        this.loadState = loadState;
        notifyItemChanged(mDatas.size()-1);
    }
    public int getLoadState(){
        return loadState;
    }
    public void setLoadStateNoNotify(int loadState) {
        this.loadState = loadState;
    }

    @Override
    public int getItemCount() {
        if (loadState==LOADING_COMPLETE)
            return mDatas != null ?mDatas.size():0;
        else
            return mDatas != null ? mDatas.size()+1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为FooterView
        if (loadState==LOADING_COMPLETE){
            return TYPE_ITEM;
        }else {
            if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

       if (viewType == TYPE_FOOTER){
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_footer_classroom, viewGroup, false);
            ViewFooterViewHodler mFooter = new ViewFooterViewHodler(view);
            return mFooter;
        }else{
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(getViewLayout(viewType), viewGroup, false);

            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return onBaseCreateViewHolder(view,viewType);
        }
    }

    @Override
    public  void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        if (viewHolder instanceof ViewFooterViewHodler) {
            ViewFooterViewHodler footViewHolder = (ViewFooterViewHodler) viewHolder;
            switch (loadState) {
                case LOADING: // 正在加载
                    footViewHolder.pbLoading.setVisibility(View.VISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.VISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                case LOADING_COMPLETE: // 加载完成
                    footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
                    footViewHolder.llEnd.setVisibility(View.GONE);
                    break;
                case LOADING_END: // 加载到底
                    footViewHolder.pbLoading.setVisibility(View.GONE);
                    footViewHolder.tvLoading.setVisibility(View.GONE);
                    footViewHolder.llEnd.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }else {
            onBaseBindViewHolder(viewHolder,i);
        }

    }
    abstract void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i);
    abstract RecyclerView.ViewHolder onBaseCreateViewHolder(View view,int viewType);
    abstract int getViewLayout(int viewType);
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
