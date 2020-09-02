package com.xingwang.swip.view.popup;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingwang.swip.R;
import com.xingwang.swip.callback.OnItemClickListener;

import java.util.List;

public abstract class BaseLoadMoreAdapter<T>  extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    // 普通布局
    private final int TYPE_ITEM = 1;
    // 脚布局
    private final int TYPE_FOOTER = 2;
    // 正在加载
    public final int LOADING = 1;
    // 加载完成
    public final int LOADING_COMPLETE = 2;
    // 加载到底
    public final int LOADING_END = 3;
    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    protected List<T> mDatas ;
    private OnItemClickListener mOnItemClickListener;
    public BaseLoadMoreAdapter(List<T> mDatas){
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

        if (viewType==TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(getViewLayout(), viewGroup, false);
            //将创建的View注册点击事件
            view.setOnClickListener(this);
            return onBaseCreatViewHolder(view);
        }else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.swip_item_footer, viewGroup, false);
            ViewFooterViewHodler mFooter = new ViewFooterViewHodler(view);
            return mFooter;
        }
    }

    @Override
    public  void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        viewHolder.itemView.setTag(i);
        if (viewHolder instanceof ViewFooterViewHodler) {
            ViewFooterViewHodler footViewHolder = (ViewFooterViewHodler) viewHolder;
            footViewHolder.tv_line.setVisibility(View.GONE);
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
    abstract RecyclerView.ViewHolder onBaseCreatViewHolder(View view);
    abstract int getViewLayout();
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

}
