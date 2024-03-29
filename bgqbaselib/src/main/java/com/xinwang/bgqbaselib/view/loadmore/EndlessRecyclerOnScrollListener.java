package com.xinwang.bgqbaselib.view.loadmore;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public abstract  class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {


// 用来标记是否正在向上滑动

    private boolean isSlidingUpward = false;
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        try {
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            // 当不滑动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 获取最后一个完全显示的itemPosition
                int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
                int itemCount = manager.getItemCount();

                // 判断是否滑动到了最后一个item，并且是向上滑动
                if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
                    // 加载更多
                    onLoadMore();
                }
            }
        }catch (Exception e){
            StaggeredGridLayoutManager manager =(StaggeredGridLayoutManager) recyclerView.getLayoutManager() ;

            // 当不滑动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                // 获取最后一个完全显示的itemPosition
                int[] lastItemPosition = manager.findLastVisibleItemPositions(null);
                int itemCount = manager.getItemCount();

                // 判断是否滑动到了最后一个item，并且是向上滑动
                if (lastItemPosition.length==1) {
                    if (lastItemPosition[0] == (itemCount - 1) && isSlidingUpward) {
                        // 加载更多
                        onLoadMore();
                    }
                }else  if (lastItemPosition.length==2){
                    if (lastItemPosition[1] == (itemCount - 1) && isSlidingUpward) {
                        // 加载更多
                        onLoadMore();
                    }
                }
            }
        }


    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        // 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
        isSlidingUpward = dy > 0;
    }
    public int getScollYDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop()+recyclerView.getPaddingTop();
    }

    /**
     * 加载更多回调
     */

    public abstract void onLoadMore();

}