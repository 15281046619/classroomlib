package com.xingwang.classroom.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xingwang.classroom.ClassRoomLibUtils;
import com.xingwang.classroom.R;

import com.xingwang.classroom.adapter.HomeAdapter;
import com.xingwang.classroom.bean.LectureListsBean;
import com.xingwang.classroom.dialog.BottomADSheetDialog;
import com.xingwang.classroom.dialog.CenterQuiteDialog;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.MyToast;
import com.xingwang.classroom.utils.SharedPreferenceUntils;
import com.xingwang.classroom.view.DividerItemDecoration;
import com.xingwang.classroom.view.loadmore.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Date:2019/8/21
 * Time;14:16
 * author:baiguiqiang
 */
public class ClassRoomHomeFragment extends BaseLazyLoadFragment {
    private int pageNum =10;
    private RecyclerView recyclerview;
    private String category;
    private List<LectureListsBean.DataBean> mData = new ArrayList<>();
    private HomeAdapter mAdapter;
    public static ClassRoomHomeFragment getInstance(int position,String category){
        ClassRoomHomeFragment mFragment = new ClassRoomHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putString("category",category);
        mFragment.setArguments(bundle);
        return mFragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home_classroom,container,false);
        recyclerview =view.findViewById(R.id.recyclerview);
        return view;
    }

    @Override
    public void initData() {
        //  position =getArguments().getInt("position");
        if(getArguments()!=null)
            category =getArguments().getString("category");
        else
            category ="全部";
        if (getContext()!=null) {
            recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        }
        requestHttpData(Constants.LOAD_DATA_TYPE_INIT,Integer.MAX_VALUE);
        recyclerview.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if (mData.size()>0&&!isRequesting)
                    requestHttpData(Constants.LOAD_DATA_TYPE_MORE,mData.get(mData.size()-1).getId());

            }
        });
    }
    boolean isRequesting =false;
    void requestHttpData(int loadDataTypeInit,int id) {
        ApiParams mApiParams = new ApiParams().with("num", pageNum).with("backward_div_id", id);
        if(!category.equals("-1"))
            mApiParams.with("category_id", category);
        isRequesting =true;
        requestGet(HttpUrls.URL_LISTS,mApiParams, LectureListsBean.class, new HttpCallBack<LectureListsBean>() {
            @Override
            public void onFailure(String message) {
                if (loadDataTypeInit!=Constants.LOAD_DATA_TYPE_MORE){
                    stopRefreshAnimation();
                }
                isRequesting =false;
                MyToast.myToast(getContext(),message);
            }

            @Override
            public void onSuccess(LectureListsBean lectureListsBean) {
                if (loadDataTypeInit!=Constants.LOAD_DATA_TYPE_MORE){
                    stopRefreshAnimation();
                    mData.clear();
                    mData.addAll(lectureListsBean.getData());
                }else {
                    mData.addAll(lectureListsBean.getData());
                }
                if(lectureListsBean.getData().size()<pageNum){
                    initAdapter(3);
                }else
                    initAdapter(1);
                isRequesting =false;
            }
        });
    }

    private void stopRefreshAnimation(){
        if (getActivity()!=null) {
            ClassRoomHomeActivity mActivity = (ClassRoomHomeActivity) getActivity();
            mActivity.refreshFinish();
        }
    }
    private void initAdapter(int state){
        if (mAdapter==null) {
            mAdapter = new HomeAdapter(mData);
            mAdapter.setLoadStateNoNotify(state);
            mAdapter.setOnItemClickListener((view, position) ->  {
                ClassRoomLibUtils.startDetailActivityType(getActivity(), mData.get(position).getId(),mData.get(position).getType());
            });
            recyclerview.setAdapter(mAdapter);
        }else {
            mAdapter.setLoadStateNoNotify(state);
            mAdapter.notifyDataSetChanged();
        }
    }


}
