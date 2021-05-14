package com.xinwang.shoppingcenter.interfaces;

import android.view.View;

/**
 * Date:2021/3/29
 * Time;16:10
 * author:baiguiqiang
 */
public interface AdapterItemClickListener {
    void onClick(int pos, View view);
    void add(int pos);
    void sub(int pos);
    void setNumber(int pos,int sum,int price);
}
