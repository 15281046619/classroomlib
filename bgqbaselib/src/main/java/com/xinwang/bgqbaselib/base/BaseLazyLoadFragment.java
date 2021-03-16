package com.xinwang.bgqbaselib.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.xinwang.bgqbaselib.http.HttpCallBack;
import com.xinwang.bgqbaselib.utils.HttpUtil;

import java.io.Serializable;
import java.util.HashMap;


/**
 * 懒加载fragment
 */
public abstract class BaseLazyLoadFragment extends Fragment {
    protected View mRootView;
    public Context mContext;
    protected boolean isVisible = true;
    private boolean isPrepared;
    private boolean isFirst = true;


    public BaseLazyLoadFragment() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = initView(inflater, container, savedInstanceState);
        }
        return mRootView;
    }
    public <T extends Serializable> void  requestGet(String url, HashMap<String, Object> params, Class<T> clas, HttpCallBack<T> mHttpCallBack){
        HttpUtil.get(url,params,clas,mHttpCallBack,this);
    }
    public <T extends Serializable> void  requestPost(String url, HashMap<String, Object> params,Class<T> clas, HttpCallBack<T> mHttpCallBack){
        HttpUtil.post(url,params,clas,mHttpCallBack,this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        HttpUtil.cancelTag(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirst) {
            return;
        }
      //  Log.d(TAG, getClass().getName() + "->initData()");
        initData();
        isFirst = false;
    }

    //do something
    protected void onInvisible() {
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState);

    public abstract void initData();

}
