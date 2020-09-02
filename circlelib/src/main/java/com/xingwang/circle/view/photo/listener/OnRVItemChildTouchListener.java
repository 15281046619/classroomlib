package com.xingwang.circle.view.photo.listener;

import android.view.MotionEvent;
import android.view.View;

import com.xingwang.circle.view.photo.holder.RecyclerViewHolder;

public interface OnRVItemChildTouchListener {
    boolean onRvItemChildTouch(RecyclerViewHolder holder, View childView, MotionEvent event);
}
