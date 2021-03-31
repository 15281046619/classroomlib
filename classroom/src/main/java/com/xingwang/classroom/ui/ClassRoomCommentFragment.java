package com.xingwang.classroom.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.beautydefinelibrary.BeautyDefine;
import com.xingwang.classroom.R;
import com.xingwang.classroom.adapter.DetailAdapter;

import com.xinwang.bgqbaselib.view.VpSwipeRefreshLayout;
import com.xingwang.classroom.view.WrapContentLinearLayoutManager;
import com.xinwang.bgqbaselib.base.BaseLazyLoadFragment;
import com.xinwang.bgqbaselib.utils.Constants;
import com.xinwang.bgqbaselib.view.loadmore.EndlessRecyclerOnScrollListener;


/**
 * Date:2019/8/22
 * Time;9:09
 * author:baiguiqiang
 */
public class ClassRoomCommentFragment extends BaseLazyLoadFragment implements DetailAdapter.OnClickDetailItemListener {
    private RecyclerView mRecyclerView;
    private DetailAdapter mAdapter;
    private  ClassRoomDetailActivity mActivity;
    private VpSwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_comment_classroom,container,false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isTouchUi = false;
                    break;
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    isTouchUi =true;
                    break;
            }
            return false;
        });
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        view.findViewById(R.id.iv_close).setOnClickListener(v -> {
            if (mActivity==null&&getActivity()!=null) {
                mActivity = (ClassRoomDetailActivity) getActivity();
            }
            mActivity.setCurFragment(0,true,false);
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.SwipeRefreshLayoutClassRoom);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (mActivity==null&&getActivity()!=null) {
                mActivity = (ClassRoomDetailActivity) getActivity();
            }
            mActivity.requestCommentListData(Constants.LOAD_DATA_TYPE_REFRESH,Integer.MAX_VALUE);
        });
        return view;
    }
    private EndlessRecyclerOnScrollListener mScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadMore() {
            if (mActivity.mComments.size()>0&&!mActivity.isRequesting) {
                mActivity.requestCommentListData(Constants.LOAD_DATA_TYPE_MORE, mActivity.mComments.get(mActivity.mComments.size() - 1).getId());
            }
        }

    };

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter!=null){
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void initData() {
        if (mActivity==null&&getActivity()!=null) {
            mActivity = (ClassRoomDetailActivity) getActivity();
        }
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
        initAdapter();
    }
    public void initAdapter(){
        if (mAdapter==null) {
            mAdapter = new DetailAdapter(mActivity.mComments, BeautyDefine.getUserInfoDefine(getContext()).getUserId(),getActivity(),mActivity.mId);
            mAdapter.setLoadStateNoNotify(mActivity.state);
            mAdapter.setOnDetailItemListener(this);
            mRecyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.setLoadStateNoNotify(mActivity.state);
            mAdapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
        if (mActivity.state==3){
            mRecyclerView.removeOnScrollListener(mScrollListener);
        }else {
            mRecyclerView.removeOnScrollListener(mScrollListener);
            mRecyclerView.addOnScrollListener(mScrollListener);
        }
    }
    public void swipeRefreshUi(int position){

        if (position==-1){//还没有加载所有数据
            if (mActivity!=null) {
                mActivity.viewDot.setVisibility(View.VISIBLE);
            }
        }else {
            if (!isTouchUi) {
                mAdapter.curMovePosition = position;
                initAdapter();
                {
                    if (mActivity!=null&&mActivity.mComments.size()<100)
                        mRecyclerView.smoothScrollToPosition(position);
                    else
                        mRecyclerView.scrollToPosition(position);
                }
            }else if (mActivity!=null) {
                mActivity.viewDot.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    public void onClickItem(int position,int childPos,int type) {
        if (type==1){//对某父及发送评论
            if (getActivity()!=null)
                ((ClassRoomDetailActivity)getActivity()).showCommentPid(position,childPos);
        }
    }
    private boolean isTouchUi = false;//是不是正在触摸界面

}
