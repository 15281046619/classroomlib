package com.xingwang.classroom.view;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2019/9/12
 * Time;10:39
 * author:baiguiqiang
 */
public abstract class BaseBarrageAdapter<T> {
    public List<T> mLists=new ArrayList<>();
    public BaseBarrageAdapter( List<T> mLists ){
        this.mLists .addAll(mLists);
    }
    public abstract View getView(ViewGroup viewGroup, int position);
    public abstract int getShowSum();
    public void setData(List<T> mLists){
        this.mLists = mLists;
    }
    /*
    *添加更新显示效果
     */
    public void notifyItemInserted(CustomBarrageLayout mCustomBarrageLayout,T t){
        if (mLists!=null) {
            mLists.add(0,t);
            mCustomBarrageLayout.addData(this,getView(mCustomBarrageLayout,0));
        }
    }
}
