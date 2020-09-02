package com.xingwang.swip.view.popup;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xingwang.swip.R;

import java.util.List;

public class PopupWindowListAdapter extends BaseLoadMoreAdapter<String> {
    public PopupWindowListAdapter(List<String> mDatas) {
        super(mDatas);
    }

    @Override
    void onBaseBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        PopupWindowListViewHolder mViewHolder = (PopupWindowListViewHolder) viewHolder;
        mViewHolder.tv_content.setText(mDatas.get(i));

    }

    @Override
    RecyclerView.ViewHolder onBaseCreatViewHolder(View view) {
        return new PopupWindowListViewHolder(view);
    }

    @Override
    int getViewLayout() {
        return R.layout.swip_item_popuwindowlist_adapter;
    }
    private class PopupWindowListViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_content;
        @SuppressLint("ClickableViewAccessibility")
        private PopupWindowListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_content =  itemView.findViewById(R.id.tv_content);
        }
    }
}
