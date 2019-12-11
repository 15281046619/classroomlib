package com.xingwang.classroom.ui;

import com.xingwang.classroom.http.HttpCallBack;
import com.xingwang.classroom.utils.HttpUtil;



import java.io.Serializable;
import java.util.HashMap;

/**
 * Date:2019/8/19
 * Time;14:28
 * author:baiguiqiang
 */
public abstract class BaseNetActivity extends BaseActivity{
    public <T extends Serializable> void  requestGet(String url, HashMap<String, Object> params,Class<T> clas,  HttpCallBack<T> mHttpCallBack){
        HttpUtil.get(url,params,clas,mHttpCallBack,this);
    }
    public <T extends Serializable> void  requestPost(String url, HashMap<String, Object> params,Class<T> clas, HttpCallBack<T> mHttpCallBack){
        HttpUtil.post(url,params,clas,mHttpCallBack,this);
    }

    @Override
    protected void onDestroy() {
        HttpUtil.cancelTag(this);
        super.onDestroy();
    }
}
