package com.xingwang.circle.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.xingwang.swip.SwipeRefreshAbsListView;

/**
 * SwipeRefreshLayout 包含 ListView 布局<br>
 * 如须自定义加载框，可继承此类重写 {@link #getLoadLayoutResource()}  getLoadLayoutResource}方法
 * <p>Created by hupei on 2016/5/12.
 */
public class SwipeMostRefreshListView extends SwipeRefreshAbsListView<ListView> {

    public SwipeMostRefreshListView(Context context) {
        super(context);
    }

    public SwipeMostRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected ListView createScrollView(Context context, AttributeSet attrs) {
        MostListView listView = new MostListView(context, attrs);
        listView.setId(android.R.id.list);
        return listView;
    }
}
