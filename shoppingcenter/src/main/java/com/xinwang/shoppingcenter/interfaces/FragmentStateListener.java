package com.xinwang.shoppingcenter.interfaces;

import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.RecyclerView;

/**
 * Date:2021/5/17
 * Time;13:40
 * author:baiguiqiang
 */
public interface  FragmentStateListener {
    void fragmentInitViewSuccess(AppBarLayout appBarLayout, RecyclerView recyclerView);
}
