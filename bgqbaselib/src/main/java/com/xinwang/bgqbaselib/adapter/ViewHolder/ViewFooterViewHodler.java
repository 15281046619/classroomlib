package com.xinwang.bgqbaselib.adapter.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xinwang.bgqbaselib.R;


public class ViewFooterViewHodler extends RecyclerView.ViewHolder {
    public ProgressBar pbLoading;
    public TextView tvLoading;
    public   LinearLayout llEnd;
    public LinearLayout llRoot;
    public ViewFooterViewHodler(@NonNull View itemView) {
        super(itemView);
        pbLoading =  itemView.findViewById(R.id.pb_loading);
        tvLoading =  itemView.findViewById(R.id.tv_loading);
        llEnd =  itemView.findViewById(R.id.ll_end);
        llRoot =  itemView.findViewById(R.id.llRoot);
    }
}
