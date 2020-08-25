package com.xingwang.classroom.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xingwang.classroom.R;
import com.xingwang.classroom.bean.GoodListBean;
import com.xingwang.classroom.bean.LiveInfoBean;
import com.xingwang.classroom.http.ApiParams;
import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.http.HttpUrls;
import com.xingwang.classroom.utils.Constants;
import com.xingwang.classroom.utils.MyToast;
import com.zzhoujay.richtext.RichText;

/**
 * Date:2020/8/13
 * Time;15:24
 * author:baiguiqiang
 */
public class LiveDesFragment extends BaseLazyLoadFragment {

    protected TextView tv_live_des;
    protected String htmlText;

    public static LiveDesFragment getInstance(String des){
        LiveDesFragment mFragment = new LiveDesFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA,des);
        mFragment.setArguments(bundle);
        return mFragment;
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live_des,container,false);

        tv_live_des=view.findViewById(R.id.tv_live_des);

        return view;
    }

    @Override
    public void initData() {
        htmlText=getArguments().getString(Constants.DATA);
        RichText.from(htmlText).into(tv_live_des);
        //getRequestData();
    }

  /*  private void getRequestData(){
        requestGet(HttpUrls.URL_LIVE_INFO,new ApiParams().with("id",33 ), LiveInfoBean.class, new HttpCallBack<LiveInfoBean>() {

            @Override
            public void onFailure(String message) {
                MyToast.myToast(getContext().getApplicationContext(),message);
            }

            @Override
            public void onSuccess(LiveInfoBean liveInfoBean) {
                RichText.from(liveInfoBean.getData().getLive().getBody()).into(tv_live_des);
            }
        });
    }*/
}
