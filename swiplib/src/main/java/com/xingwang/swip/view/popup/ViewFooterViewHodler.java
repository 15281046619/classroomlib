package com.xingwang.swip.view.popup;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xingwang.swip.R;

public class ViewFooterViewHodler extends RecyclerView.ViewHolder {
    public ProgressBar pbLoading;
    public TextView tvLoading;
    public  TextView tv_line;
    public   LinearLayout llEnd;
    public ViewFooterViewHodler(@NonNull View itemView) {
        super(itemView);
        pbLoading =  itemView.findViewById(R.id.pb_loading);
        tvLoading =  itemView.findViewById(R.id.tv_loading);
        tv_line =  itemView.findViewById(R.id.tv_line);
        llEnd =  itemView.findViewById(R.id.ll_end);
    }
}
