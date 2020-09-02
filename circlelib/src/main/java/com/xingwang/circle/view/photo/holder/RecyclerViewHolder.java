package com.xingwang.circle.view.photo.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xingwang.circle.view.photo.adapter.RecyclerViewAdapter;
import com.xingwang.circle.view.photo.adapter.ViewHolderHelper;
import com.xingwang.circle.view.photo.listener.OnNoDoubleClickListener;
import com.xingwang.circle.view.photo.listener.OnRVItemClickListener;
import com.xingwang.circle.view.photo.listener.OnRVItemLongClickListener;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
    protected Context mContext;
    protected OnRVItemClickListener mOnRVItemClickListener;
    protected OnRVItemLongClickListener mOnRVItemLongClickListener;
    protected ViewHolderHelper mViewHolderHelper;
    protected RecyclerView mRecyclerView;
    protected RecyclerViewAdapter mRecyclerViewAdapter;

    public RecyclerViewHolder(RecyclerViewAdapter recyclerViewAdapter, RecyclerView recyclerView, View itemView, OnRVItemClickListener onRVItemClickListener, OnRVItemLongClickListener onRVItemLongClickListener) {
        super(itemView);
        mRecyclerViewAdapter = recyclerViewAdapter;
        mRecyclerView = recyclerView;
        mContext = mRecyclerView.getContext();
        mOnRVItemClickListener = onRVItemClickListener;
        mOnRVItemLongClickListener = onRVItemLongClickListener;
        itemView.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (v.getId() == RecyclerViewHolder.this.itemView.getId() && null != mOnRVItemClickListener) {
                    mOnRVItemClickListener.onRVItemClick(mRecyclerView, v, getAdapterPositionWrapper());
                }
            }
        });
        itemView.setOnLongClickListener(this);

        mViewHolderHelper = new ViewHolderHelper(mRecyclerView, this);
    }

    public ViewHolderHelper getViewHolderHelper() {
        return mViewHolderHelper;
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == this.itemView.getId() && null != mOnRVItemLongClickListener) {
            return mOnRVItemLongClickListener.onRVItemLongClick(mRecyclerView, v, getAdapterPositionWrapper());
        }
        return false;
    }

    public int getAdapterPositionWrapper() {
        if (mRecyclerViewAdapter.getHeadersCount() > 0) {
            return getAdapterPosition() - mRecyclerViewAdapter.getHeadersCount();
        } else {
            return getAdapterPosition();
        }
    }
}
