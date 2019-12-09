package com.xingwang.classroom.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;



/**
 * Date:2019/9/11
 * Time;17:25
 * author:baiguiqiang
 * 自定义弹幕效果
 */
public class CustomBarrageLayout extends LinearLayout {
    private boolean isAdding  = false;
    public CustomBarrageLayout(Context context) {
        this(context,null);
    }

    public CustomBarrageLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomBarrageLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setAdapter(BaseBarrageAdapter mAdapter){
        setAdapter(mAdapter,false);

    }
    /**
     *
     * @param mAdapter
     * @param isInverted 是不是倒叙显示
     */
    public void setAdapter(BaseBarrageAdapter mAdapter,boolean isInverted){
        for (int i=0;i<mAdapter.getShowSum();i++){
            if (i<mAdapter.mLists.size()){
                //倒顺显示
                addView(mAdapter.getView(this,isInverted?(mAdapter.mLists.size()-1-i):i));
            }
        }
    }

    void addData(BaseBarrageAdapter mAdapter,View view){
        if (getChildCount()<mAdapter.getShowSum()){
            addView(view, 0);
        }else {
            if (isAdding) {
                removeCallbacks(runnable);
                removeViewAt(getChildCount() - 1);
            }
            isAdding = true;
            addView(view, 0);
            postDelayed(runnable, 300);
        }
    }
    private Runnable runnable= () -> {
        removeViewAt(getChildCount()-1);
        isAdding = false;
    };

    public void onDestroy(){
        if (runnable!=null)
            removeCallbacks(runnable);
    }

}
