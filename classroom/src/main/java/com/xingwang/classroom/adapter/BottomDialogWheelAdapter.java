package com.xingwang.classroom.adapter;

import com.contrarywind.adapter.WheelAdapter;

import java.util.ArrayList;

/**
 * Date:2021/9/14
 * Time;11:10
 * author:baiguiqiang
 */
public class BottomDialogWheelAdapter implements WheelAdapter<String> {
    private ArrayList<String> mData;
    public BottomDialogWheelAdapter(ArrayList<String> mData) {
        this.mData = mData;
    }

    @Override
    public int getItemsCount() {
        return mData.size();
    }

    @Override
    public String getItem(int index) {
        return mData.get(index);
    }

    @Override
    public int indexOf(String o) {
        return mData.indexOf(o);
    }
}
